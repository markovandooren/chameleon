package be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

/**
 * @author Marko van Dooren
 */
public abstract class Import extends ElementImpl {

  public Import() {
	}
  
// THIS IS COVERED IN NAMESPACEPART
  
  public abstract List<Declaration> directImports() throws LookupException;
  
  public abstract List<Declaration> demandImports() throws LookupException;

  public abstract <D extends Declaration> List<? extends SelectionResult> directImports(DeclarationSelector<D> selector) throws LookupException;
  
  public abstract <D extends Declaration> List<? extends SelectionResult> demandImports(DeclarationSelector<D> selector) throws LookupException;
  
  public boolean importsSameAs(Import other) throws LookupException {
	  boolean result = sameDeclarations(demandImports(), other.demandImports());
	  result = result && sameDeclarations(directImports(), other.directImports());
	  return result;
  }

  private boolean sameDeclarations(List<Declaration> mine,
		  final List<Declaration> others) throws LookupException {
	  boolean result = new AbstractPredicate<Declaration, LookupException>() {

		  @Override
		  public boolean eval(final Declaration m) throws LookupException {
			  return new AbstractPredicate<Declaration, LookupException>() {

				  @Override
				  public boolean eval(Declaration o) throws LookupException {
					  boolean result = m.sameAs(o);
					  return result;
				  }

			  }.exists(others);
		  }
	  }.forAll(mine);
	  return result;
  }

  @Override
  public LookupContext lexicalContext() throws LookupException {
  	return view().namespace().targetContext();
  }
  
}
