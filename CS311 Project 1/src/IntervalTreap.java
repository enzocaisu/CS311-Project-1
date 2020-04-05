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
			
			if(z == p.left) {
				p.left = z.right;
				if(p.left != null) p.left.parent = p;
				z.right = p;
			} else {
				p.right = z.left;
				if(p.right != null) p.right.parent = p;
				z.left = p;
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
			p = Math.min(z.left.priority,  z.right.priority);
		}
	}
	
	void replace(Node a, Node b) {
		Node p = a.parent;
		if(p == null) {
		} else if(a == p.left) {
			p.left = b;
		} else {
			p.right = b;
		}
		b.parent = p;
		p = a.left;
		a.left = b.left;
		b.left = p;
		if(p != null) p.parent = b;
		
		p = a.right;
		a.right = b.right;
		b.right = p;
		if(p != null) p.parent = b;
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
		List<Interval> ilist = null;
		Node temp = root;
		Node n = root;
		while(n != null) {
			n = intervalSearch(n, i);
			ilist.add(n.i);
			root = n.left;
			ilist.addAll(overlappingIntervals(i));
			root = n.right;
			ilist.addAll(overlappingIntervals(i));
		}
		root = temp;
		return ilist;
	}
	
	Node intervalSearchExactly(Interval i) {
		Node x = root;
		while(x != null && !(i.low == x.i.low && x.i.high == i.high)) {
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
