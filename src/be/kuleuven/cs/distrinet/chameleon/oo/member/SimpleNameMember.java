package be.kuleuven.cs.distrinet.chameleon.oo.member;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

public abstract class SimpleNameMember extends MemberImpl {

  private String _name;
  
  public String name() {
  	return _name;
  }

	private SimpleNameSignature _signature;
	
	public void setName(String name) {
		_name = name;
		if(_signature != null) {
			_signature.setName(name);
		}
	}
	
	public void setSignature(Signature signature) {
		if(signature instanceof SimpleNameSignature) {
			setName(signature.name());
		} else {
			throw new ChameleonProgrammerException();
		}
	}
	
	public SimpleNameSignature signature() {
		if(_signature == null) {
			synchronized (this) {
				if(_signature == null) {
					_signature = new SimpleNameSignature(_name) {
						@Override
						public void setName(String name) {
							super.setName(name);
							SimpleNameMember.this._name = name;
						}
					};
				}
				_signature.setUniParent(this);
			}
		}
		return _signature;
	}
	
	@Override
	public boolean sameSignatureAs(Declaration declaration) throws LookupException {
		if(declaration instanceof SimpleNameMember) {
			return _name.equals(((SimpleNameMember)declaration)._name);
		} else {
			return declaration.name().equals(_name) && declaration.signature() instanceof SimpleNameSignature;
		}
	}
	
}
