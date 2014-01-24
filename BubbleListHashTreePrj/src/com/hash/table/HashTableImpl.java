package com.hash.table;

import com.linked.list.LinkedListImpl;
import com.linked.list.LinkedListImpl.Node;

public class HashTableImpl {
	private float loadFactor = 0.75f;
	private int initialSize = 10;
	private LinkedListImpl<Element>[] buckets;
	
	public HashTableImpl(){
		this(null, null);
	}

	public HashTableImpl(Float loadFactor){
		this(loadFactor, null);
	}
	
	public HashTableImpl(Integer initialSize) {
		this(null, initialSize);
	}

	@SuppressWarnings("unchecked")
	public HashTableImpl(Float loadFactor, Integer initialSize){
		this.loadFactor = loadFactor != null ? loadFactor : this.loadFactor;
		this.initialSize = initialSize != null ? initialSize : this.initialSize;
		buckets = new LinkedListImpl[this.initialSize];
	}

	public boolean put(int key, String value) {
		Element element = new Element(key, value);
		return put(element);
	}
		
	public boolean put(Element element) {
		int index = getBucketIndex(element);
		LinkedListImpl<Element> list = buckets[index];
		if (list == null) {
			// create new list and add new Element
			buckets[index] = new LinkedListImpl<Element>();
			buckets[index].add(element);
			return true;
		} else {
			// search uses equals() of Element which takes into account key only
			if (list.contains(element)) {
				// Element with such key already exists
				return false;
			} else {
				buckets[index].add(element);
				return true;
			}
		}
	}
	
	public String get(int key) {
		Element element = new Element(key, null);
		int index = getBucketIndex(element);
		LinkedListImpl<Element> list = buckets[index];
		element = list != null ? list.get(element) : null;
		return element != null ? element.value : null;
	}

	public String remove(int key) {
		Element element = new Element(key, null);
		int index = getBucketIndex(element);
		LinkedListImpl<Element> list = buckets[index];
		element = list != null ? list.remove(element) : null;
		return element != null ? element.value : null;
	}
	
	public boolean contains(int key) {
		Element element = new Element(key, null);
		int index = getBucketIndex(element);
		LinkedListImpl<Element> list = buckets[index];
		return list != null ? list.contains(element) : false;
	}
	
	@SuppressWarnings("unchecked")
	protected void rehash() {
		LinkedListImpl<Element>[] tmpBuckets = buckets;
		buckets = new LinkedListImpl[buckets.length*2];
		for (int i = 0; i < tmpBuckets.length; i++) {
			LinkedListImpl<Element> list = tmpBuckets[i];
			if (list != null) {
				Node current = list.getHead();
				while (current != null) {
					Element element = (Element)current.getValue();
					put(element);
					current = current.getNext();
				}
			}
		}
	}
	
	private int getBucketIndex(Element element) {
		int hashCode = element.hashCode();
		int index = hashCode % buckets.length;// index of the bucket found	
		return index;
	}

	private int getBucketIndex(int key) {
		Element element = new Element(key, null);
		int hashCode = element.hashCode();
		int index = hashCode % buckets.length;// index of the bucket found	
		return index;
	}
	
	class Element {
		private int key;
		private String value;
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public Element(int key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;

			result = prime * result + key;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Element other = (Element) obj;
			if (key != other.key)
				return false;
			return true;
		}

		
		
	}
}
