import java.util.List;

/**
 * @author Enzo Ciccarelli-Asta
 */
public class IntervalTreap {
	public IntervalTreap() {};
	Node root = null;
	int size = 0;
	int height = 0;
	Node getRoot() {return root;}
	int getSize() {return size;}
	int getHeight() {return height;}
	
	void intervalInsert(Node z) {
		if(root == null) {
			root = z;
			z.parent = z;
			size = 1;
			height = 1;
			return;
		}
		Node x = root;
		z.imax = z.i.high;
		while(x != null) {
			updateImax(x);
			if(z.i.low <= x.i.low) {
				if(x.left != null) {
					x = x.left;
					continue;
				} else {
					x.left = z;
					z.parent = x;
					break;
				}
			} else {
				if(x.right != null) {
					x = x.right;
					continue;
				} else {
					x.right = z;
					z.parent = x;
					break;
				}
			}
		}
		//End of Phase 1, begin Rotation Up
		rotateUp(z);
	}
	
	void rotateUp(Node z) {
		Node g, p = null;
		while(z.priority < z.parent.priority) {
			g = z.parent.parent;
			p = z.parent;
			if(g.right == p) g.right = z;
			if(g.left == p) g.left = z;
			z.parent = g;
			if(z == z.parent.left) {
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
	
	void intervalDelete(Node z) {
		Node y = null;
		if(z.left == null) {
			replace(z, z.right);
		} else if (z.right == null) {
			replace(z, z.left);
		} else {
			Node s = findSuccessor(z);
			y = s.parent;
			replace(z, s);
		}
		if(z == null) {
			y = z.parent;
		}
		while(y != null) {
			updateImax(y);
			y = y.parent;
		}
		//Phase 1 done, rotate
		int p = Math.min(z.left.priority,  z.right.priority);
		while(z.priority >= p) {
			if(z.left.priority == p) {
				rotateUp(z.left);
			} else {
				rotateUp(z.right);
			}
			p = Math.min(z.left.priority,  z.right.priority);
		}
	}
	
	/**
	 * Replaces node a with node b in the treap. Maintains b's children, does not maintain a's children.
	 * @param a
	 * @param b
	 */
	void replace(Node a, Node b) {
		Node p = a.parent;
		if(a == p.left) {
			p.left = b;
		} else {
			p.right = b;
		}
		b.parent = p;
	}
	
	/**
	 * Finds the successor of a node in the treap. Should allow inorder traversal.
	 * @param z
	 * @return
	 */
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
	
	public List<Node> inOrder() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Node minimum() {
		Node n = root;
		while(n.left != null) {
			n = n.left;
		}
		return n;
	}
}
