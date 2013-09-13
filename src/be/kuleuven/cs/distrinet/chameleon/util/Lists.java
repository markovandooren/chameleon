package be.kuleuven.cs.distrinet.chameleon.util;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class Lists {

	public static <T> ImmutableList<T> union(List<T> first, List<T> second) {
		Builder<T> builder = ImmutableList.builder();
		builder.addAll(first);
		builder.addAll(second);
		return builder.build();
	}
	
	public static <T> ImmutableList<T> union(List<T> first, T second) {
		Builder<T> builder = ImmutableList.builder();
		builder.addAll(first);
		builder.add(second);
		return builder.build();
	}

}
