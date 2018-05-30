package com.machinelearning;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyJPanel extends JPanel {
    ArrayList<NodePanel> nl = new ArrayList<>();
    ArrayList<TreeNode> nodes;
    int h=700,w=900;
    public MyJPanel(ArrayList<TreeNode> nodes){
        setSize(w,h);
        setBackground(Color.DARK_GRAY);
        this.nodes=nodes;
        setLocation(200,50);
        //add(drawTree(root,400,0));
       drawTree();

    }
    private void drawTree(){
        int d = nodes.get(nodes.size()-1).getDepth();
        int max =d;
        int counter =0;
        for(int i=nodes.size()-1;i>=0;i--){
            if (nodes.get(i).getDepth()==d){
                this.add(new NodePanel(nodes.get(i),counter*100,h-(max-d)*100));
               // System.out.println(counter*100+" | "+(h-(max-d)*100)+nodes.get(i).getD());
                counter++;
            }else {
                counter=0;
                d--;
                this.add(new NodePanel(nodes.get(i),counter*100,h-(max-d)*100));
               // System.out.println(counter*100+" | "+(h-(max-d)*100)+nodes.get(i).getD());
                counter++;
            }

        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


    }
}
