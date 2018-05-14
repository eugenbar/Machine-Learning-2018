package com.machinelearning;
import com.mathematischemodellierung.Visualizer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.Arrays;

public class main {
    public static void main(String[] args) throws Exception {
        String filename = "weather.arff";
        int classAttributeIndex = 4;
        DataSource source = new DataSource(filename);
        Instances instances = source.getDataSet();
       /* ArrayList<Integer> indices;
        Integer[] intArray = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
        indices = new ArrayList<>(Arrays.asList(intArray));*/
        instances.setClassIndex(classAttributeIndex);

        DecisionTree dt = new DecisionTree();

        dt.buildTree(instances);

        //Split in train/test set
        Instances trainSet = new Instances(instances,1);
        Instances testSet = new Instances(instances,1);
        int trainSize = instances.numInstances()*2/3;
        while (trainSet.numInstances()<trainSize){
            int index = (int)(Math.random()*instances.numInstances()-1);
            trainSet.add(instances.instance(index));
            instances.remove(index);
        }
        testSet.addAll(instances);
      //  instances.delete();


        dt.printTree();

        //Testing a single entry
       // System.out.println(testSet.instance(0).stringValue(classAttributeIndex)
       // + "---"+dt.predict(testSet.instance(0)));

        //Testing whole test set
        System.out.println(dt.predict(testSet));
    }
}
