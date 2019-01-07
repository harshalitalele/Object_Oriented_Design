
// CSE 522: Assignment 1, Part 1
// UBITName - sahilsuh
class BST_Part1
{

	public static void main(String args[]) 
	{
		Tree tr;
		tr = new Tree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);	
		tr.insert(90);
		tr.delete(20);
		tr.delete(20);
		tr.delete(125);
		tr.delete(150);
		tr.delete(100);
		tr.delete(50);
		tr.delete(75);
		tr.delete(25);
		tr.delete(90);
	}
}

class Tree 
{ 
	// Defines one node of a binary search tree
	public Tree(int n) 
	{
		value = n;
		left = null;
		right = null;	
	}
	public void insert(int n) 
	{
		if (value == n) //n already exists!
			return;		
		if (value < n) //if n>value, insert into the right subtree, else into the left subtree
			if (right == null)
			{
				right = new Tree(n); //creating a new node on the right subtree of the parent
				right.parent = this; ////assigning parent field of the newly created node
			}
			else
				right.insert(n);
		else if (left == null) 
			{
				left = new Tree(n); //creating a new node on the left subtree of the parent
				left.parent = this; //assigning parent field of the newly created node 
			}
		else
			left.insert(n);
	}

	
	public Tree min(Tree t) 
	{
		//this code finds the minimum value node in the right subtree of node t
		t = t.right;
		while (t.left != null)
			t = t.left;
		return t; //will return the minimum valued node in the right subtree of a node
	}
	
	public Tree max(Tree t) 
	{
		// returns the Tree node with the maximum value;
		// this code finds the maximum value node in the left subtree of node t
		t = t.left;
		while (t.right != null)
			t = t.right;
		return t; //will return the maximum valued node in the left subtree of a node
	}

	public Tree find(int n) 
	{
		// returns the Tree node with value n;
		// returns null if n is not present in the tree;
		Tree t = this; // to point to the start of the tree
		if (value == n)
			return t;
		else if (t.value > n) // t.value>n means search the left subtree of 't'
		{
			if (t.left == null)
				return null;
			else
                return t.left.find(n);
		}
        else if (t.value < n) // t.value<n means search the right subtree of 't'
        {
        	if (t.right == null)
        		return null;
        	else
        		return t.right.find(n);
        }
		return null;
	}
	
	public void delete(int n) 
	{  
		//
		// *** do not modify this method ***
		//
		Tree t = find(n);
		if (t == null) // n is not in the tree 
		{ 
			System.out.println("Unable to delete " + n + " -- not in the tree!");
			return;
		} 
		else if (t.left == null && t.right == null) // n is at a leaf position
		{ 
			if (t != this) // if t is not the root of the tree
				case1(t);
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		} 
		else if (t.left == null || t.right == null) 
		{
			// t has one subtree only
			if (t != this) // if t is not the root of the tree 
			{ 
				case2(t);
				return;
			} 
			else // t is the root of the tree with one subtree 
			{ 
				if (t.left != null)
					case3L(t);
				else
					case3R(t);
				return;
			}
		} 
		else 
			// t has two subtrees; replace n with the smallest value in t's right subtree
			case3R(t);
	}
	
	private void case1(Tree t) 
	{  
		// remove leaf node t;
		// you should write the code
		if ( t.parent.value > t.value) // if t's parent value is greater than t's value, make t's parent's left field as null
			t.parent.left = null;
		else
			t.parent.right = null; // if t's parent value is less than t's value, make t's parent's right field as null
		t.parent = null;
	}
	
	private void case2(Tree t) 
	{  
		// remove internal node t;
		if (t.parent.value < t.value) // if this condition is true, we work on RHS of the tree! else, we work of LHS of the tree
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
		else if (t.parent.value > t.value) // working on LHS of the tree
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
	
	private void case3L(Tree t) 
	{   
		// replace t.value with the largest value, v, in
		// t's left subtree; then delete value v from tree;
		// you should write the code
		Tree tr = t;
		tr = tr.max(t); // to access the max value
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

	private void case3R(Tree t) 
	{   
		// replace t.value with the smallest value, v, in
		// t's right subtree; then delete value v from tree;
		// you should write the code
		Tree tr = t;
		tr = tr.min(t); //to access the min value
		if (tr.parent.value < tr.value) //to free 't' from links to it's parent
		{	
			if (tr.right!=null)	
				tr.parent.right = tr.right;
			else
				tr.parent.right = tr.left; 
		}
		else
		{
			if(tr.left!=null)
				tr.parent.left = tr.left;
			else 
				tr.parent.left = tr.right;
		}
		t.value = tr.value; //assigning minimum node 'tr' value to t's value
		if(tr.right!=null) //if right subtree of node tr (which is the minimum node in the right subtree of node t) is non-empty
			tr.right.parent=tr.parent;
		tr.parent= null;
		tr.right=null;
		tr.left=null;

 	}

	protected int value;
	protected Tree left;
	protected Tree right;
    protected Tree parent;
}

























