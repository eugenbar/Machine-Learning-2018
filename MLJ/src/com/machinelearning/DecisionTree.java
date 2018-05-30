package com.machinelearning;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DecisionTree<D,A> extends Model {
    private TreeNode<D,A> treeRoot;
    private int maxDepth=-1;//Default -1: no max depth
    private double rootEntropy;

    DecisionTree(D decision,A att){
        treeRoot = new TreeNode<D,A>(decision,null);
        treeRoot.setRoot();
    }
    DecisionTree(){
        treeRoot = new TreeNode("root",null);
        treeRoot.setRoot();
    }
    public TreeNode<D,A>getTreeRoot(){
        return this.treeRoot;
    }


    public void build(Instances d){
        treeRoot = new TreeNode("root",null);
        treeRoot.setRoot();
        this.buildTree(d, this.treeRoot);
    }

    @Override
    public BufferedImage getPlotBuffer() {
        ArrayList<TreeNode>nodes = getBFSNodes();
        int maxDepth = nodes.get(nodes.size()-1).getDepth();
        BufferedImage bf;
        int h=maxDepth*200+200,w=(int)(Math.pow(2.0,(double)maxDepth)*100+300);
        System.out.println(w+" ||||| "+h);
        bf = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        for(int x=0;x<w;x++){
            for(int y=0;y<h;y++){
                bf.setRGB(x,y,Color.WHITE.getRGB());
            }
        }
        Graphics2D g2d = (Graphics2D) bf.getGraphics();
        int max =maxDepth+2;
        int counter =1;
        for(int i=nodes.size()-1;i>=0;i--){
            if (nodes.get(i).getDepth()==maxDepth){
                this.addNode(nodes.get(i),counter*100,h-(max-maxDepth)*100,g2d);
                counter++;
            }else {
                counter=1;
                maxDepth--;
                this.addNode(nodes.get(i),counter*100,h-(max-maxDepth)*100,g2d);
                counter++;
            }

        }

        return bf;
    }
    private void addNode(TreeNode node,int x,int y,Graphics2D g2d){
        if (node.getD()=="root")g2d.setColor(Color.RED);
        else if (node.getA()=="leaf")g2d.setColor(Color.GREEN);
        else g2d.setColor(Color.ORANGE);
        g2d.fillRect(x,y,75,75);
        g2d.setColor(Color.BLACK);
        g2d.drawString(node.getD().toString(), x, y+10);
        g2d.drawString(node.getA().toString(), x, y+25);
        DecimalFormat df = new DecimalFormat("#.###");
        g2d.drawString(String.valueOf(df.format(node.getEntropy())), x, y+40);
        g2d.drawString(node.getClassCountsList().toString(), x, y+55);
    }

    public void buildTree(Instances d, TreeNode<D,A> node){
        if(node.isRoot()){//Setting class counts for the root, for the rest, class counts are set when the child is created
            node.setClassCounts(d.attributeStats(d.classIndex()).nominalCounts);
            node.setEntropy(entropyOnSubset(d,null));
        }
        ArrayList<Attribute> attL = new ArrayList<>();
        for(int x=0;x<d.numAttributes();x++){
            if(d.attribute(x)!=d.classAttribute()){
                attL.add(d.attribute(x));
            }
        }
        double maxGain = -1;
        int purestAtt = -1;
        double entropy=0;
        if(node.isRoot()){
            entropy=node.getEntropy();
        }

        for(Attribute attr:attL){
            //System.out.println(attr);
            double igain = informationGain(d,null,attr);
            if(igain>0 && (igain<maxGain || maxGain==-1)){
                maxGain=igain;
                purestAtt = attL.indexOf(attr);
            }
        }
        //No attribute left for splitting, calculating leafs and the tree is complete
        if(purestAtt==-1){

        }
        if (purestAtt>-1) {
          //  System.out.println(attL.get(purestAtt) + "|||" + maxGain + "|||" + attL.size());


//        System.out.println("purest att:"+ d.attribute(purestAtt)+" ###:"+d.attribute(purestAtt).numValues());
            //System.out.println(maxGain);
            node.setA((A) attL.get(purestAtt).name());
            //Splitting data at purest attribute (not the optimal solution on huge data sets)
            Instances[] dataSplit = new Instances[attL.get(purestAtt).numValues()];

            //Adding a child for each attribute value of the purest attribute
            for (int aValues = 0; aValues < attL.get(purestAtt).numValues(); aValues++) {
                node.addChild((D) attL.get(purestAtt).value(aValues), null);
                //Set class attribute for each splitted data set
                dataSplit[aValues] = new Instances(d, 1);
                dataSplit[aValues].setClass(d.classAttribute());
            }

            for (Instance inst : d) {
                //Index of the value of the purest attribute in instance
                int iva = attL.get(purestAtt).indexOfValue(inst.stringValue(attL.get(purestAtt)));

                dataSplit[iva].add(inst);
            }
            //Remove filter, removes attribute at index when applied to instances
            for (int aValues = 0; aValues < attL.get(purestAtt).numValues(); aValues++) {
                dataSplit[aValues].deleteAttributeAt(dataSplit[aValues].attribute(attL.get(purestAtt).name()).index());
            }

            for (TreeNode<D, A> ch : node.getChildren()) {
                // Setting classcounts for the child node
                ch.setClassCounts(dataSplit[attL.get(purestAtt).indexOfValue((String) ch.getD())].attributeStats(
                        dataSplit[attL.get(purestAtt).indexOfValue((String) ch.getD())].classIndex()).nominalCounts);
                ch.setEntropy(entropyOnSubset(dataSplit[attL.get(purestAtt).indexOfValue((String) ch.getD())], null));
                if (ch.getEntropy() > 0 && (node.getDepth()<this.maxDepth || this.maxDepth==-1)) {

                    buildTree(dataSplit[attL.get(purestAtt).indexOfValue((String) ch.getD())],ch);
                }
            }
        }
    }
    public void print(){
        //Call recursive method beginning at the root
        this.printTree(this.treeRoot);
    }
    public void printTree(TreeNode<D,A> node){
        System.out.print(node.getEntropy()+" | /");
        for(int i:node.getClassCounts()){
            System.out.print(i+"/");
        }
        System.out.print(" | "+node.getD()+"/");
        if(node.getA()!=null){
            System.out.println(node.getA());
        }
        else {
            System.out.print("leaf");
            for(int i = 0;i<node.getClassCounts().length;i++){
                System.out.print(node.getClassValues()[i]+"|");
            }
            System.out.println();
        }
        if (node.getChildren()!=null) {
            for (TreeNode<D,A> tn:node.getChildren()) {
                this.printTree(tn);
            }
        };
    }
    protected void predict(Instance entry){
        TreeNode<D,A> node=this.treeRoot;
        int correctIndex = entry.classAttribute().indexOfValue(entry.stringValue(entry.classAttribute()));
        while (!node.isLeaf()){
            for(TreeNode<D,A> ch:node.getChildren()) {
                if(node.isLeaf())break;
                for(int i=0;i<entry.numAttributes();i++) {
                    if (entry.stringValue(entry.attribute(i)).equals(ch.getD())
                            && entry.attribute(i).name().equals(ch.getParent().getA())) {
                        node = ch;
                        break;
                    }
                }
            }
        }
        this.predictions.add(node.getClassCounts());
    }
    public void setMaxDepth(int i){
        this.maxDepth=i;
    }
    public int getMaxDepth(){
        return this.maxDepth;
    }
    public int getTreeDepth(TreeNode node){

            for (int i = 0; i < node.getChildren().size(); i++) {
                return getTreeDepth((TreeNode) node.getChildren().get(i));

            }

        return node.getDepth();
    }
    public int getTreeDepth(){
        return getTreeDepth(treeRoot);
    }
    public ArrayList<TreeNode> getBFSNodes(){
        ArrayList<TreeNode> r=new ArrayList<>();
        ArrayList<TreeNode> ar=new ArrayList<>();
        ar.add(treeRoot);
        while (ar.size()>0){
            TreeNode node=null;
            if(ar.size()-1<0){
                node=null;
            }else {
                node=ar.get(0);
                r.add(node);
                ar.remove(0);
                for (int i = 0; i < node.getChildren().size(); i++) {
                    ar.add((TreeNode) node.getChildren().get(i));
                }
            }
        }
        return r;
    }
}
