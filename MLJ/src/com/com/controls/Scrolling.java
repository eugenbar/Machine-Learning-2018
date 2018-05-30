package com.com.controls;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Scrolling extends JFrame implements KeyListener,MouseListener {
    protected int moving[];
    protected boolean acclerationInc[];//
    protected long lastLoopTime = System.nanoTime();
    protected long updateLength;
    public Scrolling(){
        this.addKeyListener(this);
        this.addMouseListener(this);
        moving=new int[5];
        acclerationInc=new boolean[5];
    }
    protected void updateTimer(){
        long now = System.nanoTime();
        updateLength = now - lastLoopTime;
        lastLoopTime = now;
    }

    @Override
    public void keyTyped(KeyEvent e) {

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

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        System.out.println(e.getExtendedKeyCode());
        System.out.println(e.getKeyChar());
        if (e.getKeyCode()==39){
            moving[1]=(1+moving[1]/2)*2;//right
            acclerationInc[1]=true;acclerationInc[2]=false;acclerationInc[3]=false;acclerationInc[4]=false;
            moving[3]=0;
        }
        if (e.getKeyCode()==40){
            moving[2]=(1+moving[2]/2)*2;//down
            acclerationInc[2]=true;acclerationInc[1]=false;acclerationInc[3]=false;acclerationInc[4]=false;
            moving[4]=0;
        }
        if (e.getKeyCode()==37){
            moving[3]=(1+moving[3]/2)*2;//left
            acclerationInc[3]=true;acclerationInc[2]=false;acclerationInc[1]=false;acclerationInc[4]=false;
            moving[1]=0;
        }
        if (e.getKeyCode()==38){
            moving[4]=(1+moving[4]/2)*2;//up
            acclerationInc[4]=true;acclerationInc[2]=false;acclerationInc[3]=false;acclerationInc[1]=false;
            moving[2]=0;
        }
        if (e.getKeyCode()==32){//Space
            moving[1]=0;moving[2]=0;moving[3]=0;moving[4]=0;
        }

    }
}
