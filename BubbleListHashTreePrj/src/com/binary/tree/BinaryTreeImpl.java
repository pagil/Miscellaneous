package com.binary.tree;

public class BinaryTreeImpl<T extends Comparable<T>> {
	private Node<T> root;

	/**
	 * Insert new value. Duplicate values are not allowed.
	 * 
	 * @param value
	 * @return false if node with this value already exists.
	 */
	public boolean insert(T value) {
		if (value != null) {
			Node<T> newNode = new Node<T>(value);
			if (root == null) {
				root = newNode;
				return true;
			} else {
				return insert(root, newNode);
			}
		}
		return false;

	}

	private boolean insert(Node<T> currentNode, Node<T> newNode) {
		int comparison = newNode.value.compareTo(currentNode.value);
		if (comparison < 0) {
			if (currentNode.left == null) {
				currentNode.left = newNode;
				currentNode.left.parent = currentNode;
				return true;
			} else {
				return insert(currentNode.left, newNode);
			}
		} else if (comparison == 0) {
			// equal nodes are forbidden, duplicates are not allowed
			return false;
		} else {
			if (currentNode.right == null) {
				currentNode.right = newNode;
				currentNode.right.parent = currentNode;
				return true;
			} else {
				return insert(currentNode.right, newNode);
			}
		}
	}

	public boolean delete(T value) {
		if (value != null) {
			return delete(root, value);
		}
		return false;
	}

	private boolean delete(Node<T> currentNode, T value) {
		int comparison = value.compareTo(currentNode.value);
		if (comparison < 0) {
			return delete(currentNode.left, value);
		} else if (comparison == 0) {
			// found
			if (currentNode.left != null) {
				// find right most leaf in the left subtree and set its as value
				// to this node and then delete the leaf
				Node<T> rightLeaf = findRightLeaf(currentNode.left);
				currentNode.value = rightLeaf.value;
				rightLeaf.value = null;
				rightLeaf.parent.right = null;
				return true;
			} else if (currentNode.right != null) {
				// find left most leaf in the right subtree and set its value to
				// this node and then delete the leaf
				Node<T> leftLeaf = findLeftLeaf(currentNode.right);
				currentNode.value = leftLeaf.value;
				leftLeaf.value = null;
				leftLeaf.parent.left = null;
				return true;
			} else {
				// leaf
				if (currentNode.parent == null) {
					// handle root of the tree
					root = null;
				} else {
					// null left or right of the parent node
					Node<T> parent = currentNode.parent;
					if (parent.left == currentNode) {
						parent.left = null;
					} else {
						parent.right = null;
					}
				}
			}
		} else {
			return delete(currentNode.right, value);
		}
		return false;
	}

	private Node<T> findLeftLeaf(Node<T> currentNode) {
		if (currentNode.left != null) {
			return findLeftLeaf(currentNode.left);
		} else if (currentNode.right != null) {
			return findLeftLeaf(currentNode.right);
		} else {
			// this is a leaf - found
			return currentNode;
		}
	}

	private Node<T> findRightLeaf(Node<T> currentNode) {
		if (currentNode.right != null) {
			return findRightLeaf(currentNode.right);
		} else if (currentNode.left != null) {
			return findRightLeaf(currentNode.left);
		} else {
			// this is a leaf - found
			return currentNode;
		}
	}

	public T search(T value) {
		if (value != null) {
			return search(root, value);
		}
		return null;
	}

	private T search(Node<T> currentNode, T value) {
		int comparison = value.compareTo(currentNode.value);
		if (comparison < 0) {
			return search(currentNode.left, value);
		} else if (comparison == 0) {
			return currentNode.value;
		} else {
			return search(currentNode.right, value);
		}
	}

	public void prefixTraverse() {
		prefixTraverse(root);
	}

	private void prefixTraverse(Node<T> currentNode) {
		if (currentNode != null) {
			System.out.print(currentNode.value + " ");
			prefixTraverse(currentNode.left);
			prefixTraverse(currentNode.right);
		}
	}

	public void infixTraverse() {
		infixTraverse(root);
	}

	private void infixTraverse(Node<T> currentNode) {
		if (currentNode != null) {
			infixTraverse(currentNode.left);
			System.out.print(currentNode.value + " ");
			infixTraverse(currentNode.right);
		}
	}

	public void postfixTraverse() {
		postfixTraverse(root);
	}

	private void postfixTraverse(Node<T> currentNode) {
		if (currentNode != null) {
			postfixTraverse(currentNode.left);
			postfixTraverse(currentNode.right);
			System.out.print(currentNode.value + " ");
		}
	}

	public class Node<ValueClass> {
		private ValueClass value;
		private Node<ValueClass> left;
		private Node<ValueClass> right;
		private Node<ValueClass> parent;

		public Node(ValueClass value) {
			super();
			this.value = value;
		}

		public ValueClass getValue() {
			return value;
		}

		public void setValue(ValueClass value) {
			this.value = value;
		}

		public Node<ValueClass> getLeft() {
			return left;
		}

		public void setLeft(Node<ValueClass> left) {
			this.left = left;
		}

		public Node<ValueClass> getRight() {
			return right;
		}

		public void setRight(Node<ValueClass> right) {
			this.right = right;
		}
	}

}
