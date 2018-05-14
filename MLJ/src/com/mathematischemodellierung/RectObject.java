package com.mathematischemodellierung;

import java.awt.*;

public class RectObject extends VectorObject{
    private Point p1;
    private int width,height;
    public RectObject(Point p1, int width, int height){
        this.p1 = p1;this.width = width;this.height = height;
        this.isSelected();
    }
    public RectObject(int x,int y, int width, int height){
        this.p1 = new Point(x,y);this.width = width;this.height = height;
    }
    @Override
    public String toString() {
        return "Rect";
    }

    @Override
    public void doDrawing(Graphics2D g2d) {
        g2d.drawRect(p1.x,p1.y,width,height);
    }
}
