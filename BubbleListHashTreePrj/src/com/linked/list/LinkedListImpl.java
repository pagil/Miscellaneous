package com.linked.list;

public class LinkedListImpl<T> {
	private Node head;
	private Node tail;
	
	public LinkedListImpl() {
	}
	
	public void add(T value) {
		Node node = new Node(value);
		tail = node;
		if (head == null) {
			// initialize head when the first node is added
			head = node;
		}
		if (tail != null) {
			// do this step if it is not the forst node in the list
			tail.next = node;
		}
	}
	
	public Node getHead() {
		return head;
	}

	public T get(T value) {
		Node current = head;
		while (current != null) {
			if (value != null && value.equals(current.value)) {
				return current.value;
			}
			current = current.next;
		}
		return null;
	}
	
	public T remove(T value) {
		Node current = head;
		Node previous = null;
		while(current != null) {
			if ((value == null && current.value == null) || (value != null && value.equals(current.value))) {
				// linked list allows nulls - handle this case here
				if (previous != null) {
					previous.next = current.next;
					current.next = null;
					return current.value;
				} else {
					head = current.next;
					current.next = null;
					return current.value;
				}
			}
			previous = current;
			current = current.next;
		}
		return null;
	}
	
	public boolean contains(T value) {
		Node node = head;
		while (node != null) {
			if (value == null && node.value == null) {
				// linked list allows nulls - handle this case here
				return true;
			} else if (value != null && value.equals(node.value)) {
				return true;
			}
			node = node.next;
		}
		return false;
	}
	
	
	public class Node {
		private T value;
		private Node next;

		public Node(T value) {
			super();
			this.value = value;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

	}

}
