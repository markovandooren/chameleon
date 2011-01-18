/**
 * AODA - Aspect Oriented Debugging Architecture
 * Copyright (C) 2007-2009 Wouter De Borger
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package chameleon.util;


import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is used to cache values.
 * It uses soft references to store cached values. Once a value is garbage collected by the VM,
 * the corresponding entry is removed from the cache on the next invocation of put() or get().
 *
 * Note that WeakHashMap can't be used for this purpose because in WeakHashMap
 * soft references are only used for the keys, and values may not have 'strong' references
 * to keys otherwise they will never be garbage collected.
 *
 */
public class ValueCache<K,V> {
	
	public ValueCache() {
		
	}
	
	protected static class Entry<K, V> implements Map.Entry<K, V> {

		public Entry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		private V value;
		private K key;

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Map to store <key, Reference> pairs,
	 * where Reference is a soft reference to an Object.
	 */
	private Map<K,SoftReference<V>> cacheTable = new Hashtable<K,SoftReference<V>>();
	/**
	 * Map to store <Reference, key> pairs,
	 * to find the cacheTable-key of a garbage collected Reference.
	 */
	private Map<SoftReference<V>,K> refTable = new Hashtable<SoftReference<V>,K>();
	
	/**
	 * The reference-queue that is registered with the soft references.
	 * The garbage collector will enqueue soft references that are garbage collected.
	 */
	private ReferenceQueue<V> refQueue = new ReferenceQueue<V>();
	
	/**
	 * Clean up all entries from the table for which the values were garbage collected.
	 */
	private void cleanup() {
		Reference<V> ref;
		while ((ref = (Reference<V>) refQueue.poll()) != null) {
			Object key = refTable.get(ref);
			if (key != null)
				cacheTable.remove(key);
			refTable.remove(ref);
		}
	}

	/**
	 * Put a new entry in the cache under the given key.
	 */
	public void put(K key, V value) {
		cleanup();
		SoftReference<V> ref = new SoftReference<V>(value, refQueue);
		cacheTable.put(key, ref);
		refTable.put(ref, key);
	}

	/**
	 * Get entry from the cache.
	 * @return Returns value that is cached under the given key,
	 * or null of one of the following is true:
	 *  - The value has not been cached.
	 *  - The value had been cached but is garbage collected.
	 */
	public V get(Object key) {
		cleanup();
		V value = null;
		SoftReference<V> ref = (SoftReference<V>)cacheTable.get(key);
		if (ref != null) {
			value = ref.get();
		}
		return value;
	}
	
	/**
	 * Returns a Collection view of the values contained in this cache.
	 */
	public Collection<V> values() {
		cleanup();
		List<V> returnValues = new ArrayList<V>();
		synchronized (cacheTable) {
			Iterator<SoftReference<V>> iter = cacheTable.values().iterator();
			SoftReference<V> ref;
			V value;
			while (iter.hasNext()) {
				ref = (SoftReference<V>)iter.next();
				value = ref.get();
				if (value != null) {
					returnValues.add(value);
				}
			}
		}
		return returnValues;
	}
	
	/**
	 * Returns a Collection view of the values contained in this cache.
	 */
	public Collection<K> keys() {
		cleanup();
		List<K> returnValues = new ArrayList<K>();
		synchronized (cacheTable) {
			Iterator<Map.Entry<K,SoftReference<V>>> iter = cacheTable.entrySet().iterator();
			Map.Entry<K,SoftReference<V>> ref;
			V value;
			K key;
			while (iter.hasNext()) {
				ref = iter.next();
				value = ref.getValue().get();
				key = ref.getKey();
				if (value != null) {
					returnValues.add(key);
				}
			}
		}
		return returnValues;
	}
	
	/**
	 * Returns a Collection view of the values contained in this cache.
	 */
	public Collection<Map.Entry<K, V>> entrySet() {
		cleanup();
		List<Map.Entry<K, V>> returnValues = new ArrayList<Map.Entry<K, V>>();
		synchronized (cacheTable) {
			Iterator<Map.Entry<K,SoftReference<V>>> iter = cacheTable.entrySet().iterator();
			Map.Entry<K,SoftReference<V>> ref;
			V value;
			K key;
			while (iter.hasNext()) {
				ref = iter.next();
				value = ref.getValue().get();
				key = ref.getKey();
				if (value != null) {
					returnValues.add(new Entry<K, V>(key,value));
				}
			}
		}
		return returnValues;
	}
	
//	/**
//	 * Returns a Collection view of the values contained in this cache that have the same
//	 * runtime class as the given Class.
//	 */
//	public Collection valuesWithType(Class type) {
//		cleanup();
//		List returnValues = new ArrayList();
//		synchronized (cacheTable) {
//			Iterator iter = cacheTable.values().iterator();
//			SoftReference ref;
//			Object value;
//			while (iter.hasNext()) {
//				ref = (SoftReference)iter.next();
//				value = ref.get();
//				if (value != null && value.getClass().equals(type)) {
//					returnValues.add(value);
//				}
//			}
//		}
//		return returnValues;
//	}

	/** 
	 * Removes the key and its corresponding value from this cache.
	 * @return Returns The value to which the key had been mapped in this hashtable,
	 * or null if the key did not have a mapping.
	 */
	public V remove(K key) {
		cleanup();
		V value = null;
		SoftReference<V> ref = cacheTable.get(key);
		if (ref != null) {
			value = ref.get();
			refTable.remove(ref);
		}
		cacheTable.remove(key);
		return value;
	}
}
