package com.machinelearning;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws Exception {
        String filename = "car.arff";
        int classAttributeIndex = 6;
        DataSource source = new DataSource(filename);
        Instances instances = source.getDataSet();
        instances.setClassIndex(classAttributeIndex);
        Instances in2 = new Instances(instances,1);
        in2.addAll(instances);
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



        DecisionTree dt = new DecisionTree();
        dt.build(trainSet);
        //dt.predict(testSet);

        dt.plotModel();

    }
}
