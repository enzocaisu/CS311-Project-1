import java.util.List;

import org.junit.Test;

public class Tests {
	public static void main(String[] args) {
		IntervalTreap itreap = new IntervalTreap();
		Node a = new Node(new Interval(16, 21));
		a.priority = 8;
		Node b = new Node(new Interval(8, 9));
		b.priority = 12;
		Node c = new Node(new Interval(5,8));
		c.priority = 17;
		Node d = new Node(new Interval(0,3));
		d.priority = 21;
		Node e = new Node(new Interval(6, 10));
		e.priority = 20;
		Node f = new Node(new Interval(7, 25));
		f.priority = 9;
		Node g = new Node(new Interval(15, 23));
		g.priority = 16;
		Node h = new Node(new Interval(25, 30));
		h.priority = 10;
		Node i = new Node(new Interval(26, 26));
		i.priority = 11;
		Node j = new Node(new Interval(17, 19));
		j.priority = 13;
		Node k = new Node(new Interval(19, 20));
		k.priority = 17;
		itreap.intervalInsert(a);
		itreap.intervalInsert(b);
		itreap.intervalInsert(c);
		itreap.intervalInsert(d);
		itreap.intervalInsert(e);
		itreap.intervalInsert(f);
		itreap.intervalInsert(g);
		itreap.intervalInsert(h);
		itreap.intervalInsert(i);
		itreap.intervalInsert(j);
		itreap.intervalInsert(k);
	
		System.out.println("Searched for 27, found "+ itreap.intervalSearch(new Interval(27, 27)));
		System.out.println("Searched for 33, found "+ itreap.intervalSearch(new Interval(33, 33)));
		
		System.out.println(itreap.getHeight());
		
		System.out.println(itreap.intervalSearchExactly(new Interval(27, 27)));
		System.out.println(itreap.intervalSearchExactly(new Interval(25, 30)));
		List<Interval> ilist = itreap.overlappingIntervals(new Interval(19, 20));
		for(Interval in : ilist) {
			System.out.println("["+in.low+", "+in.high+"]");
		}
		itreap.intervalDelete(itreap.root);
		return;
	}
}
