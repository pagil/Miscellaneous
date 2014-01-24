package com.binary.tree;

public class BinaryTreeClient {

	public static void main(String[] args) {
		BinaryTreeImpl<Integer> tree = new BinaryTreeImpl<Integer>();
		tree.insert(8);
		tree.insert(3);
		tree.insert(11);
		tree.insert(1);
		tree.insert(5);
		tree.insert(9);
		tree.insert(14);
		tree.insert(6);
		tree.insert(10);
		tree.insert(12);
		tree.insert(15);
		tree.insert(7);
		tree.insert(13);
		
		tree.prefixTraverse();
		System.out.println();
		tree.infixTraverse(); // left-right
		System.out.println();
		tree.postfixTraverse();
		System.out.println("\n Exit.");
	}

}
