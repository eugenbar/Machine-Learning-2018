package com.machinelearning;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public class DecisionTree<D,A> {
    private TreeNode<D,A> treeRoot;
    private int maxDepth;
    private double rootEntropy;

    DecisionTree(D decision,A att,Instances d,ArrayList<Integer>i,Attribute classAtt){
        treeRoot = new TreeNode<D,A>(decision,att);
        maxDepth = 0;
        treeRoot.setRoot();
        rootEntropy=(entropyOnSubset(d,i,classAtt));
        treeRoot.setEntropy(rootEntropy);
    }
    DecisionTree(Instances d,ArrayList<Integer>i,Attribute classAtt){
        treeRoot = new TreeNode("root",null);
        maxDepth = 0;
        treeRoot.setEntropy(entropyOnSubset(d,i,classAtt));
    }
    public TreeNode<D,A>getTreeRoot(){
        return this.treeRoot;
    }
    public double entropyOnSubset(Instances d, ArrayList<Integer> i, Attribute classAtt){
        Instances s = new Instances(d,1);
        if(i==null){
            s.addAll(d);
        }
        else {
            for (Integer index : i) {
                s.add(d.instance(index));
            }
        }
        double entropy=0;
        int n = s.numInstances();
        for(int aValues=0;aValues<s.attributeStats(classAtt.index()).distinctCount;aValues++) {
            double factor = (double)s.attributeStats(classAtt.index()).nominalCounts[aValues]/(double)n;
            if((factor!=0)&&(factor!=1))entropy-=factor*Math.log10(factor)/Math.log10(2);

        }
        return entropy;
    }
    public double informationGain(Instances d,ArrayList<Integer>i,Attribute classAtt,Attribute a){
        Instances s = new Instances(d,1);
        //double entropy = entropyOnSubset(d,i,classAtt);
        double gain = 0;
        if(i==null){
            s.addAll(d);
        }
        else {
            for (Integer index : i) {
                s.add(d.instance(index));
            }
        }
        int n = s.numInstances();
        for(int aValues=0;aValues<s.attributeStats(a.index()).distinctCount;aValues++){

            Instances sv = new Instances(s,1);
            int v = 0;
            ArrayList<Integer>indices = new ArrayList<>();
            for(Instance inst:s){
                if(inst.stringValue(a).equals(a.value(aValues))){
                    indices.add(v);
                    sv.add(inst);
                }
                v+=1;
            }
            int nv = indices.size();
            gain+=((double)nv/(double)n)*entropyOnSubset(s,indices,classAtt);
        }
        return /*entropy-*/gain;
    }
    public void buildTree(Instances d,ArrayList<Integer>i, Attribute classAtt,TreeNode<String,String> node){
        System.out.println(i);
        double maxGain = 0;
        int purestAtt = -1;
        double entropy=0;
        if(node.isRoot()){
            entropy=node.getEntropy();
        }

        for(int attIndex=0;attIndex<d.numAttributes();attIndex++){
            double igain = informationGain(d,i,classAtt,d.attribute(attIndex));
            if(((igain < maxGain)||(maxGain==0))&&(igain!=0)){
                maxGain=igain;
                purestAtt = attIndex;
            }
        }
        //System.out.println(maxGain);
        node.setA(d.attribute(purestAtt).name());
        //Splitting data at purest attribute (not the optimal solution on huge data sets)
        Instances []dataSplit = new Instances[d.attribute(purestAtt).numValues()];

        //Adding a child for each attribute value of the purest attribute
        for(int aValues=0;aValues<d.attribute(purestAtt).numValues();aValues++){
            node.addChild(d.attribute(purestAtt).value(aValues),null);
            //Set class attribute for each splitted data set
            dataSplit[aValues] = new Instances(d,1);
            dataSplit[aValues].setClass(d.classAttribute());
        }
        /*for (TreeNode<String,String>ch:node.getChildren()){
            ch.setEntropy(node.getEntropy()-maxGain);
        }*/

        for(Instance inst:d){
            //Index of the value of the purest attribute in instance
            int iva = d.attribute(purestAtt).indexOfValue(inst.stringValue(purestAtt));
            dataSplit[iva].add(inst);
        }

     /*   for(int aValues=0;aValues<d.attribute(purestAtt).numValues();aValues++) {
            System.out.print(dataSplit[aValues] + " --- " );
        }*/
        System.out.println(" --- "+maxGain);
        if((maxGain)>0){
            for (TreeNode<String,String> ch:node.getChildren()) {
                buildTree(dataSplit[d.attribute(purestAtt).indexOfValue(ch.getD())],
                        null, d.classAttribute(), ch);
            }
        }
    }
}
