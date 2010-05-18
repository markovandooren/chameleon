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
		public boolean uniSameAs(Element element) throws LookupException {
//			
//			String x = getFullyQualifiedName();
//			String y = ((Type)element).getFullyQualifiedName();
//			if(x.equals("org.rejuse.property.Property.F") && y.equals("org.rejuse.property.Property.F")) {
//				System.out.println("Checking uniSameAs of:"+x+" and "+y);
//			}
			boolean result = false;
			if(element instanceof ActualType) {
				result = parameter().sameAs(((ActualType)element).parameter());
			} 
			if(! result) {
			  result = element.sameAs(aliasedType());
			}
//			System.out.println("uniSameAs= "+result);
			return result;
		}
		
		public TypeParameter parameter() {
			return _parameter;
		}
		
		private void setParameter(TypeParameter parameter) {
			_parameter = parameter;
		}
		
		private TypeParameter _parameter;
		
	}