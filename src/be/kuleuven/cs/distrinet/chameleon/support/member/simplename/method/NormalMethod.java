package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.method;


import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.member.HidesRelation;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.MethodHeader;
import be.kuleuven.cs.distrinet.chameleon.oo.method.RegularMethod;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;


/**
 * @author Marko van Dooren
 */
public class NormalMethod extends RegularMethod {

  public NormalMethod(MethodHeader header) {
    super(header);
  }
  
	public boolean sameKind(Method other) {
  	return(other instanceof NormalMethod);
  }  

	protected NormalMethod cloneSelf() {
    return new NormalMethod(null);
  }
	
  public HidesRelation<? extends Member> hidesRelation() {
		return _hidesSelector;
  }
  
  private static HidesRelation<NormalMethod> _hidesSelector = new HidesRelation<NormalMethod>(NormalMethod.class) {
		
		public boolean containsBasedOnRest(NormalMethod first, NormalMethod second) throws LookupException {
			boolean result = first.name().equals(second.name()) 
					          && first.isTrue(((ObjectOrientedLanguage)first.language(ObjectOrientedLanguage.class)).INSTANCE) 
			              && first.signature().sameParameterBoundsAs(second.signature()) 
			              && ((Type)first.nearestAncestor(Type.class)).subTypeOf((Type)second.nearestAncestor(Type.class));
			return result;
		}
	};

}


