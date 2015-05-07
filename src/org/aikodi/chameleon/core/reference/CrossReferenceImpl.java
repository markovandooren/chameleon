package org.aikodi.chameleon.core.reference;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicReference;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * An class with default implementations for cross-references.
 * 
 * @author Marko van Dooren
 *
 * @param <D> The type of the declaration that is referenced by the cross-reference.
 */
public abstract class CrossReferenceImpl<D extends Declaration> extends ElementImpl implements CrossReference<D> {

  /**
   * The type of an expression is cached to increase performance. Call {@link #flushCache()} to
   * flush the cache of the model has changed.
   * 
   * The reference is stored in a soft reference to allow garbage collection of cached types.
   * 
   * The soft reference is stored in an atomic reference to deal with concurrent lookups of the
   * type of this expression without needing a lock.
   */
  private final AtomicReference<SoftReference<D>> _cache = new AtomicReference<>();

  protected D getCache() {
    SoftReference<D> cache = _cache.get();
    D result = (cache == null ? null: cache.get());
    return result;
  }

  @Override
  public void flushLocalCache() {
    super.flushLocalCache();
    boolean success = false;
    do {
      success = _cache.compareAndSet(_cache.get(), null);
    } while (!success);

  }

  protected void setCache(D value) {
    if (Config.cacheElementReferences() == true) {
      // We only try to set once. Concurrent lookups of this name
      // should result in the same element.
      _cache.compareAndSet(null, new SoftReference<D>(value));
    }
  }


  /**
   * Return the declaration selector that is responsible for selecting the
   * declaration referenced by this cross-reference.
   * 
   * @return
   */
  public abstract DeclarationSelector<D> selector();

  @Override
  public D getElement() throws LookupException {
    return getElement(selector());
  }

  @Override
  public Declaration getDeclarator() throws LookupException {
    //      return getElement(new DeclaratorSelector(selector()));
    //FIXME Now that Declaration has a declarator() method, do we still
    //need the separate getElement(DeclaratorSelector) method? 
    return getElement().declarator();
  }

  protected <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
    X result = null;

    // OPTIMISATION
    boolean cache = selector.equals(selector());
    if (cache) {
      result = (X) getCache();
    }
    if (result != null) {
      return result;
    }
    synchronized (this) {
      DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
      lookupContext().lookUp(collector);
      result = collector.result();
      if (cache) {
        setCache((D) result);
      }
      return result;
    }
  }

  protected LookupContext lookupContext() throws LookupException {
    return lexicalContext();
  }

  @Override
  public Verification verifySelf() {
    try {
      return getElement() != null ? Valid.create() : new UnresolvableCrossReference(this);
    } catch (LookupException e) {
      return new UnresolvableCrossReference(this, e.getMessage());
    } catch (ChameleonProgrammerException e) {
      return new UnresolvableCrossReference(this, e.getMessage());
    }
  }

}
