package com.mathematischemodellierung;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLoop extends GameLoop {
    private World w;
    private BufferedImage bufferedImage;
    private BufferStrategy bufferStrategy;
    private List<Object3D> object3DList = new ArrayList<Object3D>();
    private Object3D bunny;
    private int width,height;

    public MainLoop() throws IOException {
        w = new World();
        w.addKeyListener(this);
        w.addMouseListener(this);
        w.createBufferStrategy(2);
        width = w.getWidth();
        height = w.getHeight();
        width = 1100;
        height = 700;
        bufferedImage = new BufferedImage(w.getWidth(),w.getHeight(),BufferedImage.TYPE_INT_RGB);

        bunny = new Object3D(this.width,this.height);
        bunny.loadFile("src/bun_zipper_res3.ply");
        //bunny.loadFile("src/sphere.ply");
        bunny.scale(2000,2000,2000);
        bunny.translate(0,0,-500);
       // object3DList.add(bunny);
        //addObject();

        /*Object3D obj = new Object3D(width,height);
        obj.addObject();
        object3DList.add(obj);
        */
      //  setGameRunning(true);
        //gameLoop();
    }

    @Override
    public void render() {

        bufferStrategy = w.getBufferStrategy();
        Graphics2D g2d = (Graphics2D)bufferStrategy.getDrawGraphics();
        /*for (int i = 0;i<w.getWidth();i++){
            for(int j = 0;j<w.getHeight();j++){
                bufferedImage.setRGB(i,j,Color.DARK_GRAY.getRGB());
            }
        }*/
        for(Object3D obj:object3DList){
            obj.render(g2d,bufferedImage);
        }
        g2d.setColor(Color.RED);

        g2d.drawString("FPS: "+String.valueOf(getLastFps()),0,50);
        //g2d.drawImage(bufferedImage,0,0,width,height,null);
        bufferStrategy.show();
        g2d.dispose();
    }

    @Override
    public void doGameUpdates(double delta) {
        /*for(Object3D obj:object3DList) {
            obj.rotate(0, 0.1 * delta * obj.rotatingY, 0.1 * delta * obj.rotatingZ);
            obj.translate(0.1 * delta * obj.movingX, 0.1 * delta * obj.movingY, 0.1 * delta * obj.movingZ);
        }*/

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
