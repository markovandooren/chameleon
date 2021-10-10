package org.aikodi.chameleon.oo.type.generics;

import com.google.common.collect.ImmutableList;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.ParameterBlock;
import org.aikodi.chameleon.util.Lists;

import java.util.List;

/**
 * WARNING! If you use a parameter block as an subelement of a class X, then you must add
 * a lookupstrategy to X that directly returns parameters(). Declarations() returns stubs
 * to support recursion in generic parameters. If anything is wrong in a type reference of a generic parameter,
 * then it won't be noticed by the lazy alias until an operation is executed on the alias by the declaration
 * selector. At that point, there should be no exception. If you use parameters() instead, any problems will be
 * detected before the declaration selector can do its job. because of this, lazy alias ignores exceptions
 * during lookup of the aliased type (the upperbound of generic parameter) because they cannot occur anymore
 * at that stage.
 *   
 * @author Marko van Dooren
 */
public class TypeParameterBlock extends ParameterBlock<TypeParameter> implements DeclarationContainer {

	public TypeParameterBlock() {
		super(TypeParameter.class);
	}
	
	@Override
	public TypeParameterBlock cloneSelf() {
		return new TypeParameterBlock();
	}


	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
	  if(_localDeclarationCache == null) {
	    synchronized (this) {
	      if(_localDeclarationCache == null) {
	        List<Declaration> result = Lists.create();
	        BlockFixer stub = new BlockFixer();
	        stub.setUniParent(this);
	        for(TypeParameter parameter:parameters()) {
	          TypeParameter clone = clone(parameter);
	          clone.setOrigin(parameter);
	          result.add(clone);
	          stub.add(clone);
	        }
	        _localDeclarationCache = ImmutableList.copyOf(result);
	      }        
      }
	  }
    return _localDeclarationCache;
	}
	
	private List<Declaration> _localDeclarationCache;

	@Override
   public LookupContext lookupContext(Element element) throws LookupException {
		if(element instanceof TypeParameterFixer) {
			return parent().lookupContext(this);
		} else {
			if(_lexical == null) {
				_lexical = language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
			}
			return _lexical;
		}
	}
	
	private LookupContext _lexical;
}
