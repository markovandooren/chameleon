package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.association.Association;

/**
 * A class of composite qualified names.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 */
public class CompositeQualifiedName extends QualifiedName {

	public List<Signature> signatures() {
		return _signatures.getOtherEnds();
	}
	
	public void append(Signature signature) {
		add(_signatures, signature);
	}
	
	public void appendAll(List<Signature> signatures) {
		for(Signature signature: signatures) {
			append(signature);
		}
	}
	
	public void prefix(Signature signature) {
		if(signature != null) {
			_signatures.addInFront((Association)signature.parentLink());
		}
	}
	
	public void remove(Signature signature) {
		remove(_signatures,signature);
	}
	
	private Multi<Signature> _signatures = new Multi<Signature>(this);

	public Signature lastSignature() {
		return _signatures.lastElement();
	}
	
	@Override
	protected CompositeQualifiedName cloneSelf() {
		return new CompositeQualifiedName();
	}
	
	public int length() {
		return _signatures.size();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();//new BasicProblem(this, "TODO: implement verifySelf of FullyQualifiedName");
	}

	public QualifiedName popped() {
		CompositeQualifiedName result = clone(this);
		result.remove(result.lastSignature());
		return result;
	}

	@Override
	public Signature signatureAt(int index) {
		return _signatures.elementAt(index);
	} 

	/**
	 * For debugging purposes because Eclipse detail formatters simply don't work.
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		List<Signature> signatures = signatures();
		int size = signatures.size();
		for(int i=0; i< size; i++) {
			result.append(signatures.get(i));
			if(i < size - 1) {
				result.append('.');
			}
		}
		return result.toString();
	}

	
}
