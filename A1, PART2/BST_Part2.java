//CSE 522: Assignment 1, Part 2
// UBITName - sahilsuh
class BST_Part2 
{
	public static void main(String[] args) 
	{
		AbsTree tr = new DupTree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);
		tr.insert(90);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(75);
		tr.insert(90);
		
		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
	}
}
  
abstract class AbsTree 
{

	public AbsTree(int n) 
	{
		value = n;
		left = null;
		right = null;
	}
	
	public void insert(int n) 
	{
		if (value == n)
			count_duplicates();
		else if (value < n)
			if (right == null)
			{
				right = add_node(n);
				right.parent = this;
			}
			else
				right.insert(n);
		else if (left == null)
		{
			left = add_node(n);
			left.parent = this;
		}
		else
			left.insert(n);
	}
	
	public void delete(int n) 
	{  
		//
		// *** do not modify this method ***
		//
		AbsTree t = find(n);
		if (t == null) // n is not in the tree 
		{ 
			System.out.println("Unable to delete " + n + " -- not in the tree!");
			return;
		} 
		else if (t.left == null && t.right == null) // n is at a leaf position
		{ 
			if (t != this) // if t is not the root of the tree
			{
				if (check(t)) //check method checks whether the count == 1, if yes the node is deleted else count is decremented
					case1(t);
			}
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		} 
		else if (t.left == null || t.right == null) 
		{
			// t has one subtree only
			if (t != this) // if t is not the root of the tree 
			{ 
				if (check(t))
				{
					case2(t);
				}
				return;
			} 
			else // t is the root of the tree with one subtree 
			{ 
				if (t.left != null)
				{
					if (check(t))
						case3L(t);
				}
				else
				{
					if (check(t))
						case3R(t);
				}
				return;
			}
		} 
		else 
		{	// t has two subtrees; replace n with the smallest value in t's right subtree
			if (check(t))
				case3R(t);
			return;
		}
	}
	
	protected void case1(AbsTree t) 
	{  
		// adapt Part 1 solution and use here
		// remove leaf node t;
		if ( t.parent.value > t.value) // if t's parent value is greater than t's value, make t's parent's left field as null
			t.parent.left = null;
		else
			t.parent.right = null; // if t's parent value is less than t's value, make t's parent's right field as null
		t.parent = null;	
	}
	
	protected void case2(AbsTree t) 
	{ 
		// adapt Part 1 solution and use here
		// remove internal node t;
		if (t.parent.value < t.value) //if this condition is true, we work on RHS of the tree! else, we work of LHS of the tree
		{
			if (t.left == null) // if 't' node does not have a left subtree
			{
				t.parent.right = t.right; // bypassing logic
				t.right.parent = t.parent;
				t.right = null;
				t.parent = null;
			}
			else // if 't' node does not have a right subtree
			{
				t.parent.right = t.left; // bypassing logic
				t.left.parent = t.parent;
				t.left = null;
				t.parent = null;
			}
		}
		else if (t.parent.value > t.value) //working on LHS of the tree
		{
			if(t.left == null) 
			{
				t.parent.left = t.right; // bypassing logic
				t.right.parent = t.parent;
				t.right = null;
				t.parent = null;
			}
			else
			{
				t.parent.left = t.left;	// bypassing logic
				t.left.parent = t.parent;
				t.left = null;
				t.parent = null;
			}
		}
	}
	
	
	protected void case3L(AbsTree t) 
	{ 
		// adapt Part 1 solution and use here
		// replace t.value with the largest value, v, in
		// t's left subtree; then delete value v from tree;
		AbsTree tr = t;
		tr = tr.max(t); // to access the max value        
		updateCount(t,tr);
		if (tr.parent.value < tr.value) // to free 't' from links to it's parent
		{
			if (tr.right != null)	
				tr.parent.right = tr.right;
			else
				tr.parent.right = tr.left;
		}
		else
		{
			if(tr.left != null)
				tr.parent.left = tr.left;
			else 
				tr.parent.left = tr.right;
		}
		t.value = tr.value;   // assigning maximum node 'tr' value to t's value
		if (tr.left != null) // if left subtree of node tr (which is the maximum node in the left subtree of node t) is non-empty
			tr.left.parent=tr.parent;
		tr.parent = null;
		tr.right = null;
		tr.left = null;
	}

	protected void case3R(AbsTree t) 
	{  
		// adapt Part 1 solution and use here
		// replace t.value with the smallest value, v, in
		// t's right subtree; then delete value v from tree;
		AbsTree tr = t;
		tr = tr.min(t); // to access the min value
		updateCount(t,tr);
		if (tr.parent.value < tr.value) // to free 'tr' from links to it's parent
		{	
			if (tr.right != null)	
				tr.parent.right = tr.right;
			else
				tr.parent.right = tr.left; 
		}
		else
		{
			if(tr.left != null)
				tr.parent.left = tr.left;
			else 
				tr.parent.left = tr.right;
		}
		t.value = tr.value; // assigning minimum node 'tr' value to t's value
		if(tr.right!=null) // if right subtree of node tr (which is the minimum node in the right subtree of node t) is non-empty
			tr.right.parent=tr.parent;
		tr.parent= null;
		tr.right=null;
		tr.left=null;
	}

	private AbsTree find(int n) 
	{
		// returns the Tree node with value n;
		// returns null if n is not present in the tree;
		AbsTree t = this; // to point to the start of the tree
		if (value == n)
			return t;
		else if (t.value > n) // t.value > n means search the left subtree of node t
		{
			if (t.left == null)
				return null;
			else
                return t.left.find(n);
		}
        else if (t.value < n) // t.value < n means search the right subtree of node t
        {
        	if (t.right == null)
        		return null;
        	else
        		return t.right.find(n);
        }
		return null;
	}
	
	public AbsTree min(AbsTree t) 
	{
		// this code finds the minimum value node in the right subtree of node t
		t = t.right;
		while (t.left != null)
			t = t.left;
		return t; // will return the minimum valued node in the right subtree of a node
	}

	public AbsTree max(AbsTree t) 
	{
		// this code finds the maximum value node in the left subtree of node t
		t = t.left;
		while (t.right != null)
			t = t.right;
		return t; // will return the maximum valued node in the left subtree of a node
	}

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
	protected AbsTree parent;

	// Protected Abstract Methods
	
	protected abstract AbsTree add_node(int n);
	protected abstract void count_duplicates();
	
	// Additional protected abstract methods, as needed
	
	protected abstract void updateCount(AbsTree t, AbsTree tr);
	protected abstract boolean check(AbsTree t);
}


class Tree extends AbsTree {

	public Tree(int n) {
		super(n);
		count=1;
	}

	protected AbsTree add_node(int n) {
		return new Tree(n);
	}

	protected void count_duplicates() {
		count++;
	}
	protected int count;
	// define additional protected methods here, as needed
	protected boolean check(AbsTree t) 
	{
		Tree tr = (Tree) t;
		if (tr.count == 1)
			return true;
		else
		{
			tr.count--;
			return false;
		}
	}
	
	protected void updateCount(AbsTree t, AbsTree tr) 
	{
		Tree tr1 = (Tree) t;
		Tree tr2 = (Tree) tr;
		tr1.count = tr2.count;
		
	}
}

class DupTree extends AbsTree {

	public DupTree(int n) {
		super(n);
		count = 1;
	};


	protected AbsTree add_node(int n) {
		return new DupTree(n);
	}

	protected void count_duplicates() {
		count++;
	}

	// define additional protected methods here, as needed
	protected void updateCount(AbsTree t, AbsTree tr)
	{
		DupTree tr1 = (DupTree) t;
		DupTree tr2 = (DupTree) tr;
		tr1.count = tr2.count;
	}
	protected boolean check(AbsTree t)
	{
		DupTree tr = (DupTree) t;
		if (tr.count == 1)
			return true;
		else
		{
			tr.count--;
			return false;
		}
	}
	protected int count;
}