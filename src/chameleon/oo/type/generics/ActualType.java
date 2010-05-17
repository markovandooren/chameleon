/**
 * 
 */
package chameleon.oo.type.generics;

import java.util.List;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeIndirection;

public class ActualType extends TypeIndirection {

		public ActualType(SimpleNameSignature sig, Type aliasedType, TypeParameter parameter) {
			super(sig,aliasedType);
			setParameter(parameter);
		}

		
		
		@Override
		public List<Type> getDirectSuperTypes() throws LookupException {
			return aliasedType().getDirectSuperTypes();
		}



		@Override
		public Type clone() {
			return new ActualType(signature().clone(), aliasedType(),parameter());
		}
		
		@Override
		public Type actualDeclaration() {
			//debug
//			return aliasedType();
			return this;
		}
		
		@Override
		public boolean uniSameAs(Element element) {
			return element.equals(aliasedType());
		}
		
		public TypeParameter parameter() {
			return _parameter;
		}
		
		private void setParameter(TypeParameter parameter) {
			_parameter = parameter;
		}
		
		private TypeParameter _parameter;
		
	}