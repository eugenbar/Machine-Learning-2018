package com.mathematischemodellierung;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Enumeration;

public abstract class VectorObject extends DefaultMutableTreeNode {
   // protected List<VectorObject> listVO= new ArrayList<VectorObject>();
    protected AffineTransform aT = new AffineTransform();
    protected  Color c = new Color(0f,0f,1f);
    public AffineTransform getAT(){
        return this.aT;
    }
    public void affineTranslate(double x,double y){
        this.aT.translate(x,y);
        Enumeration<VectorObject> e = children();
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
       /* Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().setAT(aT);
        }*/
    }
   // protected Graphics2D g2d;
    public void isSelected(){
        c = Color.RED;
        /*for (VectorObject vo : listVO) {
            vo.isSelected();
        }*/
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().isSelected();
        }

    }
    public void notSelected(){
        c = Color.BLUE;
        /*for (VectorObject vo : listVO) {
            vo.notSelected();
        }*/
        Enumeration<VectorObject> e = children();
        while (e.hasMoreElements()) {
            e.nextElement().notSelected();
        }

    }
    public void draw(Graphics2D g2d) {
        Graphics2D g = (Graphics2D)g2d.create();

        /*for (VectorObject vo : listVO) {
            vo.draw(g);
        }*/

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
    }

}
