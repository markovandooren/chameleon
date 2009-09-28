package chameleon.core.validation;

public class Valid extends VerificationResult {
	
	public String toString() {
		return "valid";
	}

	@Override
	public VerificationResult and(VerificationResult other) {
		return other;
	}

}
