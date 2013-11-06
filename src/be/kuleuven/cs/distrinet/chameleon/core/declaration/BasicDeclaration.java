package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

public abstract class BasicDeclaration extends DeclarationImpl implements SimpleNameDeclaration {

	public BasicDeclaration(String name) {
		setName(name);
	}
	
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
							BasicDeclaration.this._name = name;
						}
					};
				}
				_signature.setUniParent(this);
			}
		}
		return _signature;
	}
	
	@Override
	public boolean sameSignatureAs(Declaration declaration) {
		if(declaration instanceof BasicDeclaration) {
			return _name.equals(((BasicDeclaration)declaration)._name);
		} else {
			return declaration.name().equals(_name) && declaration instanceof SimpleNameDeclaration;
		}
	}

}
