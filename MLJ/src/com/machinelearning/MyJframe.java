package com.machinelearning;

import com.mathematischemodellierung.Object3D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

//Used for Model.plot
public class MyJframe extends JFrame {
    private double lastFpsTime,fps;
    private boolean gameRunning=true;
    private BufferedImage image;
    private int width = 800;
    private int height = 600;
    private int[] pixels;
    public MyJframe(){
        image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        this.setSize(800, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
       // this.gameLoop();

    }

    public void gameLoop()
    {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        // keep looping round til the game ends
        while (gameRunning)
        {
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000)
            {
                // System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            }


            doGameUpdates(delta);


        }
    }

    private void doGameUpdates(double delta)
    {
        Graphics2D g2d = (Graphics2D) image.getGraphics();

       // g2d.drawImage(image,0, 0, getWidth(), getHeight(), null);

        this.paint(g2d);
    }
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(2);
            return;

        }

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        /*for(int x = 300;x<800;x++){
            for(int y = 200;y<500;y++){
            image.setRGB(x,y,Color.RED.getRGB());
            }
        }*/

        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        for(int x=0;x<getWidth();x++){
            for(int y=0;y<getHeight();y++){
                image.setRGB(x,y,Color.WHITE.getRGB());
            }
        }
        //rayList.get(rayList.size()-1).render(g2d,image);
        bs.show();
        g2d.dispose();
    }
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(new Color(255,0,0));
        g2d.fillRect(100,100,200,200);



        g2d.dispose();
        //this.repaint();

    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d =(Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
        doDrawing(g);
    }
}
