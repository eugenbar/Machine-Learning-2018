package com.machinelearning;

import java.util.*;

public class TreeNode<D,A> implements Iterable<TreeNode<D,A>> {

    private D decision;
    private A att;
    private int[] classCounts;
    private double[] classValues;
    private TreeNode<D,A> parent;
    private List<TreeNode<D,A>> children;
    private boolean root=false;
    private double entropy=0;
    private int depth = 0;



    public TreeNode(D decision,A att) {
        this.decision = decision;
        this.att = att;
        this.children = new ArrayList<>();
    }


    public TreeNode<D,A> addChild(D d,A a) {
        TreeNode<D,A> childNode = new TreeNode<D,A>(d,a);
        childNode.parent = this;
        childNode.setDepth(this.depth+1);
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
        if (att==null)return (A) "leaf";
        else return this.att;
    }
    public TreeNode<D,A>getParent(){
        return parent;
    }
    public List<TreeNode<D,A>>getChildren(){
        return this.children;
    }
    public boolean isLeaf(){
        if(this.getChildren()==null ||this.getChildren().isEmpty()){
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

    public D getDecision() {
        return decision;
    }

    public void setDecision(D decision) {
        this.decision = decision;
    }

    public A getAtt() {
        return att;
    }

    public void setAtt(A att) {
        this.att = att;
    }

    public int[] getClassCounts() {
        return classCounts;
    }
    public void setClassCounts(int[] classCounts) {
        this.classCounts=classCounts;
        int sum = 0;
        double[]cv = new double[classCounts.length];
        for(int i=0;i<classCounts.length;i++){
            cv[i] = classCounts[i];
            sum+=classCounts[i];
        }
        for (int i=0;i<cv.length;i++){
            cv[i] = cv[i]/sum;
        }
        this.classValues = cv;
    }

    public void setParent(TreeNode<D, A> parent) {
        this.parent = parent;
    }

    public void setChildren(List<TreeNode<D, A>> children) {
        this.children = children;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public double[] getClassValues() {
        return classValues;
    }

    public ArrayList<Integer> getClassCountsList(){
        ArrayList<Integer> l = new ArrayList<Integer>();
        for(int i:this.classCounts){
            l.add(i);
        }
        return l;
    }

    @Override
    public Iterator<TreeNode<D,A>> iterator() {
        return null;
    }

    // other features ...

}