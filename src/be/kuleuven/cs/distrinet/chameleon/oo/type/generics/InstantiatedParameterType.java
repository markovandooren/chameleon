/**
 * 
 */
package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeIndirection;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

public class InstantiatedParameterType extends TypeIndirection {

		public InstantiatedParameterType(SimpleNameSignature sig, Type aliasedType, TypeParameter parameter) {
			super(sig,aliasedType);
			setParameter(parameter);
		}
		
		@Override
		public String toString() {
		  return parameter().toString();
		}
		
		@Override
		public List<Type> getDirectSuperTypes() throws LookupException {
//			return aliasedType().getDirectSuperTypes();
			return Util.createNonNullList(aliasedType());
		}

		@Override
    public void accumulateAllSuperTypes(Set<Type> acc) throws LookupException {
    	Type aliased =aliasedType();
    		boolean add=true;
    		for(Type acced: acc) {
    			if(acced == aliased) {
    				add=false;
    				break;
    			}
    		}
    		if(add) {
    			acc.add(aliased);
    		  aliased.accumulateAllSuperTypes(acc);
    		}
    }
    
    
		public void newAccumulateAllSuperTypes(Set<Type> acc) throws LookupException {
			Type aliased =aliasedType();
			boolean add=true;
			for(Type acced: acc) {
				if(acced == aliased) {
					add=false;
					break;
				}
			}
			if(add) {
				aliased.newAccumulateSelfAndAllSuperTypes(acc);
			}
		}

    
		@Override
		public InstantiatedParameterType cloneSelf() {
			return new InstantiatedParameterType(null, aliasedType(),parameter());
		}
		
		@Override
		public Type actualDeclaration() {
			return this;
		}
		
		@Override
		public boolean uniSameAs(Element element) throws LookupException {
			boolean result = false;
			if(element instanceof InstantiatedParameterType) {
				result = parameter().sameAs(((InstantiatedParameterType)element).parameter());
			} 
			if(! result) {
			  result = element.sameAs(aliasedType());
			}
			return result;
		}
		
		public boolean uniSameAs(Type element, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
			boolean result = false;
			if(element instanceof InstantiatedParameterType) {
				TypeParameter mine = parameter();
				TypeParameter others = ((InstantiatedParameterType)element).parameter();
				result = mine.sameAs(others);
				if(! result) {
					for(Pair<TypeParameter, TypeParameter> pair: trace) {
						if(mine.sameAs(pair.first()) && others.sameAs(pair.second())) {
							return true;
						}
					}
					trace.add(new Pair<TypeParameter, TypeParameter>(mine, others));
				}
			} 
			if(! result) {
				result = ((Type)element).sameAs(aliasedType(),trace);
			}
			return result;
		}

		public TypeParameter parameter() {
			return _parameter;
		}
		
		private void setParameter(TypeParameter parameter) {
			_parameter = parameter;
		}
		
		private TypeParameter _parameter;

		public Declaration declarator() {
			return parameter();
		}
		
		@Override
		public boolean auxSubTypeOf(Type other) throws LookupException {
		  return aliasedType().subTypeOf(other);
		}
		
		@Override
		public boolean auxSuperTypeOf(Type type) throws LookupException {
		  return type.subTypeOf(aliasedType());
		}
	}
