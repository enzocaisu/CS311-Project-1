import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Teststuff extends JFrame implements MouseListener {

    private int size = 50;
    private IntervalTreap t;
    private Node searchedNode;
    private ArrayList<Node> nodes;
    private ArrayList<Rectangle> bounds;
    public Teststuff(IntervalTreap t){
        super("Test");
        this.t = t;
        nodes = new ArrayList<Node>();
        bounds = new ArrayList<Rectangle>();
        addMouseListener(this);
        JTextField low = new JTextField(4);
        JTextField high = new JTextField(4);
        JButton search = new JButton("Search");
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(low);
        searchPanel.add(high);
        searchPanel.add(search);
        JButton addNew = new JButton("Add Random Node");
        addNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Node z;
                Random rand = new Random();
                int w = rand.nextInt(200) - 100;
                int j = rand.nextInt(200) - 100;
                int l, s;
                if (w > j) {
                    l = w;
                    s = j;
                } else {
                    l = j;
                    s = w;
                }
                z = new Node(new Interval(s, l));
                t.intervalInsert(z);
                repaint();
            }
        });
        searchPanel.add(addNew);
        add(searchPanel,BorderLayout.SOUTH);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int lo = Integer.parseInt(low.getText());
                    int hi = Integer.parseInt(high.getText());
                    Interval foo = new Interval(lo,hi);
                    searchedNode = t.intervalSearch(foo);
                    repaint();
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),"Please Enter Valid Number :)");
                }

            }
        });
        setPreferredSize(new Dimension(500,500));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        nodes.clear();
        bounds.clear();
        if(t.getRoot() != null){
            draw(t.getRoot(), 50, getWidth()/2,getWidth()/2,g,-1,-1);
        }
    }
    private void draw(Node node, int depth, int leftRight, int distance, Graphics g, int x, int y){
        if(searchedNode != null && searchedNode == node){
            g.setColor(Color.red);
        }else {
            g.setColor(Color.black);
        }
        nodes.add(node);
        bounds.add(new Rectangle(leftRight,depth,size,size));
        g.drawRect(leftRight,depth,size,size);
        g.setColor(Color.black);
        g.drawChars(node.getInterv().toString().toCharArray(),0,node.getInterv().toString().length(),leftRight + 5,depth + size /3);
        g.drawChars(Integer.toString(node.getIMax()).toCharArray(), 0, Integer.toString(node.getIMax()).length(),leftRight + 5, depth + size /3 + size/3);
        g.drawChars(Integer.toString(node.getPriority()).toCharArray(), 0, Integer.toString(node.getPriority()).length(),leftRight + 5, depth + size /3 + size/3 + size/3);
        if(x != -1 && y != -1){
            g.drawLine(leftRight + size/2, depth,x + size/2, y + size/2);
        }
        if(node.getLeft() != null){
            draw(node.getLeft(), depth + size, leftRight - distance/2,distance/2,g,leftRight,depth);
        }if(node.getRight() != null){
            draw(node.getRight(), depth + size, leftRight + distance/2, distance/2,g,leftRight,depth);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for(int i = 0; i < bounds.size();i++){
            if(bounds.get(i).contains(e.getX(),e.getY())){
                t.intervalDelete(nodes.get(i));
                repaint();
                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}