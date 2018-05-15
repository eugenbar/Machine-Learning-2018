package com.machinelearning;
import com.mathematischemodellierung.Visualizer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.Arrays;

public class main {
    public static void main(String[] args) throws Exception {
        String filename = "car.arff";
        int classAttributeIndex = 6;
        DataSource source = new DataSource(filename);
        Instances instances = source.getDataSet();
        instances.setClassIndex(classAttributeIndex);
        Instances in2 = new Instances(instances,1);
        in2.addAll(instances);
        double[]results = new double[10];
        for(int i=0;i<10;i++) {
            System.out.println("-------------------------");
            instances.delete();
            instances.addAll(in2);
            //Split in train/test set
            Instances trainSet = new Instances(instances, 1);
            Instances testSet = new Instances(instances, 1);
            int trainSize = instances.numInstances() * 2 / 3;
            while (trainSet.numInstances() < trainSize ) {
                int index = (int) (Math.random() * instances.numInstances() - 1);
                trainSet.add(instances.instance(index));
                instances.remove(index);
            }
            testSet.addAll(instances);
            //  instances.delete();
            DecisionTree dt = new DecisionTree();
            dt.setMaxDepth(4);
            dt.buildTree(trainSet);

            dt.printTree();

            //Testing a single entry
            // System.out.println(testSet.instance(0).stringValue(classAttributeIndex)
            // + "---"+dt.predict(testSet.instance(0)));
            System.out.println();
            //Testing whole test set
            results[i] = dt.predict(testSet);
            //System.out.println(dt.predict(testSet));

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
