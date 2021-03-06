package com.machinelearning;

import com.com.controls.Scrolling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

//Used for Model.plot
public class MyJframe extends Scrolling implements MouseMotionListener {
    private double lastFpsTime,fps;
    private boolean gameRunning=true;
    private BufferedImage image;
    private int width = 1100;
    private int height = 800;
    private int[] pixels;
    private BufferedImage buffer;
    private int x=0,y=0;
    public MyJframe(BufferedImage bf){
        image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setSize(1100, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        buffer =bf;
        this.setVisible(true);
        this.gameLoop();


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
        if (x+width+moving[1]<buffer.getWidth()-50)
            x+=moving[1];
        if (y+height+moving[2]<buffer.getHeight()-50)
            y+=moving[2];
        if (x-moving[3]>0+50)
            x-=moving[3];
        if (y-moving[4]>0+50)
            y-=moving[4];
        if (moving[1]>0 && !acclerationInc[1])moving[1]--;
        if (moving[2]>0 && !acclerationInc[2])moving[2]--;
        if (moving[3]>0 && !acclerationInc[3])moving[3]--;
        if (moving[4]>0 && !acclerationInc[4])moving[4]--;


        //Graphics2D g2d = (Graphics2D) image.getGraphics();

       // g2d.drawImage(image,0, 0, getWidth(), getHeight(), null);
        image= buffer.getSubimage(x,y,width,height);
        //this.paint(g2d);
        render();
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
       /* for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                image.setRGB(x,y,Color.CYAN.getRGB());
            }
        }*/
        //rayList.get(rayList.size()-1).render(g2d,image);
        bs.show();
        g2d.dispose();
    }
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();





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

    @Override
    public void mouseDragged(MouseEvent e) {


    }

    @Override
    public void mouseMoved(MouseEvent e) {


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
