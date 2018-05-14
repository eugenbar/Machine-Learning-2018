package com.mathematischemodellierung;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class VectorObject2{
    public List<Point> points = new ArrayList<Point>();
    public String objectType = "";
    public boolean isSelected=false;
    public boolean isDrawing = false;
    private Point border1,border2;
    private int selectedPoint = -1;
    private int thickness = 10;
    private Color c1=Color.MAGENTA,c2=Color.yellow;
    private boolean marked = false;
    private Point prevMousePosition = new Point(0,0);
    private boolean dragging = false;
    private boolean isScaleable = false;
    private Point shadingPoint = new Point(0,0);
    private boolean shading = false;
    private List<Point>[] shadingArray;

    public VectorObject2(Point p1,String objectType){
        points.add(p1);
        this.objectType=objectType;
        isSelected = true;
        isDrawing = true;
        border1 = new Point(p1.x-thickness,p1.y-thickness);
        border2 = new Point(p1.x+thickness,p1.y+thickness);
    }
    public void addPoint(Point p){
        points.add(p);
        setBorders();
        if(objectType=="Rect"){
            points.add(new Point(points.get(0).x,points.get(1).y));
            points.add(new Point(points.get(1).x,points.get(0).y));
        }

    }
    public void setPoint(int i,Point p){
        if(i<points.size()){
            points.get(i).setLocation(p);
        }
        setBorders();
    }
    public void draw(Graphics2D g2d) {
        g2d.setColor(c1);
        if (objectType == "Bezier" || points.size() < 2) {
            /*for (int i = 0; i < points.size(); i++) {
                if (i == points.size() - 1) {
                    g2d.drawLine(points.get(i).x, points.get(i).y,
                            points.get(i).x, points.get(i).y);
                } else {
                    g2d.drawLine(points.get(i).x, points.get(i).y,
                            points.get(i + 1).x, points.get(i + 1).y);
                }

            }*/
            if (objectType == "Bezier" && points.size() > 2) {
                for (int i = 0; i < points.size() * 500; i++) {
                    besierCurvePixel(g2d, (float) i / (500 * points.size()));
                }
            }
        } else if (objectType == "Rect") {
            drawLine(g2d, points.get(0), points.get(2));
            drawLine(g2d, points.get(0), points.get(3));
            drawLine(g2d, points.get(1), points.get(3));
            drawLine(g2d, points.get(1), points.get(2));
        } else if (objectType == "Line") {
            //g2d.drawLine(points.get(0).x,points.get(0).y,
            //      points.get(1).x,points.get(1).y);
            drawLine(g2d, points.get(0), points.get(1));
        } else if (objectType == "Circle") {
            g2d.drawOval(points.get(0).x, points.get(0).y,
                    Math.abs(points.get(0).x - points.get(1).x),
                    Math.abs(points.get(0).y - points.get(1).y));
        }
            if (isSelected) {
                drawSelection(g2d);
                drawPoints(g2d);
                if (checkMarked()) {
                    drawMarked(g2d);
                }
                drawScaleable(g2d, getPrevMousePosition());
                //drawShading(g2d);
            }
            if(shading){
                drawShading(g2d);
            }

        }

    private void drawLine(Graphics2D g2d,Point p1,Point p2){
        double m = (double) (p2.y-p1.y)/(double)(p2.x-p1.x);
        double mx = (double) (p2.x-p1.x)/(double)(p2.y-p1.y);
        if(Math.abs(mx)>Math.abs(m)) {
            if (p2.x >= p1.x) {
                for (int i = p1.x; i < p2.x; i += 1) {
                    g2d.fillOval(i, p1.y + (int) ((i - p1.x) * m), thickness, thickness);
                }

            } else {
                for (int i = p1.x; i > p2.x; i -= 1) {
                    g2d.fillOval(i, p1.y + (int) ((i - p1.x) * m), thickness, thickness);
                }
            }
        }else {
            if (p2.y >= p1.y) {
                for (int i = p1.y; i < p2.y; i += 1) {
                    g2d.fillOval(p1.x+(int)((i-p1.y)*mx), i, thickness, thickness);
                }

            } else {
                for (int i = p1.y; i > p2.y; i -= 1) {
                    g2d.fillOval(p1.x+(int)((i-p1.y)*mx), i, thickness, thickness);
                }
            }
        }
    }
    public void setMarked(boolean b){
        marked = b;
    }
    public boolean checkMarked(){
        return marked;
    }
    public void setSelectedPoint(int i){
        selectedPoint = i;
        setBorders();
    }
    public int getSelectedPoint(){
        return selectedPoint;
    }
    private void drawPoints(Graphics2D g2d){
        g2d.setColor(Color.CYAN);
        for(int i=0;i<points.size();i++){
            if(i==selectedPoint ){
                g2d.setColor(Color.red);
                g2d.fillOval(points.get(i).x,points.get(i).y,10,10);
                g2d.setColor(Color.CYAN);
            }else {
                g2d.fillOval(points.get(i).x, points.get(i).y, 10, 10);
            }
        }if(!shadingPoint.equals(new Point(0,0))){
            g2d.fillOval(shadingPoint.x,shadingPoint.y,10,10);
        }
    }
    private void drawSelection(Graphics2D g2d){
        g2d.setColor(Color.GREEN);
        g2d.drawRect(border1.x,border1.y,
                border2.x-border1.x,border2.y-border1.y);
        g2d.drawLine(border1.x,border1.y,border2.x,border1.y);
        g2d.drawLine(border1.x,border1.y,border1.x,border2.y);
        g2d.drawLine(border2.x,border2.y,border1.x,border2.y);
        g2d.drawLine(border2.x,border2.y,border2.x,border1.y);
    }
    private void drawMarked(Graphics2D g2d){
        if(checkMarked()){
            g2d.setColor(new Color(0.5f,0.5f,0.5f,0.5f));
            g2d.fillRect(border1.x,border1.y,border2.x-border1.x,
                    border2.y-border1.y);
            g2d.setColor(c1);
        }
    }
    public void setShading(boolean b){

        if(b){
            shadingPoint = new Point((border2.x-border1.x)/2,border2.y);
            shadingArray = (ArrayList<Point>[])new ArrayList[100];
            double a = 5;
            double e = shadingPoint.y-border2.y;
            double d = ((border2.x-border1.x)/2) - shadingPoint.x;
            double x1 = d-(border2.x-border1.x)/2;
            double x2 = d+(border2.x-border1.x)/2;
            for(int i=0;i<100;i++){
                double dx = x1+Math.random()*(x2-x1);
                double dy = a*(dx-d)*(dx-d)+e;
                //for(int j=0;j<points.size())
                for(int j = 0;j<points.size();j++){
                    shadingArray[i].add(new Point((int)(points.get(j).x+dx),(int)(points.get(j).y+dy)));
                }

                //g2d.setColor(new Color(c1.getRed(),c1.getGreen(),c1.getBlue(),1));
            }
            shading=b;
            //points.add(shadingPoint);
           // drawShading();
        }else {
            shadingPoint=new Point(0,0);
        }
    }
    public boolean getShading(){
        return shading;
    }
    public void setBorders(){
        border1.setLocation(points.get(0).x-thickness,points.get(0).y-thickness);
        border2.setLocation(points.get(0).x+thickness,points.get(0).y+thickness);
        for(int i=0;i<points.size();i++){
            if(points.get(i).x<border1.x){
                border1 = new Point(points.get(i).x,border1.y);
            }
            if(points.get(i).x>border2.x){
                border2 = new Point(points.get(i).x,border2.y);
            }
            if(points.get(i).y<border1.y){
                border1 = new Point(border1.x,points.get(i).y);
            }
            if(points.get(i).y>border2.y){
                border2 = new Point(border2.x,points.get(i).y);
            }
        }
        border1.x-=thickness;
        border1.y-=thickness;
        border2.x+=2*thickness;
        border2.y+=2*thickness;

    }
    public void setColor(Color c){
        c1 = c;
    }
    public Point getBorder1(){
        return border1;
    }
    public Point getBorder2(){
        return border2;
    }
    public Point getPrevMousePosition(){
        return prevMousePosition;
    }
    public void setPrevMousePosition(Point p){
        prevMousePosition=p;
    }
    public void setScaleable(boolean b){
        isScaleable = b;
    }
    public boolean getScaleable(){
        return isScaleable;
    }
    private void drawScaleable(Graphics2D g2d,Point p){
        if(getScaleable()){
            g2d.setColor(Color.RED);
            g2d.drawLine(p.x,p.y-2*thickness,p.x,p.y+2*thickness);
            g2d.drawLine(p.x,p.y-2*thickness,p.x-thickness/2,p.y-3*thickness/2);
            g2d.drawLine(p.x,p.y-2*thickness,p.x+thickness/2,p.y-3*thickness/2);
            g2d.drawLine(p.x,p.y+2*thickness,p.x-thickness/2,p.y+3*thickness/2);
            g2d.drawLine(p.x,p.y+2*thickness,p.x+thickness/2,p.y+3*thickness/2);

            g2d.drawLine(p.x-2*thickness,p.y,p.x+2*thickness,p.y);
            g2d.drawLine(p.x-2*thickness,p.y,p.x-3*thickness/2,p.y-thickness/2);
            g2d.drawLine(p.x-2*thickness,p.y,p.x-3*thickness/2,p.y+thickness/2);
            g2d.drawLine(p.x+2*thickness,p.y,p.x+3*thickness/2,p.y-thickness/2);
            g2d.drawLine(p.x+2*thickness,p.y,p.x+3*thickness/2,p.y+thickness/2);

            g2d.setColor(c1);
        }
    }
    public void dragMove(Point p){
        if(prevMousePosition.equals(new Point(0,0))){
            setPrevMousePosition(p);
        }
        if(!p.equals(prevMousePosition)){
            int dx = p.x-prevMousePosition.x;
            int dy = p.y-prevMousePosition.y;
            double sx = 1+(double)(dx/(border2.x-border1.x));
            double sy = 1+(double)(dy/(border2.y-border1.y));
            for(int i=0;i<points.size();i++){
                if(getScaleable()){
                    points.get(i).setLocation(points.get(i).x*sx,points.get(i).y*sy);

                }else {
                    points.get(i).x += dx;
                    points.get(i).y += dy;
                }
            }
            setBorders();
            setPrevMousePosition(p);
            dragging=true;
        }
    }
    public void undragged(){
        setPrevMousePosition(new Point(0,0));
        dragging=false;
    }
    public int getThickness(){
        return thickness;
    }
    //Factorial
    private static int fact(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    public void setThickness(boolean inc){
        if(inc){
            thickness+=1;
        }
        else {
            thickness-=1;
        }
    }
    //Bernstein polynomial
    private void drawShading(Graphics2D g2d){
        if(shading){
            g2d.setColor(new Color(c1.getRed(),c1.getGreen(),c1.getBlue(),1));
            for(int i=0;i<100;i++){
                drawLine(g2d, shadingArray[i].get(0), shadingArray[i].get(1));

            }
        }
    }
    private static double bernstein(float t, int n, int i){

        return (fact(n) / (fact(i) * fact(n-i))) * Math.pow(1-t, n-i) * Math.pow(t, i);
    }

    private void besierCurvePixel(Graphics2D g2d,float t){
        int pointCount=points.size();
        double bPoly[] = new double[pointCount];

        for(int i = 0; i < pointCount; i++){
            bPoly[i] = bernstein(t, pointCount-1, i+1);
        }

        double sumX = 0;
        double sumY = 0;

        for(int i = 0; i < pointCount;  i++){
            sumX += bPoly[i] * points.get(i).x;
            sumY += bPoly[i] * points.get(i).y;
        }

        int x, y;
        x = (int) Math.round(sumX);
        y = (int) Math.round(sumY);
        int offsetX =(int)(points.get(0).x*(1-t));
        int offsetY = (int)(points.get(0).y*(1-t));
        //g2d.drawLine(x + offsetX, y + offsetY, x + offsetX, y + offsetY);
        g2d.fillOval(x+offsetX,y+offsetY,thickness,thickness);
    }

    // protected List<VectorObject> listVO= new ArrayList<VectorObject>();
  /*  protected AffineTransform aT = new AffineTransform();
    protected  Color c = new Color(0f,0f,1f);
    public AffineTransform getAT(){
        return this.aT;
    }
    public void affineTranslate(double x,double y){
        this.aT.translate(x,y);
        Enumeration<VectorObject2> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().affineTranslate(x,y);
        }

    }
    public void affineScale(double x,double y){
        this.aT.scale(x,y);
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().affineScale(x,y);
        }

    }
    public void affineRotate(double th){
        this.aT.rotate(th);
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().affineRotate(th);
        }

    }
    public void setAT(AffineTransform aT){
        this.aT = aT;
       *//* Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().setAT(aT);
        }*//*
    }
    // protected Graphics2D g2d;
    public void isSelected(){
        c = Color.RED;
        *//*for (VectorObject vo : listVO) {
            vo.isSelected();
        }*//*
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().isSelected();
        }

    }
    public void notSelected(){
        c = Color.BLUE;
        *//*for (VectorObject vo : listVO) {
            vo.notSelected();
        }*//*
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().notSelected();
        }

    }
    public void draw(Graphics2D g2d) {
        Graphics2D g = (Graphics2D)g2d.create();

        *//*for (VectorObject vo : listVO) {
            vo.draw(g);
        }*//*

        g.setTransform(aT);
        g.setColor(c);
        doDrawing(g);
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().draw(g);
        }

        g.dispose();
    }
    @Override
    public abstract String toString();




    protected abstract void doDrawing(Graphics2D g2d);
    public void addObject(VectorObject vo){
        //listVO.add(vo);
        add(vo);
    }*/

}
