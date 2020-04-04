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
			x.imax = Math.max(x.imax,z.i.high);
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
		//End of Phase 1, begin Rotation
		while(z.priority < z.parent.priority) {
			if(z == z.parent.left) {
				Node g = z.parent.parent;
				Node p = z.parent;
				if(g.right == p) g.right = z;
				if(g.left == p) g.left = z;
				z.parent = g;
				p.left = z.right;
				if(p.left != null) p.left.parent = p;
				z.right = p;
				p.parent = z;
				p.imax = Math.max(p.imax, p.right.imax);
			} else {
				Node g = z.parent.parent;
				Node p = z.parent;
				if(g.right == p) g.right = z;
				if(g.left == p) g.left = z;
				z.parent = g;
				p.right = z.left;
				if(p.right != null) p.right.parent = p;
				z.left = p;
				p.parent = z;
				p.imax = Math.max(p.imax, p.right.imax);
			}
		}
	}
	
	void intervalDelete(Node z) {
		
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
}
