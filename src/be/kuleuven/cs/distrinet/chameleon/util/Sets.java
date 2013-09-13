package be.kuleuven.cs.distrinet.chameleon.util;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

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
