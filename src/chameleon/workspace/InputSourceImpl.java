package chameleon.workspace;

import org.rejuse.association.SingleAssociation;

import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;

public abstract class InputSourceImpl implements InputSource {
	
	public InputSourceImpl() {
		
	}
	
	protected void setNamespace(InputSourceNamespace ns) {
		if(ns != null) {
			if(_namespace == null) {
				_namespace =  new SingleAssociation<InputSourceImpl, InputSourceNamespace>(this);
			}
			ns.addInputSource(this);
			_uniNamespace = null;
		}
	}
	
	public abstract InputSourceImpl clone();
	
	
	
	public Namespace namespace() {
		if(_namespace != null) {
			return _namespace.getOtherEnd();
		} else {
			return _uniNamespace;
		}
	}
	
	protected void setUniNamespace(Namespace ns) {
		if(_namespace != null) {
			_namespace.connectTo(null);
		}
		if(ns != null) {
			_namespace = null;
		} else if(_namespace == null) {
			_namespace = new SingleAssociation<InputSourceImpl, InputSourceNamespace>(this);
		}
		_uniNamespace = ns;
	}
	
	private Namespace _uniNamespace;
	
	public SingleAssociation<InputSourceImpl, InputSourceNamespace> namespaceLink() {
		return _namespace;
	}
	
	protected SingleAssociation<InputSourceImpl, InputSourceNamespace> _namespace;


}
