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

        System.out.println("--"+instances.get(instances.size()-1).classValue());
        in2.addAll(instances);
        double[]results = new double[1];
        for(int i=0;i<1;i++) {
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
            dt.setMaxDepth(2);
            dt.build(trainSet);

            dt.print();

            //Testing a single entry
            // System.out.println(testSet.instance(0).stringValue(classAttributeIndex)
            // + "---"+dt.predict(testSet.instance(0)));
            System.out.println();
            //Testing whole test set
            dt.predict(testSet);
            results[i] = dt.getAccuracy(testSet);
            //System.out.println(dt.predict(testSet));
            System.out.println((int)testSet.instance(0).classValue()+ "---"+
                    (int)testSet.instance(1).classValue());
            for(int y=0;y<5;y++){
                System.out.print(testSet.instance(0).stringValue(testSet.attribute(y))+" | ");
            }
            System.out.println();
            for(int y=0;y<5;y++){
                System.out.print(testSet.instance(1).stringValue(testSet.attribute(y))+" | ");
            }
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
