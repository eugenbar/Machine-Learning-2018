package com.mathematischemodellierung;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon3D implements Comparable<Polygon3D>{
    private Color color = new Color(1f,0f,0f);
    private List<Vertex> vertexList = new ArrayList<>();
    private int depthValue;
    public int getDepthValue(){
        for(Vertex v:vertexList){
            depthValue+=(int)v.getZ();
        }
        depthValue = (int)(depthValue/vertexList.size());
        return depthValue;
    }
    public Polygon3D(){

    }
    public Polygon3D(Color c){
        this.color = c;

    }
    public void addV(Vertex v){
        vertexList.add(v);
    }
    public List<Vertex> getVertexList(){
        return vertexList;
    }
    public void setColor(Color c){
        this.color=c;
    }
    public Color getColor(){
        return this.color;
    }
    public void drawWire(Graphics2D g2d){
        g2d.setColor(color);
       /* for(int i=0;i<vertexList.size()-1;i++){
            g2d.drawLine((int)vertexList.get(i).getX(),
                    (int)vertexList.get(i).getY(),
                    (int)vertexList.get(i+1).getX(),
                    (int)vertexList.get(i+1).getY());
        }
        //g2d.drawLine((int)vertexList.get(vertexList.size()-1).getX(),
          //      (int)vertexList.get(vertexList.size()-1).getY(),
            //    (int)vertexList.get(0).getX(),
              //  (int)vertexList.get(0).getY());*/
        g2d.drawLine((int)vertexList.get(0).getX(),
                (int)vertexList.get(0).getY(),
                (int)vertexList.get(1).getX(),
                (int)vertexList.get(1).getY());
        g2d.drawLine((int)vertexList.get(1).getX(),
                (int)vertexList.get(1).getY(),
                (int)vertexList.get(2).getX(),
                (int)vertexList.get(2).getY());
        g2d.drawLine((int)vertexList.get(2).getX(),
                (int)vertexList.get(2).getY(),
                (int)vertexList.get(3).getX(),
                (int)vertexList.get(3).getY());
        g2d.drawLine((int)vertexList.get(3).getX(),
                (int)vertexList.get(3).getY(),
                (int)vertexList.get(0).getX(),
                (int)vertexList.get(0).getY());
    }
    public void draw(Graphics2D g2d){
        Polygon p = new Polygon();
        for(Vertex v:vertexList){
            double px,py;
            //px = v.getX()+0.5*Math.sqrt(2*v.getZ()/50);
            //py = v.getY()+0.5*Math.sqrt(2*v.getZ()/50);
            //px = -400*v.getX()/v.getZ();
            //py = -400*v.getY()/v.getZ();
            px=v.getX();py=v.getY();
            p.addPoint((int)px,(int)py);
        }
        g2d.setColor(color);
        g2d.fillPolygon(p);
    }
    public boolean checkNormal(Graphics2D g2d){
        double vx,vy,vz,uy,ux,uz,nx,ny,nz;
        vx = vertexList.get(0).getX()-vertexList.get(1).getX();
        vy = vertexList.get(0).getY()-vertexList.get(1).getY();
        vz = vertexList.get(0).getZ()-vertexList.get(1).getZ();
        ux = vertexList.get(1).getX()-vertexList.get(2).getX();
        uy = vertexList.get(1).getY()-vertexList.get(2).getY();
        uz = vertexList.get(1).getZ()-vertexList.get(2).getZ();
        nx = vy*uz-vz*uy;
        ny = vz*ux-vx*uz;
        nz = vx*uy-vy*ux;
        //nx=-1*nx;ny=-1*ny;nz = nz * -1;
        g2d.setColor(color);
        g2d.drawLine(0,0,(int)(nx)*10,(int)(ny)*10);
        if (nz>0) return true;
        else return false;
    }
    public void scale(double s){
        for (Vertex v:vertexList){
            v.setX(v.getX()*s);
            v.setY(v.getY()*s);
            v.setZ(v.getZ()*s);

        }
    }
    public void rotateY(double alpha){
        for(Vertex v:vertexList){
            double newX,newZ;
            newX = v.getX()*Math.cos(alpha)+v.getZ()*Math.sin(alpha);
            newZ = -v.getX()*Math.sin(alpha)+v.getZ()*Math.cos(alpha);
            v.setX(newX);
            v.setZ(newZ);
        }
    }
    public void rotateZ(double alpha){
        for(Vertex v:vertexList){
            double newX,newY;
            newX = v.getX()*Math.cos(alpha)+v.getY()*Math.sin(alpha);
            newY = -v.getX()*Math.sin(alpha)+v.getY()*Math.cos(alpha);
            //newY = Math.cos(alpha)*Math.sqrt((v.getX()*v.getX()+v.getY()*v.getY()));
           // newX = Math.sin(alpha)*Math.sqrt((v.getX()*v.getX()+v.getY()*v.getY()));
            v.setX(newX);
            v.setY(newY);
        }
    }
    public void translate(double tx,double ty,double tz){
        for(Vertex v:vertexList){
            v.setX(v.getX()+tx);
            v.setY(v.getY()+ty);
            v.setZ(v.getZ()+tz);
        }
    }
    @Override
    public int compareTo(Polygon3D o) {
        int oDepth = o.getDepthValue();
        return (this.getDepthValue()-oDepth);
    }
}
