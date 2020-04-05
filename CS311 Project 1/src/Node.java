/**
 * @author Enzo Ciccarelli-Asta
 */
import java.util.Random;
public class Node {
	Interval i;
	Node parent;
	Node left;
	Node right;
	int imax;
	int priority;
	public Node(Interval i) {
		this.i = i;
		Random rand = new Random();
		priority = rand.nextInt();
		imax = i.high;
	}
	Node getParent() {return parent;}
	Node getLeft() {return left;}
	Node getRight() {return right;}
	Interval getInterv() {return i;}
	int getIMax() {return imax;}
	int getPriority() {return priority;}
	public int key() {return i.low;}
	public void setLeft(Node n2) {left = n2;}
	public void setParent(Node n1) {parent = n1;}
	public void setRight(Node n3) {right = n3;}
}