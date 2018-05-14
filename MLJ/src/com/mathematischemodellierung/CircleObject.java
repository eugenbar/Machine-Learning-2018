package com.mathematischemodellierung;

import java.awt.*;

public class CircleObject extends VectorObject {
    private int x,y,width,height;
    public CircleObject(int x, int y, int width, int height){
        this.x =x;this.y=y;this.width=width;this.height=height;
        this.isSelected();
    }

    @Override
    public String toString() {
        return "Circle";
    }

    @Override
    protected void doDrawing(Graphics2D g2d) {
        g2d.drawOval(x,y,width,height);

    }
}
