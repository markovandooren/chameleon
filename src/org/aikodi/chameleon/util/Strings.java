package org.aikodi.chameleon.util;

import java.util.Collection;
import java.util.Iterator;

public class Strings {

	public static String create(Collection<?> collection) {
		return make(collection);
	}
	
	private static String make(Object object) {
		if(object instanceof Collection) {
			return doCreate((Collection<?>) object);
		} else if(object != null){
			return object.toString();
		} else {
			return "null";
		}
	}
	
	private static String doCreate(Collection<?> collection) {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		Iterator<?> iterator = collection.iterator();
		while(iterator.hasNext()) {
			builder.append(make(iterator.next()));
			if(iterator.hasNext()) {
				builder.append(", ");
			}
		}
		builder.append(']');
		return builder.toString();
	}

}
