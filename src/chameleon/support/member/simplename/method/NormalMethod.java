package chameleon.support.member.simplename.method;


import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.member.DeclarationWithParametersSignature;
import chameleon.oo.member.HidesRelation;
import chameleon.oo.member.Member;
import chameleon.oo.method.Method;
import chameleon.oo.method.MethodHeader;
import chameleon.oo.method.RegularMethod;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;


/**
 * @author Marko van Dooren
 */
public class NormalMethod<E extends RegularMethod<E,H,S>, H extends MethodHeader<H, S>, S extends DeclarationWithParametersSignature> extends RegularMethod<E,H,S>  {

  public NormalMethod(H header) {
    super(header);
  }
  
	public boolean sameKind(Method other) {
  	return(other instanceof NormalMethod);
  }  

	protected E cloneThis() {
    return (E) new NormalMethod(header().clone());
  }
	
  public HidesRelation<? extends Member> hidesRelation() {
		return _hidesSelector;
  }
  
  private static HidesRelation<NormalMethod> _hidesSelector = new HidesRelation<NormalMethod>(NormalMethod.class) {
		
		public boolean containsBasedOnRest(NormalMethod first, NormalMethod second) throws LookupException {
			boolean result = first.isTrue(((ObjectOrientedLanguage)first.language(ObjectOrientedLanguage.class)).INSTANCE) 
			              && first.signature().sameParameterBoundsAs(second.signature()) 
			              && ((Type)first.nearestAncestor(Type.class)).subTypeOf((Type)second.nearestAncestor(Type.class));
			return result;
		}
	};

}


