package be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public class NamingRegistry<T extends Element> {
	public NamingRegistry() {
		
	}
	
	private Map<T, String> names = new HashMap<T, String>();
	
	public String getName(T element) {
		String name = getExisting(element);
		
		if (name == null) {
			do {
				name = getRandomName();
			} while (names.values().contains(name));
			
			T elementClone = (T) element.clone();
			elementClone.setOrigin(element);
			
			names.put(elementClone, name);
		}
		
		return name;
	}
	
	private String getExisting(T element) {
		T toGet = element;

		for (T m : names.keySet()) {
			try {
				if (checkEquality((T) m.origin(), element)) {
					toGet = m;
					break;
				}
			} catch (LookupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		return names.get(toGet);
	}
	
	protected boolean checkEquality(T first, T second) throws LookupException {
		return first.sameAs(second);
	}

	private Random r = new Random();
	private String alphabet = "abcdefghijklmopqrstuvwxyz";
	private String getRandomName() {
		StringBuilder name = new StringBuilder();
		for (int i = 0; i < 8; i++)
			name.append(alphabet.charAt(r.nextInt(alphabet.length())));
		
		return name.toString();
	}
}
