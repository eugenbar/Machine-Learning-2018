package com.machinelearning;

import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

public abstract class Model extends Evaluation {

    //List<>[] Array for every entry. Contains indices of class values classified to one leaf
    //Last Array entry contains index of the correct class value
    // private ArrayList<int[]> predictions = new ArrayList<>();
    protected ArrayList<int[]> predictions;

    protected abstract void predict(Instance instance);

    public abstract void print();

    public abstract void build(Instances d);
    public abstract void plot();

    //Makes new predictions and deletes the old ones if any were made before
    public void predict(Instances instances){
        this.predictions = new ArrayList<>();
        for (Instance i:instances){
            this.predict(i);
        }
    }
    public double getAccuracyRounded(Instances instances){
        double acc = 0;
        for(int i = 0;i<instances.size();i++){
            if(getOnlyMaxPrediction(i)==
                    instances.get(i).classValue()){
                acc+=1;
            }
        }
        return acc/instances.numInstances();
    }
    public double getAccuracy(Instances instances){
        double acc = 0;
        for(int i = 0;i<instances.size();i++){
            acc+= (double)this.getPrediction(i)[(int)instances.get(i).toDoubleArray()[instances.get(i).classIndex()]]/
                    (double)getNumberOfPredictions(i);
        }
        return acc/(double)instances.numInstances();
    }
    public int getNumberOfPredictions(int instanceIndex){
        int sum=0;
        for(int i=0;i<this.predictions.get(instanceIndex).length;i++){
            sum+=this.predictions.get(instanceIndex)[i];
        }
        return sum;
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
            if ((this.predictions.get(instanceIndex)[i] > max) ||
                    (this.predictions.get(instanceIndex)[i] == max && (int) Math.random() == 1)) {
                max = this.predictions.get(instanceIndex)[i];
                maxIndex = i;
            }
        }

        return maxIndex;
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
    public void stratifiedCrossValidation(Instances instances,int k){
        Instances [] trainSet = new Instances[k];
        Instances [] testSet = new Instances[k];
        int testSize = instances.numInstances()/k;
        Instances instancesBak = new Instances(instances,1);
        instancesBak.addAll(instances);
        //Initializing Arrays for k test/train sets
        for(int i=0;i<k;i++){
            testSet[i]= new Instances(instances,1);
            trainSet[i] = new Instances(instances,1);
        }
        for(int i=0;i<k;i++){
            for(int n = 0;n<k;n++){
                //Choosing k instances for testSet i
                testSet[i].add(instancesBak.instance(k*i+n));
                //Removing the choosen testSet i instance n to seperate them from trainSet
                instances.remove(k*i+n);
            }
            //Adding remaining instances to trainSet i, since they were not choosen to be in testSet i
            trainSet[i].addAll(instances);
            //Restoring instances to initial backup value, so that the next k  test instances can be choosen in next iteration
            instances = new Instances(instancesBak,1);
            instances.addAll(instancesBak);
        }

        //Prints all test/train sets
       /* for (int i=0;i<k;i++){
            System.out.println("Test-Set--------------");
            for (Instance in:testSet[i]){
                for (int x=0;x<testSet[i].numAttributes();x++){
                    System.out.print(in.stringValue(in.attribute(x))+" | ");
                }
                System.out.println();
            }
            System.out.println("Train-Set--------------");
            for (Instance in:trainSet[i]){
                for (int x=0;x<trainSet[i].numAttributes();x++){
                    System.out.print(in.stringValue(in.attribute(x))+" | ");
                }
                System.out.println();
            }
        }*/
        double [] acc = new double[k];
        for(int i=0;i<k;i++) {
            this.build(trainSet[i]);
            this.predict(testSet[i]);
            acc[i]+=this.getAccuracy(testSet[i]);
        }
        System.out.println("Mean values for each of k folds: ");
        for(int i=0;i<k;i++){
            System.out.print(acc[i]+" | ");
        }
        System.out.println();
        System.out.println("Mean of means: "+mean(acc));
        System.out.println("Standard deviation of k folds: "+sd(acc));
    }
}
