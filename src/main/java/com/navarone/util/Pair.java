package com.navarone.util;

/**
 * @author Craig<br/>
 *         A utility class where K has no mathematical relation to V, except
 *         that K maps to V
 * 
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> {

	private K key;

	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
