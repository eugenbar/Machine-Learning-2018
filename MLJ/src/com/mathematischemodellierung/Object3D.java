package com.mathematischemodellierung;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Object3D extends RenderEngine{
    protected List<Vertex> vL = new ArrayList<>();
    protected List<Integer> pL = new ArrayList<>();
    protected List<Color> cL = new ArrayList<>();
    protected List<Color> cTL = new ArrayList<>();
    public Vertex lightSource;
    public Color lightColor = new Color(255,0,0);
    public double lightSourceScalar;
    public boolean wire = false;
    public boolean ray = false;
    public Vertex rayT;
    public Vertex rayS;
    public Vertex rayU;
    public List<Vertex> rayQ= new ArrayList<>();
    public List<Vertex> rayBeam;
    public double zn=5;
    private boolean beam=false;

    private double tx,ty,tz,sx,sy,sz,dx,dy,dz;
    public int width,height;
    public double movingX,movingY,movingZ,rotatingX,rotatingY,rotatingZ;
    public Object3D(int width,int height){
        tx=0;ty=0;tz=0;dx=0;dy=0;dz=0;
        //tx+=width/2;ty+=height/2;
        this.width=width;this.height=height;
        sx=1;sy=1;sz=1;
        movingX=0;movingY=0;movingZ=0;rotatingX=0;rotatingY=0;rotatingZ=0;
        lightSource = new Vertex(0,0,1);
        setLightSourceScalar();
        rayS = new Vertex(0,0,-0.1);
        setRay(1,0,-0.1);


    }
    public void setRay(double x, double y, double z){
        rayT = new Vertex(x,y,z);

        rayU = new Vertex(x,y,-z);
    }
    public void addV(Vertex v){
        vL.add(v);
    }

    public void addP(int a,int b, int c,Color color){
        pL.add(a);pL.add(b);pL.add(c);
        cL.add(color);
        cTL.add(color);
        rayQ.add(new Vertex(0,0,-0.1));


    }

    public boolean checkNormal(int p){
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
        //nz*=-1;ny*=-1;nx*=-1;
        double nScalar = Math.sqrt(nx*nx+ny*ny+nz*nz);
        nx = nx/nScalar;
        ny = ny/nScalar;
        nz = nz/nScalar;
        double nnz=nz;

        /*nx += vL.get(pL.get(p+2)).getTx();
        ny += vL.get(pL.get(p+2)).getTy();
        nz += vL.get(pL.get(p+2)).getTz();*/

        Vertex view = new Vertex(0,0,-0.1);/*(vL.get(pL.get(p)).getTx(),vL.get(pL.get(p)).getTy(),
                vL.get(pL.get(p)).getTz()-200);*/
        double viewS = Math.sqrt((view.getX()*view.getX()+view.getY()*view.getY()+
                view.getZ()*view.getZ()));

        if (Math.toDegrees((nx*view.getX()+ny*view.getY()+nz*view.getZ())/
                (viewS))>=0) {
            if(checkRay(nx,ny,nz,p/3)){
                Vertex a = new Vertex(rayQ.get(p/3).getTx()-vL.get(pL.get(p)).getTx(),
                        rayQ.get(p/3).getTy()-vL.get(pL.get(p)).getTy(),
                        rayQ.get(p/3).getTz()-vL.get(pL.get(p)).getTz());
                Vertex b = new Vertex(vx,vy,vz);
                Vertex c = new Vertex(ux,uy,uz);

                if(checkRayPoint(a,b,c)){
                    cTL.set(p/3,Color.GREEN);

                    return true;
                } else {
                    rayQ.set(p/3,new Vertex(0,0,-0.1));
                    cTL.set(p/3,Color.RED);
                    //cTL.set(p/3,cL.get(p/3));
                }

                //return true;

            }


            //nScalar=Math.sqrt(nx*nx+ny*ny+nz*nz);
            Vertex dl = new Vertex(lightSource.getX()-vL.get(pL.get(p)).getTx(),
                    lightSource.getY()-vL.get(pL.get(p)).getTy(),
                    lightSource.getZ()-vL.get(pL.get(p)).getTz());
            double dlS = Math.sqrt(dl.getX()*dl.getX()+dl.getY()*dl.getY()+
                    dl.getZ()*dl.getZ());
            double ang = (((nx*dl.getX()+ny*dl.getY()+
                    nz*dl.getZ())/(dlS)));

            Color col = cL.get(p/3);
            Color mixC = new Color((col.getRed()+lightColor.getRed())/2,
                    (col.getGreen()+lightColor.getGreen())/2,
                    (col.getBlue()+lightColor.getBlue())/2);
           // ang*=-1;
            //System.out.println(Math.toDegrees(ang));
            if(ang<0)ang=0;
            cTL.set(p / 3, getShade(mixC,ang));



            return true;

        }
        else return false;
    }
    public boolean checkRayPoint(Vertex a,Vertex b,Vertex c){
        if(!ray) {
            double nenner;
            double cskalar = c.getTScalar();
            double bskalar = b.getTScalar();
            double cbskalar = scalarT(c,b);
            nenner = cskalar*bskalar-cbskalar*cbskalar;
            double ubeta1 = cskalar/nenner;
            double ubeta2 = cbskalar/nenner;

            Vertex ubeta = new Vertex(b.getTx()*ubeta1-c.getTx()*ubeta2,
                    b.getTy()*ubeta1-c.getTy()*ubeta2,
                    b.getTz()*ubeta1-c.getTz()*ubeta2);
            double beta = scalarT(a,ubeta);
            double ugamma1 = bskalar/nenner;
            double ugamma2 = ubeta2;
            Vertex ugamma = new Vertex(c.getTx()*ugamma1-b.getTx()*ugamma2,
                    c.getTy()*ugamma1-b.getTy()*ugamma2,
                    c.getTz()*ugamma1-b.getTz()*ugamma2);
            double gamma = scalarT(a,ugamma);
            if(beta >= 0 && gamma>=0 && (1-beta-gamma)>=0){

                return true;
            }
        }
        return false;
    }
    public boolean checkRay(double x,double y,double z,int p){
        rayQ.set(p,new Vertex(0,0,-0.1));
        if(!ray) {
            Vertex a = vL.get(pL.get(p));
            double d = x*a.getTx()+y*a.getTy()+z*a.getTz();
            double alpha = (d-a.getTx()*x+a.getTy()*y+a.getTz()*z)/
                    (rayU.getTx()*x+rayU.getTy()*y+rayU.getTz()*z);
            if(alpha>=0){
                //rotatingX=0;
               // rotatingY=0;
              //  rotatingZ=0;

                Vertex q = new Vertex(rayS.getTx()+alpha*rayU.getTx(),
                        rayS.getTy()+alpha*rayU.getTy(),
                        rayS.getTz()+alpha*rayU.getTz());

                rayQ.set(p,q);

                return true;
            }
        }
        return false;
    }
    public void setLightSourceScalar(){
        this.lightSourceScalar = Math.sqrt(lightSource.getX()*lightSource.getX()+
                lightSource.getY()*lightSource.getY()+lightSource.getZ()*lightSource.getZ());
    }


    public void loadFile(String source) throws FileNotFoundException {
        Scanner scanner;
        File f = new File(source);
        scanner = new Scanner(f);
        String s="";
        String[] sA = new String[5];
        sA[0]="";
        while (scanner.hasNextLine() && !s.equals("end_header")){
            s=scanner.nextLine();
        }
        while (scanner.hasNextLine()&& !sA[0].equals("3")){
            s=scanner.nextLine();
            sA = s.split(" ");
            vL.add(new Vertex(Double.parseDouble(sA[0]),
                    Double.parseDouble(sA[1]),
                    Double.parseDouble(sA[2])));
            // System.out.println(vL.get(vL.size()-1).getX());
        }
        Color c = new Color(1f,0f,0f);
        c = Color.BLUE;
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        while (scanner.hasNextLine()){
            //c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
            addP(Integer.parseInt(sA[1]),
                    Integer.parseInt(sA[2]),
                    Integer.parseInt(sA[3]),
                    c);
            //System.out.println(pL.get(pL.size()-3)+" "+pL.get(pL.size()-2)+" "+pL.get(pL.size()-1));
            s=scanner.nextLine();
            sA = s.split(" ");
        }
        addP(Integer.parseInt(sA[1]),
                Integer.parseInt(sA[2]),
                Integer.parseInt(sA[3]),
                c);
        scanner.close();




    }
    public void createRayBeam(){
        beam = true;
        double f = 1/Math.tan(0.5*30*Math.PI/180);
        double ttrX = height*f/(2*zn);//+width*trZ/2;
        double ttrY = height*f/(2*zn);// + height*trZ/2;
        rayBeam = new ArrayList<>();
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                rayBeam.add(new Vertex(((double)i-width/2)*2/width,
                        ((double)j-height/2)*-2/height,-zn));
                rayBeam.get(rayBeam.size()-1).setProjection(
                        (int)(rayBeam.get(rayBeam.size()-1).getX()*ttrX+width/2) ,
                        (int)(rayBeam.get(rayBeam.size()-1).getY()*ttrY+height/2));

            }
        }

    }
    public void renderBeam(Graphics2D g2d){

        for(Vertex r:rayBeam){
            g2d.setColor(new Color((float) Math.random(),(float) Math.random(),(float)Math.random(),1.f));
            g2d.drawLine(width/2,height/2,r.getPx(),r.getPy());
        }
    }
    public void addObject(){

        addV(new Vertex(-50,-50,-50));
        addV(new Vertex(-50,50,-50));
        addV(new Vertex(50,50,-50));
        addV(new Vertex(50,-50,-50));

        addV(new Vertex(-50,-50,50));
        addV(new Vertex(50,-50,50));
        addV(new Vertex(50,50,50));
        addV(new Vertex(-50,50,50));
        Color c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(4,6,7,c);
        addP(4,5,6,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(0,1,2,c);
        addP(2,3,0,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(0,4,7,c);
        addP(7,1,0,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(6,5,3,c);
        addP(3,2,6,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(0,3,5,c);
        addP(5,4,0,c);
        c = new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1.f);
        addP(7,6,1,c);
        addP(1,6,2,c);
        scale(10,10,10);
       translate(0,0,-500);

    }

    private void checkBoundaries() {
        for (Vertex v : vL) {
            if ((v.getTx() ) >= 1 && movingX > 0) {
                movingX *= -1;
            }
            if ((v.getTx() ) <= -1 && movingX < 0) {
                movingX *= -1;
            }
            if ((v.getTy()) > 1 && movingY >= 0) {
                movingY *= -1;
            }
            if ((v.getTy()) <= -1 && movingY < 0) {
                movingY *= -1;
            }
            if ((v.getTz()) >=-5 && movingZ > 0) {
                movingZ *= -1;
            }
            if ((v.getTz()) <= -50 && movingZ < 0) {
                movingZ *= -1;
            }
            /*if((v.getTx())>width && movingX > 0){
                movingX*=-1;
            }
            else if((v.getTx())<0 && movingX < 0){
                movingX*=-1;
            }
            else if((v.getTy())>height && movingY > 0){
                movingY*=-1;
            }
            else if((v.getTy())<0 && movingY < 0){
                movingY*=-1;
            }
            else if((v.getTz())<-3000 && movingZ <0){
                movingZ*=-1;
            }
            else if((v.getTz())>-250 && movingZ >0){
                movingZ*=-1;
            }*//*
        }*/
        }
    }
    public void render(Graphics2D g2d, BufferedImage bf){
        //System.out.println(vL.get(0).getTx()+ " "+vL.get(0).getTy()+" "+
          //      vL.get(0).getTz());
        if(beam){
            renderBeam(g2d);

        }else {
            transformV();
            //System.out.println(vL.get(0).getTx()+ " "+vL.get(0).getTy()+" "+
            //      vL.get(0).getTz());
            Polygon p = new Polygon();
       /* p.addPoint(500,100);
        p.addPoint(300,300);
        p.addPoint(600,500);
        rasterizePolygon(p,bf,Color.CYAN);
        g2d.drawImage(bf,0,0,null);*/
            for (int i = 0; i < pL.size(); i += 3) {
                if (wire) {
                    g2d.setColor(cL.get(i / 3));
                    g2d.drawLine((int) vL.get(pL.get(i)).getPx(),
                            (int) vL.get(pL.get(i)).getPy(),
                            (int) vL.get(pL.get(i + 1)).getPx(),
                            (int) vL.get(pL.get(i + 1)).getPy());
                    g2d.drawLine((int) vL.get(pL.get(i)).getPx(),
                            (int) vL.get(pL.get(i)).getPy(),
                            (int) vL.get(pL.get(i + 2)).getPx(),
                            (int) vL.get(pL.get(i + 2)).getPy());
                    g2d.drawLine((int) vL.get(pL.get(i + 2)).getPx(),
                            (int) vL.get(pL.get(i + 2)).getPy(),
                            (int) vL.get(pL.get(i + 1)).getPx(),
                            (int) vL.get(pL.get(i + 1)).getPy());
                } else if (checkNormal(i) || ray) {
                    p = new Polygon();
                    p.addPoint((int) vL.get(pL.get(i)).getPx(), (int) vL.get(pL.get(i)).getPy());
                    p.addPoint((int) vL.get(pL.get(i + 1)).getPx(), (int) vL.get(pL.get(i + 1)).getPy());
                    p.addPoint((int) vL.get(pL.get(i + 2)).getPx(), (int) vL.get(pL.get(i + 2)).getPy());
                    //rasterizePolygon(p,bf, cTL.get(i / 3));
                    if (ray) g2d.setColor(cL.get(i / 3));
                    else g2d.setColor(cTL.get(i / 3));
                    g2d.fillPolygon(p);
                    //g2d.fillRect(0,0,800,800);
                }

            }
            //g2d.drawImage(bf, 0, 0, width, height, null);
        }

    }



    private void transformV() {
        for (int i = 0; i < vL.size(); i++) {

            double trX = vL.get(i).getX();
            double trY = vL.get(i).getY();
            double trZ = vL.get(i).getZ();
           // System.out.println(vL.get(0).getTx()+ " "+vL.get(0).getTy()+" "+
             //       vL.get(0).getTz());
            trX *= sx;
            trY *= sy;//(1+tz/5000)*//*;
            trZ *= sz;



            double zn = 5;
            double zf = 50;
            double a = (zn+zf)/(zn-zf);
            double b = (2*zf*zn)/(zn-zf);
            double f = 1/Math.tan(0.5*30*Math.PI/180);
            double aspect = width/height;

            double ttrX = trX * Math.cos(dz) + trY * Math.sin(dz);
            double ttrY = -trX * Math.sin(dz) + trY * Math.cos(dz);
            double ttrZ = trZ;
            trX = ttrX;
            trY = ttrY;
            ttrX = trX * Math.cos(dy) - trZ * Math.sin(dy);
            ttrZ = trX * Math.sin(dy) + trZ * Math.cos(dy);


            trX = ttrX;
            trY = ttrY;
            trZ = ttrZ;
          //  ttrZ += tz;

            //ttrX = ttrX / (0.005 * ttrZ);
            //ttrY = ttrY / (0.005 * ttrZ);

           // ttrZ = (ttrZ-1)/ttrZ;


            trZ += tz;
            trX += tx;
            trY += ty;
            double zz = (trZ-1)/-trZ;
            zz=-a+b/-trZ;
            //zz=(zz-1)/zz;

            ttrX = trX*height*f/(2*-trZ)+width/(2);//+width*trZ/2;
            ttrY = -trY*height*f/(2*-trZ)+height/(2);// + height*trZ/2;
           // ttrZ = -a+b/-ttrZ;
           // ttrZ = ttrZ * zzfn+zfn;

           trZ = zz;








            //trX = ttrX;trY=ttrY;
            vL.get(i).setT(trX, trY, trZ);
            vL.get(i).setProjection(Math.round(((float) ttrX)),Math.round((float)ttrY));
            checkBoundaries();
            //int[] tr = {(int) Math.round(ttrX),//+ttrZ
            //      (int) Math.round(ttrY),//+ttrZ
            //    (int) Math.round(ttrZ)};
            //return tr;
        }
    }

    public void scale(double sx,double sy, double sz){

        this.sx*=sx;this.sy*=sy;this.sz*=sz;
    }
    public void translate(double tx,double ty,double tz){
        //checkBoundaries();
        this.tx+=tx;this.ty+=ty;this.tz+=tz;

    }
    public void setNewPosition(double tx,double ty,double tz){
        this.tx = tx;this.ty=ty;this.tz=tz;
    }
    public void rotate(double dx,double dy,double dz){
        this.dx+=dx;this.dy+=dy;this.dz+=dz;
    }
    public double scalarT(Vertex v1,Vertex v2){
        return v1.getTx()*v2.getTx()+v1.getTy()*v2.getTy()+v1.getTz()*v2.getTz();
    }


}
