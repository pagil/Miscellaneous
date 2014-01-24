package com.hash.table;

public class HashTableClient {

	public static void main(String[] args) {
		HashTableImpl hashTable = new HashTableImpl();
		hashTable.put(5, "five");
		hashTable.put(5, "six");
		System.out.println(hashTable.get(5));
	}

}
