package com.mathematischemodellierung;

import java.awt.*;

public class LineObject extends VectorObject{
    private Point p1,p2;
    public LineObject(Point p1, Point p2){
        this.p1 = p1;this.p2 = p2;
    }
    public LineObject(int x1, int y1,int x2,int y2){
        this.p1 = new Point(x1,y1);this.p2 = new Point(x2,y2);
        this.isSelected();
    }

    @Override
    public String toString() {
        return "Line";
    }

    @Override
    public void doDrawing(Graphics2D g2d) {
        g2d.drawLine(p1.x,p1.y,p2.x,p2.y);
    }
}
