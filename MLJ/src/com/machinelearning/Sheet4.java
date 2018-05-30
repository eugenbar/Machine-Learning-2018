package com.machinelearning;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Sheet4 {
    public static void main(String[] args) throws Exception {
        String filename = "car.arff";
        int classAttributeIndex = 6;
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filename);
        Instances instances = source.getDataSet();
        instances.setClassIndex(classAttributeIndex);

        Instances instancesReordered = new Instances(instances,1);
        int instancesSize = instances.numInstances();
        while (instancesReordered.numInstances()<instancesSize){
            int sampleIndex = (int)(Math.random()*(instances.numInstances()-1));
            instancesReordered.add(instances.instance(sampleIndex));
            instances.remove(sampleIndex);
        }

        System.out.println("Our implementation CrossValidation (k=10) on cars: ");
        DecisionTree dt = new DecisionTree();
        dt.stratifiedCrossValidation(instancesReordered,10);
        dt.plotModel();
        System.out.println("Weka J48 CrossValidation (k=10) on cars: ");
        J48Weka j48 = new J48Weka();
        j48.stratifiedCrossValidation(instancesReordered,10);
        System.out.println("Weka RandomForests CrossValidation (k=10) on cars: ");
        RandomForestsWeka rf = new RandomForestsWeka();
        rf.stratifiedCrossValidation(instancesReordered,10);


        filename = "diabetes.arff";
        classAttributeIndex = 8;
        source = new ConverterUtils.DataSource(filename);
        Instances instancesDia = source.getDataSet();
        instancesDia.setClassIndex(classAttributeIndex);

        System.out.println("Weka J48 CrossValidation (k=10) on diabetes: ");
        j48 = new J48Weka();
        j48.stratifiedCrossValidation(instancesDia,10);
        System.out.println("Weka RandomForests CrossValidation (k=10) on diabetes: ");
        rf = new RandomForestsWeka();
        rf.stratifiedCrossValidation(instancesDia,10);

/*
        Classifier cls = new J48();
        cls.buildClassifier(instancesReordered);
        Evaluation eval=new Evaluation(instancesReordered);
        eval.evaluateModel(cls,instancesReordered);
        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
        System.out.println(1-eval.errorRate());
        System.out.println(eval.);*/



    }
}
