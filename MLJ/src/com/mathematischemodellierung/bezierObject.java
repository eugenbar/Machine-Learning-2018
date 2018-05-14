package com.mathematischemodellierung;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class bezierObject extends VectorObject{
    private List<Point> points = new ArrayList<Point>();
    public bezierObject(Point p1){
        this.points.add(p1);
    }
    public bezierObject(int x1, int y1){
        this.points.add(new Point(x1,y1));
        this.isSelected();
    }
    public void addPoint(Point p1){
        points.add(p1);
    }
    @Override
    public String toString() {
        return "Bezier";
    }

    @Override
    public void doDrawing(Graphics2D g2d) {
        for(int i = 0;i<points.size();i++){
            if(i==points.size()-1){
                g2d.drawLine(points.get(i).x,points.get(i).y,
                        points.get(i).x,points.get(i).y);
            }else{
                g2d.drawLine(points.get(i).x,points.get(i).y,
                        points.get(i+1).x,points.get(i+1).y);
            }
        }

    }
}
