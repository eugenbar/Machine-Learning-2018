package com.machinelearning;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<D,A> implements Iterable<TreeNode<D,A>> {

    private D decision;
    private A att;
    private TreeNode<D,A> parent;
    private List<TreeNode<D,A>> children;
    private boolean root=false;
    private double entropy=0;



    public TreeNode(D decision,A att) {
        this.decision = decision;
        this.att = att;
        this.children = new LinkedList<TreeNode<D,A>>();
    }


    public TreeNode<D,A> addChild(D childD,A childA) {
        TreeNode<D,A> childNode = new TreeNode<D,A>(childD,childA);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    public TreeNode<D,A> getRoot(){
        if(root){
            return this;
        }
        else {
            return this.parent.getRoot();
        }
    }
    public void setRoot(){
        this.root=true;
    }
    public void setEntropy(double d){
        this.entropy=d;
    }

    public double getEntropy() {
        return entropy;
    }
    public boolean isRoot(){
        return this.root;
    }
    public D getD(){
        if(this.root){
            return (D)"root";
        }
        else {
            return this.decision;
        }
    }
    public A getA(){
        return this.att;
    }
    public TreeNode<D,A>getParent(){
        return parent;
    }
    public List<TreeNode<D,A>>getChildren(){
        return this.children;
    }
    public boolean isLeaf(){
        if(this.getChildren()==null){
            return true;
        }
        else {
            return false;
        }
    }
    public void plot(){
        System.out.println("("+this.decision+", "+this.att+")");

        for(TreeNode<D,A> ch:children){
            ch.plot();
        }
    }
    public void setA(A att){
        this.att=att;
    }
    public int nodeLevel(){
        if(root){
            return 0;
        }
        else {
            return 1+getParent().nodeLevel();
        }
    }

    @Override
    public Iterator<TreeNode<D,A>> iterator() {
        return null;
    }

    // other features ...

}