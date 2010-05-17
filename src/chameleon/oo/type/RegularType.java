package chameleon.oo.type;



import chameleon.core.declaration.SimpleNameSignature;
import chameleon.util.CreationStackTrace;

public class RegularType extends TypeWithBody {

	public RegularType(SimpleNameSignature sig) {
		super(sig);
	}
	
	public RegularType(String name) {
		this(new SimpleNameSignature(name));
	}
	
	@Override
	public RegularType clone() {
		RegularType result = cloneThis();
		result.copyContents(this);
		return result;
	}

	protected RegularType cloneThis() {
		return new RegularType(signature().clone());
	}

	@Override
	public Type baseType() {
		return this;
	}

}
