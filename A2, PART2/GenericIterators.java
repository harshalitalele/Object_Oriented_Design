import java.util.*;

// ============  THE MAIN METHOD WITH TWO TESTS.  ==============
// ============  DON'T MODIFY THE TEST METHODS   ===============
// ============  COMPLETE ONLY THE containedIn METHOD ==========

public class GenericIterators {

	public static void main(String[] args) {

		test1();
		System.out.println();
		test2();
	}

	static void test1() {

		AbsTree<Integer> set1 = new Tree<Integer>(100);
		set1.insert(50);
		set1.insert(50);
		set1.insert(25);
		set1.insert(75);
		set1.insert(75);
		set1.insert(150);
		set1.insert(125);
		set1.insert(200);
		set1.insert(100);

		AbsTree<Integer> set2 = new Tree<Integer>(100);
		set2.insert(150);
		set2.insert(125);
		set2.insert(50);
		set2.insert(50);
		set2.insert(26);
		set2.insert(25);
		set2.insert(27);
		set2.insert(75);
		set2.insert(75);
		set2.insert(76);
		set2.insert(150);
		set2.insert(125);
		set2.insert(200);

		System.out.print("set1 = ");
		print(set1);
		System.out.print("set2 = ");
		print(set2);

		if (containedIn(set1, set2))
			System.out.println("set1 is contained in set2.");
		else
			System.out.println("set1 is not contained in set2.");
	}

	static void test2() {

		AbsTree<Integer> bag1 = new DupTree<Integer>(100);
		bag1.insert(50);
		bag1.insert(50);
		bag1.insert(25);
		bag1.insert(75);
		bag1.insert(75);
		bag1.insert(150);
		bag1.insert(125);
		bag1.insert(200);
		bag1.insert(100);

		AbsTree<Integer> bag2 = new DupTree<Integer>(100);
		bag2.insert(150);
		bag2.insert(125);
		bag2.insert(50);
		bag2.insert(50);
		bag2.insert(26);
		bag2.insert(25);
		bag2.insert(27);
		bag2.insert(75);
		bag2.insert(75);
		bag2.insert(76);
		bag2.insert(150);
		bag2.insert(125);
		bag2.insert(200);

		System.out.print("bag1 = ");
		print(bag1);
		System.out.print("bag2 = ");
		print(bag2);

		if (containedIn(bag1, bag2))
			System.out.println("bag1 is contained in bag2.");
		else
			System.out.println("bag1 is not contained in bag2.");
	}

	static int inc, set1_bag1_count; //set1_bag1_count will count the number of items in set1/bag1

	static void print(AbsTree<Integer> bs) {
		set1_bag1_count = inc;
		inc = 0;
		System.out.print("{ ");
		for (int x : bs) {
			System.out.print(x + " ");
			inc++;
		}
		System.out.println("}");
	}

	static int equals_count; //equals_count will count the number of times two elements in a set/bag were equal

	static <T extends Comparable<T>> boolean containedIn(AbsTree<T> tr1, AbsTree<T> tr2) {
		Iterator<T> iter1 = tr1.iterator();
		Iterator<T> iter2 = tr2.iterator();
		T val1, val2;
		equals_count = 0;
		while (iter1.hasNext() && iter2.hasNext()) {
			val1 = iter1.next();
			val2 = iter2.next();
		
			if (val1.equals(val2)) {
				System.out.println(val1 + " = " + val2);
				equals_count++;
				continue;
			}
			if (val1.compareTo(val2) < 0) {
				System.out.println(val1 + " < " + val2);
				return false;
			}
			while (val1.compareTo(val2) > 0) {
				System.out.println(val1 + " > " + val2);
				val2 = iter2.next();
				if (val1.equals(val2)) {
					System.out.println(val1 + " = " + val2);
					equals_count++;
					break;
				}
			}
		}
		if (set1_bag1_count == equals_count)
			return true;
		else
			return false;
	}
}

//========= GENERIC ABSTREE, TREE, AND DUPTREE (DON'T MODIFY THESE CLASSES)

abstract class AbsTree<T extends Comparable<T>> implements Iterable<T> {

	public AbsTree(T v) {
		value = v;
		left = null;
		right = null;
	}

	public void insert(T v) {
		if (value.compareTo(v) == 0)
			count_duplicates();
		if (value.compareTo(v) > 0)
			if (left == null)
				left = add_node(v);
			else
				left.insert(v);
		else if (value.compareTo(v) < 0)
			if (right == null)
				right = add_node(v);
			else
				right.insert(v);
	}

	public Iterator<T> iterator() {
		return create_iterator();
	}

	protected abstract AbsTree<T> add_node(T n);

	protected abstract void count_duplicates();

	protected abstract int get_count();

	protected abstract Iterator<T> create_iterator();

	protected T value;
	protected AbsTree<T> left;
	protected AbsTree<T> right;
}

class Tree<T extends Comparable<T>> extends AbsTree<T> {
	public Tree(T n) {
		super(n);
		count = 1;
	}

	public Iterator<T> create_iterator() {
		return new AbsTreeIterator<T>(this);
	}

	protected AbsTree<T> add_node(T n) {
		return new Tree<T>(n);
	}

	protected void count_duplicates() {
		;

	}

	protected int get_count() {
		return 1;
	}

	protected int count;
}

class DupTree<T extends Comparable<T>> extends AbsTree<T> {
	public DupTree(T n) {
		super(n);
		count = 1;
	};

	public Iterator<T> create_iterator() {
		return new AbsTreeIterator<T>(this); // to do
	}

	protected AbsTree<T> add_node(T n) {
		return new DupTree<T>(n);
	}

	protected void count_duplicates() {
		count++;
	}

	protected int get_count() {
		return count;
	}

	protected int count;
}

// ========  GENERIC TREE ITERATORS (COMPLETE THE OUTLINES) =========

class AbsTreeIterator<T extends Comparable<T>> implements Iterator<T> {

	public AbsTreeIterator(AbsTree<T> root) {
		
		stack_left_spine(root);
	}

	public boolean hasNext() {
		
		return !stack.empty();
	}

	public T next() {
		
		if (countdup == 0) {
			node = stack.pop();
			countdup = node.get_count(); //countdup is a duplicate variable for 'count' of a node, it serves the purpose of iterating through next() method

		}else if (countdup > 0) { //this part will run when there are duplicate elements in a bag, eg, 50, 50
			countdup = countdup - 1;
			return node.value;
		}
		stack_left_spine(node.right);
		countdup = countdup - 1;
		return node.value;
	}

	private void stack_left_spine(AbsTree<T> node) {

		// fill in code here
		while (node != null) {
			stack.push(node);
			node = node.left;
		}
	}

	private Stack<AbsTree<T>> stack = new Stack<AbsTree<T>>();
	private int countdup = 0;
	AbsTree<T> node = null;
}

class TreeIterator<T extends Comparable<T>> extends AbsTreeIterator<T> {

	// fill in code here
	public TreeIterator(AbsTree<T> n) {
		super(n);
	}
}

class DupTreeIterator<T extends Comparable<T>> extends AbsTreeIterator<T> {

	// fill in code here
	public DupTreeIterator(AbsTree<T> n) {
		super(n);
	}
}
