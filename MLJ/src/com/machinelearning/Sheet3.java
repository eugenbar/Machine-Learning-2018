package com.machinelearning;
import com.mathematischemodellierung.Visualizer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.Arrays;
public class Sheet3 {
    Sheet3(){

    }
    public static void main(String[] args) throws Exception {
        String filename = "car.arff";
        int classAttributeIndex = 6;
        DataSource source = new DataSource(filename);
        Instances instances = source.getDataSet();
        instances.setClassIndex(classAttributeIndex);
        ArrayList<DecisionTree> dts = new ArrayList<>();
        double[]results = new double[10];

        Instances trainSet = new Instances(instances, 1);
        Instances testSet = new Instances(instances, 1);
        int trainSize = instances.numInstances() * 2 / 3;
        while (trainSet.numInstances() < trainSize ) {
            int index = (int) (Math.random() * instances.numInstances() - 1);
            trainSet.add(instances.instance(index));
            instances.remove(index);
        }
        testSet.addAll(instances);

        for(int i=0;i<10;i++) {
            Instances sampleTrainSet = new Instances(trainSet,1);
            for(int n = 0;n<trainSize;n++){
                sampleTrainSet.add(trainSet.instance((int)Math.random()*trainSize));
            }
            dts.add(new DecisionTree());
            dts.get(dts.size()-1).buildTree(sampleTrainSet);
        }

        double sum=0;
        for(double d:results){
            sum+=d;
            System.out.print(d+"---");
        }
        System.out.println();
        System.out.println(sum/10);
    }
}
