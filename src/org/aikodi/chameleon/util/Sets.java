package org.aikodi.chameleon.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Set;

public class Sets {

	public static <T> ImmutableSet<T> union(Set<T> first, Set<T> second) {
		Builder<T> builder = ImmutableSet.builder();
		builder.addAll(first);
		builder.addAll(second);
		return builder.build();
	}
	
	public static <T> ImmutableSet<T> union(Set<T> first, T second) {
		Builder<T> builder = ImmutableSet.builder();
		builder.addAll(first);
		builder.add(second);
		return builder.build();
	}
}
