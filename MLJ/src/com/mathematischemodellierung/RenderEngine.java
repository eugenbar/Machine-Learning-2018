package com.mathematischemodellierung;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class RenderEngine {

    /*protected List<Vertex> vL = new ArrayList<>();
    protected List<Integer> pL = new ArrayList<>();
    protected List<Color> cL = new ArrayList<>();
    protected List<Color> cTL = new ArrayList<>();
    public int width,height;
    public boolean wire = false;
    public Vertex lightSource;
    public double lightSourceScalar;*/
    public RenderEngine(){

    }
    /*public void addV(Vertex v){
        vL.add(v);
    }

    public void addP(int a,int b, int c,Color color){
        pL.add(a);pL.add(b);pL.add(c);
        cL.add(color);
        cTL.add(color);


    }*/

    /*public boolean checkNormal(int p){
        double vx,vy,vz,ux,uy,uz,nx,ny,nz;
        vx = vL.get(pL.get(p+2)).getTx()-vL.get(pL.get(p)).getTx();
        vy = vL.get(pL.get(p+2)).getTy()-vL.get(pL.get(p)).getTy();
        vz = vL.get(pL.get(p+2)).getTz()-vL.get(pL.get(p)).getTz();
        ux = vL.get(pL.get(p+1)).getTx()-vL.get(pL.get(p)).getTx();
        uy = vL.get(pL.get(p+1)).getTy()-vL.get(pL.get(p)).getTy();
        uz = vL.get(pL.get(p+1)).getTz()-vL.get(pL.get(p)).getTz();
        nx = vy*uz-vz*uy;
        ny = vz*ux-vx*uz;
        nz = vx*uy-vy*ux;
        //  nz*=-1;ny*=-1;nx*=-1;
        double nnz=nz;
        double nScalar = Math.sqrt(nx*nx+ny*ny+nz*nz);
        *//*nx += vL.get(pL.get(p+2)).getTx();
        ny += vL.get(pL.get(p+2)).getTy();
        nz += vL.get(pL.get(p+2)).getTz();*//*

        Vertex view = new Vertex(0,0,-200);*//*(vL.get(pL.get(p)).getTx(),vL.get(pL.get(p)).getTy(),
                vL.get(pL.get(p)).getTz()-200);*//*
        double viewS = Math.sqrt((view.getX()*view.getX()+view.getY()*view.getY()+
                view.getZ()*view.getZ()));

        if (((nx*view.getX()+ny*view.getY()+nz*view.getZ())/
                (nScalar*viewS))>0) {
            //nScalar=Math.sqrt(nx*nx+ny*ny+nz*nz);
            Vertex dl = new Vertex(lightSource.getX()-vL.get(pL.get(p)).getTx(),
                    lightSource.getY()-vL.get(pL.get(p)).getTy(),
                    lightSource.getZ()-vL.get(pL.get(p)).getTz());
            double dlS = Math.sqrt(dl.getX()*dl.getX()+dl.getY()*dl.getY()+
                    dl.getZ()*dl.getZ());
            double ang = (((nx*dl.getX()+ny*dl.getY()+
                    nz*dl.getZ())/(dlS*nScalar)));

            Color c = cL.get(p/3);
            ang*=-1;
            if(ang<0)ang=0;
            cTL.set(p / 3, getShade(c,ang));

            return true;

        }
        else return false;
    }
    public void setLightSourceScalar(){
        this.lightSourceScalar = Math.sqrt(lightSource.getX()*lightSource.getX()+
                lightSource.getY()*lightSource.getY()+lightSource.getZ()*lightSource.getZ());
    }*/
    public Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1/2.4);
        int green = (int) Math.pow(greenLinear, 1/2.4);
        int blue = (int) Math.pow(blueLinear, 1/2.4);

        return new Color(red, green, blue);
    }
    public void rasterizePolygon(Polygon p, BufferedImage bf, Color c){
        int[] y =p.ypoints;
        int[] x = p.xpoints;
        int minX,minY,maxX,maxY;

        int yMin =0,yMid=1,yMax=2;
        if(y[1]<y[0])
            yMin = 1;
        if(y[2]<y[yMin]) yMin = 2;
        if(y[(yMin+1)%3]<y[(yMin+2)%3]){
            yMid=(yMin+1)%3;
            yMax=(yMin+2)%3;
        }else {
            yMid=(yMin+2)%3;
            yMax=(yMin+1)%3;
        }
        minX = 0;minY = yMin;maxY=yMax;
        if (x[1] < x[0]) {
            minX = 1;
        }
        if (x[2]<x[minX]){
            minX =2;
        }if(x[(minX+1)%3]>x[(minX+2)%3]){
            maxX = x[(minX+1)%3];
        }else maxX = x[(minX+2)%3];
        /*for(int i=minX;i<maxX;i++){
            for(int j = minY;j<maxY;j++){
                int c1 =
            }
        }*/

        int d1x = x[yMin]-x[yMid],dd1x=d1x;
        int d1y = y[yMin]-y[yMid],dd1y=d1y;
        int d2x = x[yMid]-x[yMax],dd2x=d2x;
        int d2y = y[yMid]-y[yMax],dd2y=d2y;
        int d3x = x[yMin]-x[yMax],dd3x=d3x;
        int d3y = y[yMin]-y[yMax],dd3y=d3y;
        int[]left = new int[y[yMax]+1];
        int[]right = new int[y[yMax]+1];
        for(int i =0;i<y[yMax]+1;i++){
            left[i]=10000;
        }
        int xr = x[yMax],yr=y[yMax];
        right[y[yMax]]=x[yMax];
        left[y[yMax]]=x[yMax];
        while (yr>y[yMin]) {
            if(x[yMin]<=x[yMax]){
                if (dd3x < dd3y) {
                    xr -= 1;
                    dd3x -= d3y;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);

                } else {
                    yr -= 1;
                    dd3y -= d3x;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);
                }
            }else if(x[yMax]<=x[yMin]){
                if (dd3x < -1*dd3y) {
                    xr += 1;
                    dd3x -= d3y;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);

                } else {
                    yr -= 1;
                    dd3y -= d3x;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);
                }
            }
        }

        yr=y[yMax];xr=x[yMax];
        while (yr>y[yMid]){
            if(x[yMid]<=x[yMax]){
                if (dd2x < dd2y) {
                    xr -= 1;
                    dd2x -= d2y;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);

                } else {
                    yr -= 1;
                    dd2y -= d2x;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);
                }
            }else if(x[yMax]<=x[yMid]){
                if (dd2x < -1*dd2y) {
                    xr += 1;
                    dd2x -= d2y;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);

                } else {
                    yr -= 1;
                    dd2y -= d2x;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);
                }
            }
        }
        yr=y[yMid];xr=x[yMid];
        while (yr>y[yMin]){
            if(x[yMin]<=x[yMid]){
                if (dd1x < dd1y) {
                    xr -= 1;
                    dd1x -= d1y;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);

                } else {
                    yr -= 1;
                    dd1y -= d1x;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);
                }
            }else if(x[yMid]<=x[yMin]){
                if (dd1x < -1*dd1y) {
                    xr += 1;
                    dd1x -= d1y;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);

                } else {
                    yr -= 1;
                    dd1y -= d1x;
                    right[yr] = Math.max(xr, right[yr]);
                    left[yr] = Math.min(xr, left[yr]);
                }
            }
        }

        for(int yy = y[yMin];yy<y[yMax]+1;yy++) {
            for (int xx = left[yy]; xx < right[yy]; xx++) {
                if((xx >= 0) && (xx < bf.getWidth()) && (yy >= 0 )&& (yy < bf.getHeight()))
                    bf.setRGB(xx, yy, c.getRGB());

            }
        }
        //g2d.drawImage(bf, 0, 0, width, height, null);
    }

}
