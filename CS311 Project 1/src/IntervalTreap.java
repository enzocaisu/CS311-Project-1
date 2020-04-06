import java.util.ArrayList;
import java.util.List;

/**
 * @author Enzo Ciccarelli-Asta
 */
public class IntervalTreap {
	public IntervalTreap() {};
	Node root = null;
	int size = 0;
	int height = -1;
	Node getRoot() {return root;}
	int getSize() {return size;}
	int getHeight() {return height;}
	//-------------------------------------------------------------------------------------------------
	void intervalInsert(Node z) {
		int h = 0;//this is approximately correct for keeping track of height.
		if(root == null) {
			root = z;
			z.parent = null;
			size = 1;
			height = 0;
			System.out.println("Inserted root "+z+".");
			return;
		}
		Node x = root;
		z.imax = z.i.high;
		String chiral = null;
		while(x != null) {
			updateImax(x);
			if(z.i.low <= x.i.low) {
				if(x.left != null) {
					x = x.left;
					h++;
					continue;
				} else {
					x.left = z;
					z.parent = x;
					chiral = "left";
					break;
				}
			} else {
				if(x.right != null) {
					x = x.right;
					h++;
					continue;
				} else {
					x.right = z;
					z.parent = x;
					chiral = "right";
					break;
				}
			}
		}
		if(h > height) height = h;
		size++;
		System.out.println("Inserted node "+z+" as "+chiral+" child of "+z.parent+".");
		//End of Phase 1, begin Rotation Up
		rotateUp(z);
		System.out.println();
	}
	//-------------------------------------------------------------------------------------------------
	void rotateUp(Node z) {
		Node g, p = null;
		while(z.parent != null && z.priority < z.parent.priority) {
			g = z.parent.parent;
			p = z.parent;
			
			if(g != null && g.right == p) g.right = z;
			if(g != null && g.left == p) g.left = z;
			z.parent = g;
			if(g == null) root = z;
			
			if(z == p.left) {
				p.left = z.right;
				if(p.left != null) p.left.parent = p;
				z.right = p;
				if(z.left != null) height--;
				if(p.right != null) height++;
			} else {
				p.right = z.left;
				if(p.right != null) p.right.parent = p;
				z.left = p;
				if(z.right != null) height--;
				if(p.left != null) height++;
			}
			p.parent = z;
			updateImax(p);
			System.out.println("Rotated node " + z + " to "+ p +".");
		}
	}
	
	void updateImax(Node p) {
		if(p.left == null && p.right == null) {
			p.imax = p.i.high;
		} else if (p.left == null) {
			p.imax = Math.max(p.i.high, p.right.imax);
		} else if (p.right == null) {
			p.imax = Math.max(p.i.high,  p.left.imax);
		} else {
			p.imax = Math.max(p.i.high, Math.max(p.left.imax, p.right.imax));
		}
	}
	//------------------------------------------------------------------------------------------------
	void intervalDelete(Node z) {
		Node y = z.parent;
		if(z.left == null) {
			Node n = z.right;
			replace(z, n);
			System.out.println("Deleted node "+z);
			z = n;
			return;
		} else if (z.right == null) {
			Node n = z.left;
			replace(z, n);
			System.out.println("Deleted node "+z);
			z = n;
			return;
		} else {
			Node s = findSuccessor(z);
			y = s.parent;
			replace(z, s);
			intervalDelete(z);
			z = s;
		}
		while(y != null) {
			updateImax(y);
			y = y.parent;
		}
		//Phase 1 done, rotate
		int p = Math.min(z.left.priority,  z.right.priority);
		while(z.priority > p) {
			if(z.left.priority == p) {
				rotateUp(z.left);
			} else {
				rotateUp(z.right);
			}
			if(z.left != null && z.right != null) p = Math.min(z.left.priority,  z.right.priority);
			if(z.left == null) p = z.right.priority;
			if(z.right == null) p = z.left.priority;
		}
	}
	
	void replace(Node a, Node b) {
		Node n = a.parent;
		if(n == null) {
		} else if(a == n.left) {
			n.left = b;
		} else {
			n.right = b;
		}
		a.parent = b.parent;
		b.parent = n;
		
		n = a.left;
		a.left = b.left;
		b.left = n;
		if(n != null) n.parent = b;
		
		n = a.right;
		a.right = b.right;
		b.right = n;
		if(n != null) n.parent = b;
		System.out.println("Replaced "+a+" and "+b);
	}
	
	Node findSuccessor(Node z) {
		if(z.right != null) {
			Node r = z.right;
			while(r.left != null) {
				r = r.left;
			}
			return r;
		} else if(z.parent.left == z) return z.parent;
		return null;
	}
	
	Node intervalSearch(Interval i) {
		Node x = root;
		while(x != null && !(i.low <= x.i.high && x.i.low <= i.high)) {
			if(x.left != null && x.left.imax >= i.low) {
				x = x.left;
			} else {
				x = x.right;
			}
		}
		return x;
	}
	
	Node intervalSearch(Node x, Interval i) {
		while(x != null && !x.i.overlaps(i)) {
			if(x.left != null && x.left.imax >= i.low) {
				x = x.left;
			} else {
				x = x.right;
			}
		}
		return x;
	}
	
	List<Interval> overlappingIntervals(Interval i){
		List<Interval> ilist = new ArrayList<Interval>();
		if(root == null) return ilist;
		System.out.println("root "+root);
		Node t = root;
		if(t.i.overlaps(i)) ilist.add(t.i);
		if(t.left != null) {
			root = intervalSearch(t.left, i);
			ilist.addAll(overlappingIntervals(i));
		}
		if(t.right != null) {
			root = intervalSearch(t.right, i);
			ilist.addAll(overlappingIntervals(i));
		}
		root = t;
		return ilist;
	}
	
	public Node intervalSearchExactly(Interval i) {
		Node x = root;
		while(x != null && !(x.i.low == i.low && x.i.high == i.high)) {
			//System.out.println("Exactly checking "+x);
			if(x.left != null && x.left.imax >= i.low) {
				x = x.left;
			} else {
				x = x.right;
			}
		}
		return x;
	}
	
	public Node minimum() {
		Node n = root;
		while(n.left != null) {
			n = n.left;
		}
		return n;
	}
}
