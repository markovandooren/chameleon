package be.kuleuven.cs.distrinet.chameleon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	
//	public final static Timer LIST_CREATION = new Timer();
	
	public static <T> List<T> create() {
//		LIST_CREATION.start();
		ArrayList<T> result = new ArrayList<T>();
//		LIST_CREATION.stop();
		return result;
	}

	public static <T> List<T> create(int size) {
//		LIST_CREATION.start();
		ArrayList<T> result = new ArrayList<T>(size);
//		LIST_CREATION.stop();
		return result;
	}
	
	public static <T> List<T> create(T t) {
		//	LIST_CREATION.start();
		ArrayList<T> result = new ArrayList<T>();
		result.add(t);
		//	LIST_CREATION.stop();
		return result;
	}

	public static <T> List<T> create(T t, int size) {
		//	LIST_CREATION.start();
		ArrayList<T> result = new ArrayList<T>(size);
		result.add(t);
		//	LIST_CREATION.stop();
		return result;
	}
	
	public static <T> List<T> create(Collection<T> list) {
//		LIST_CREATION.start();
		ArrayList<T> result = new ArrayList<T>(list);
//		LIST_CREATION.stop();
		return result;
	}

}
