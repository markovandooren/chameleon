package chameleon.core.validation;

public class Valid extends VerificationResult {
	
	private Valid() {
		
	}
	
	public static Valid create() {
		return _instance;
	}
	
	private static Valid _instance = new Valid();
	
	public String toString() {
		return "valid";
	}

	@Override
	public VerificationResult and(VerificationResult other) {
		return other;
	}

	@Override
	protected VerificationResult andInvalid(Invalid problem) {
		return problem;
	}

}
