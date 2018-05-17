package com.machinelearning;

import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public abstract class Model extends Ratios {

    //List<>[] Array for every entry. Contains indices of class values classified to one leaf
    //Last Array entry contains index of the correct class value
    // private ArrayList<int[]> predictions = new ArrayList<>();
    protected ArrayList<int[]> predictions;

    protected abstract void predict(Instance instance);

    public abstract void print();

    public abstract void build(Instances d);

    //Makes new predictions and deletes the old ones if any were made before
    public void predict(Instances instances){
        this.predictions = new ArrayList<>();
        for (Instance i:instances){
            this.predict(i);
        }
    }
    public double getAccuracy(Instances instances){
        double acc = 0;
        for(int i = 0;i<instances.size();i++){
            if(getOnlyMaxPrediction(i)==
                    instances.get(i).classValue()){
                acc+=1;
            }
        }
        return acc/instances.numInstances();
    }
    public double getAccuracy(int[] instances){
        double acc = 0;
        for(int i = 0;i<instances.length;i++){
            if(getOnlyMaxPrediction(i)==
                    instances[i]){
                acc+=1;
            }
        }
        return acc/instances.length;
    }
    //Returns only the index of the most probable classification of instance for this model
    public int getOnlyMaxPrediction(int instanceIndex) {
        int max = 0;
        int maxIndex =0;
        for (int i =0;i<this.predictions.get(instanceIndex).length;i++) {
            if (this.predictions.get(instanceIndex)[i] > max ||
                    (this.predictions.get(instanceIndex)[i] == max && (int) Math.random() == 1))
                max = this.predictions.get(instanceIndex)[i];
            maxIndex=i;
        }
        return max;
    }
    public int[] getOnlyMaxPredictions(){
        int[] maxPredictions = new int[this.predictions.size()];
        for (int i=0;i<maxPredictions.length;i++){
            maxPredictions[i]=getOnlyMaxPrediction(i);
        }
        return maxPredictions;
    }
    public ArrayList<int[]> getPredictions(){
        return this.predictions;
    }
    public int[]getPrediction(int instanceIndex){
        return this.predictions.get(instanceIndex);
    }

}
