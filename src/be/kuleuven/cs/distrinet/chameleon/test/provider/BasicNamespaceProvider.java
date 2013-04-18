package be.kuleuven.cs.distrinet.chameleon.test.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

	/**
	 * A class for providing namespaces based on their fully qualified names. Both null and the empty string
	 * are treated as the names of the root namespace.
	 * 
	 * @author Marko van Dooren
	 */
	public class BasicNamespaceProvider implements ElementProvider<Namespace> {
		
	 /*@
		 @ public behavior
		 @
		 @ post namespaces().isEmpty(); 
		 @*/
		public BasicNamespaceProvider() {
			
		}
		
	 /*@
		 @ public behavior
		 @
		 @ pre fqn != null;
		 @
		 @ post namespaces().contains(fqn); 
		 @*/
		public BasicNamespaceProvider(String fqn) {
			addNamespace(fqn);
		}
		
		 /*@
		 @ public behavior
		 @
		 @ pre fqns != null;
		 @
		 @ post namespaces().containsAll(fqn); 
		 @*/
		public BasicNamespaceProvider(Collection<String> fqns) {
			addAllNamespaces(fqns);
		}

	 /*@
		 @ public behavior
		 @
		 @ pre fqn != null;
		 @
		 @ post namespaces().contains(fqn); 
		 @*/
		public void addNamespace(String fqn) {
			_namespaces.add(fqn);
		}

		 /*@
		 @ public behavior
		 @
		 @ pre fqns != null;
		 @
		 @ post namespaces().containsAll(fqns); 
		 @*/
		public void addAllNamespaces(Collection<String> fqns) {
			_namespaces.addAll(fqns);
		}

		private List<String> _namespaces = new ArrayList<String>();
		
		public Collection<Namespace> elements(View view) {
			Collection<Namespace> result = new ArrayList<Namespace>();
			try {
				for(String fqn: _namespaces) {
				  Util.addNonNull(namespace(fqn, view),result);
				}
			} catch (LookupException e) {
				e.printStackTrace();
			}
			return result;
		}
		
		public Namespace namespace(String fqn, View view) throws LookupException {
			Namespace ns = view.namespace();
			String head = Util.getFirstPart(fqn);
			String tail = Util.getPartAfterFirstDot(fqn);
			while (ns != null && head != null && ! head.equals("")) {
				ns = ns.getSubNamespace(head);
				head = Util.getFirstPart(tail);
				tail = Util.getPartAfterFirstDot(tail);
			}
			return ns;
		}
		
	}
