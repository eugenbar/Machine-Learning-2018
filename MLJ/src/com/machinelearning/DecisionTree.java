package com.machinelearning;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

import java.util.ArrayList;

public class DecisionTree<D,A> {
    private TreeNode<D,A> treeRoot;
    private int maxDepth=-1;//Default -1: no max depth
    private double rootEntropy;
    //List<>[] Array for every entry. Contains indices of class values classified to one leaf
    //Last Array entry contains index of the correct class value
    private ArrayList<int[]> predictions = new ArrayList<>();

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

    public double entropyOnSubset(Instances inst,ArrayList<Integer>i){
        Instances sa;
        if(i==null){
            sa=new Instances(inst,1);
            sa.addAll(inst);
        }
        else {
            sa=new Instances(inst,1);
            for(int x:i){
                sa.add(inst.instance(x));
            }
        }
        sa.setClass(inst.classAttribute());
        double entropy =0;
        int[]factors = new int[sa.classAttribute().numValues()];
        int n=inst.numInstances();
        for(Instance sainst:sa){
            factors[sa.classAttribute().indexOfValue(sainst.stringValue(sainst.classAttribute()))]+=1;
        }
        for(int x=0;x<factors.length;x++){
            double factor = (double)factors[x]/(double)n;
            if(factor==1 || factor == 0){
                factor=0;
            }
            else {
                entropy+=factor * Math.log10(factor)/Math.log10(2);
            }
        }
        return -entropy;
    }
    public double informationGain(Instances d,ArrayList<Integer>i,Attribute a){
        Instances s = new Instances(d,1);
        double entropy = entropyOnSubset(d,i);
        double gain = 0;
        if(i==null){
            s.addAll(d);
        }
        else {
            for (Integer index : i) {
                s.add(d.instance(index));
            }
        }
        s.setClass(d.classAttribute());
        int n = s.numInstances();
        Instances []sv=new Instances[s.attribute(a.name()).numValues()];
        for(int x = 0;x<sv.length;x++){
            sv[x]=new Instances(s,1);
            sv[x].setClass(s.classAttribute());
        }
        for (Instance inst : s) {
            sv[a.indexOfValue(inst.stringValue(a))].add(inst);
        }

        for (int x=0;x<sv.length;x++) {
            double factor = (double)sv[x].numInstances() / (double) n;
            if (factor == 0 || factor == 1) {
                factor = 0;
            }
            gain+=factor*entropyOnSubset(sv[x],null);


        }
        return entropy-gain;
    }
    public void buildTree(Instances d){
        this.buildTree(d, this.treeRoot);
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
        double maxGain = 0;
        int purestAtt = -1;
        double entropy=0;
        if(node.isRoot()){
            entropy=node.getEntropy();
        }

        for(Attribute attr:attL){
            //System.out.println(attr);
            double igain = informationGain(d,null,attr);
            if(igain>maxGain){
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
    public void printTree(){
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
    public double predict(Instance entry,Instances ins){
        TreeNode<D,A> node=this.treeRoot;
        int correctIndex = entry.classAttribute().indexOfValue(entry.stringValue(entry.classAttribute()));
        while (!node.isLeaf()){
            for(TreeNode<D,A> ch:node.getChildren()) {
                if(ins.attribute((String) node.getD()).equals(ch.getA())){
                    node = ch;
                }
            }
        }
        int max = -1;
        int maxIndex=-1;
        for(int i=0;i<node.getClassCounts().length;i++) {
            if(node.getClassCounts()[i]>max){
                max=node.getClassCounts()[i];
                maxIndex=i;
            }
        }
        //List<>[] Array for every entry. Contains indices of class values classified to one leaf
        //Last Array entry contains index of the correct class value
        this.predictions.add(new int[node.getClassCounts().length+1]);
        for (int i = 0;i<node.getClassCounts().length;i++){
            this.predictions.get(this.predictions.size()-1)[i]=node.getClassCounts()[i];
        }
        this.predictions.get(this.predictions.size()-1)[node.getClassCounts().length]
                = entry.classAttribute().indexOfValue(entry.stringValue(entry.classAttribute()));
        return node.getClassValues()[correctIndex];//entry.classAttribute().value(maxIndex);
    }
    public double predict(Instances instances){
        double accuracy = 0;
        for (Instance i:instances){
            accuracy+=this.predict(i,instances);
        }
        /*
        for (int[]i:this.predictions){
            int sum = 0;
            for(int x = 0;x<i.length-1;x++){
                sum+=i[x];
                System.out.print(i[x]+"|");
            }
            System.out.println(i[i.length-1]);
            accuracy+=(double)i[i.length-1]/(double)sum;
        }*/
        //System.out.println(accuracy);
        //System.out.println(instances.numInstances());
        return accuracy/instances.numInstances();
    }
    public void setMaxDepth(int i){
        this.maxDepth=i;
    }
    public int getMaxDepth(){
        return this.maxDepth;
    }
}
