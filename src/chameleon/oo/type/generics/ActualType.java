/**
 * 
 */
package chameleon.oo.type.generics;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeIndirection;
import chameleon.util.Pair;
import chameleon.util.Util;

public class ActualType extends TypeIndirection {

		public ActualType(SimpleNameSignature sig, Type aliasedType, TypeParameter parameter) {
			super(sig,aliasedType);
			setParameter(parameter);
		}
		
		@Override
		public List<Type> getDirectSuperTypes() throws LookupException {
			return aliasedType().getDirectSuperTypes();
//			return Util.createNonNullList(aliasedType());
		}

		@Override
		public Type clone() {
			return new ActualType(signature().clone(), aliasedType(),parameter());
		}
		
		@Override
		public Type actualDeclaration() {
			return this;
		}
		
		@Override
		public boolean uniSameAs(Element element) throws LookupException {
			boolean result = false;
			if(element instanceof ActualType) {
				result = parameter().sameAs(((ActualType)element).parameter());
			} 
			if(! result) {
			  result = element.sameAs(aliasedType());
			}
			return result;
		}
		
		public boolean uniSameAs(Type element, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
			boolean result = false;
			if(element instanceof ActualType) {
				TypeParameter mine = parameter();
				TypeParameter others = ((ActualType)element).parameter();
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
		
	}