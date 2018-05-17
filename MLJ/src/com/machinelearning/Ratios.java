package com.machinelearning;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public class Ratios {

    public double entropyOnSubset(Instances inst, ArrayList<Integer> i){
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
}
