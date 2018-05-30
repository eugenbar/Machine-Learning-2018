package com.machinelearning;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class NodePanel extends JPanel {
    TreeNode node;
    public NodePanel(TreeNode node,int x,int y){
        this.setSize(75,75);
        if (node.getD()=="root")this.setBackground(Color.RED);
        else if (node.getA()=="leaf")this.setBackground(Color.GREEN);
        else this.setBackground(Color.ORANGE);
        this.setLocation(x,y);
        this.node = node;
        System.out.println("---"+x+" -- "+y);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString(node.getD().toString(), 0, 10);
        g.drawString(node.getA().toString(), 0, 25);
        DecimalFormat df = new DecimalFormat("#.###");
        g.drawString(String.valueOf(df.format(node.getEntropy())), 0, 40);
        g.drawString(node.getClassCountsList().toString(), 0, 55);
    }
}
