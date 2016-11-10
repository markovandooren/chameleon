package org.aikodi.chameleon.support.member.simplename.method;


import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.MethodHeader;
import org.aikodi.chameleon.oo.method.RegularMethod;


/**
 * @author Marko van Dooren
 */
public class NormalMethod extends RegularMethod {

  public NormalMethod(MethodHeader header) {
    super(header);
  }
  
	@Override
   public boolean sameKind(Method other) {
  	return(other instanceof NormalMethod);
  }  

	@Override
   protected NormalMethod cloneSelf() {
    return new NormalMethod(null);
  }
	
//  @Override
//public HidesRelation<? extends Member> hidesRelation() {
//		return _hidesSelector;
//  }
  
//  private static HidesRelation<NormalMethod> _hidesSelector = new HidesRelation<NormalMethod>(NormalMethod.class) {
//		
//		@Override
//      public boolean containsBasedOnRest(NormalMethod first, NormalMethod second) throws LookupException {
//			boolean result = first.name().equals(second.name()) 
//					          && first.isTrue(first.language(ObjectOrientedLanguage.class).INSTANCE) 
//			              && first.signature().sameParameterBoundsAs(second.signature()) 
//			              && first.nearestAncestor(Type.class).subtypeOf(second.nearestAncestor(Type.class));
//			return result;
//		}
//	};

}


