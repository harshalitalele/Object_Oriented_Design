package edu.buffalo;

import java.util.Stack;

import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;
import com.google.java.contract.Ensures;
class BST_Contract {

	public static void main(String args[]) {
		test1();
		test2();
	}

	public static void test1() {
		Tree tr;
		tr = new Tree(100);
		tr.insert(50);
		tr.insert(20);
		tr.insert(150);
		tr.insert(120);
		tr.insert(200);
		tr.insert(50);
		tr.insert(20);
		tr.insert(150);
		tr.insert(120);
		tr.insert(200);
		tr.insert(50);
		tr.insert(20);
		tr.insert(150);

		BST_Iterator it = new BST_Iterator(tr);

		System.out.print("Tree: ");
		while (it.hasNext()) {
			int v = it.next();
			System.out.print(v + " ");
		}
	}

	public static void test2() {
		DupTree tr;
		tr = new DupTree(100);
		tr.insert(50);
		tr.insert(20);
		tr.insert(150);
		tr.insert(120);
		tr.insert(200);
		tr.insert(50);
		tr.insert(20);
		tr.insert(150);
		tr.insert(120);
		tr.insert(200);
		tr.insert(50);
		tr.insert(20);
		tr.insert(150);

		BST_Iterator it = new BST_Iterator(tr);

		System.out.print("\nDupTree: ");
		while (it.hasNext()) {
			int v = it.next();
			System.out.print(v + " ");
		}
	}
}

//*************** Class Tree  *************

@Invariant({ "ordered()" })

class Tree { // Defines one node of a binary search tree

	public Tree(int n) {
		value = n;
		left = null;
		right = null;
	}

	int min() {
		if (left == null)
			return value;
		else
			return left.min();
	}

	int max() {
		if (right == null)
			return value;
		else
			return right.max();
	}

	public void insert(int n) {
		if (value == n)
			return;
		if (value < n)
			if (right == null)
				right = new Tree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new Tree(n);
		else
			left.insert(n);
	}

	public int get_count() {
		return 1;
	}

	public boolean ordered() {
		return (left == null || (value > left.max() && left.ordered()))
				&& 
			   (right == null || (value < right.min() && right.ordered()));
	}

	protected int value;
	protected Tree left;
	protected Tree right;
}

//Class DupTree

class DupTree extends Tree {

	public DupTree(int n) {
		super(n); // superclass initialization
		count = 1;
	};

	public void insert(int n) {
		if (value == n) {
			count++;
			return;
		}
		if (value < n)
			if (right == null)
				right = new DupTree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new DupTree(n);
		else
			left.insert(n);
	}

	public int get_count() {
		return count;
	}

	protected int count;
}

class BST_Iterator {
	
	@Requires ({"root.ordered()"}) //Correct
	@Ensures ({"hasNext()", "root.min() == stack.peek().value"}) //
	public BST_Iterator(Tree root) {
		stack_left_spine(root);
	}
	
	public boolean hasNext() {
		return !stack.isEmpty() || count > 0;
	}
	
	@Requires ({"hasNext()"})
	@Ensures ({"value >= old(value)", "count >= 0"}) //Ascending order
	public int next() {
		if (count == 0) {
			Tree node = stack.pop();
			value = node.value;
			count = node.get_count();
			stack_left_spine(node.right);
		}
		count--;
		//System.out.println(value);
		return value;
	}
	
	@Ensures({"stack.isEmpty() || value <= stack.peek().value", "node == null || stack.peek().value == old(node).min()"})
	private void stack_left_spine(Tree node) {
		if (node == null)
			return;
		stack.push(node);
		while (node.left != null) {
			stack.push(node.left);
			node = node.left;
		}
		//System.out.println("stack.peek().value"+stack.peek().value);
	}

	private Stack<Tree> stack = new Stack<Tree>();
	private int value;
	private int count = 0;
}
