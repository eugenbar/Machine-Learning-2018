package com.machinelearning;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CVParameterSelection;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils;

import java.util.ArrayList;
import java.util.Random;

public class OptimalJ48 {
    private String filename = "diabetes.arff";
    private int classAttributeIndex = 8;
    private int seed = 1;
    private int folds = 10;
    private ArrayList <Double> results = new ArrayList<>();
    private double resultMaxParameter=0;
    private double starts;
    private double steps;
    private double ends;
    OptimalJ48(String filename,int classIndex, double starts, double steps,double ends){
        this.filename=filename;
        this.classAttributeIndex=classIndex;
        this.starts=starts;
        this.steps=steps;
        this.ends=ends;
    }
    public void CVparameterSelection() throws Exception {
        double resultsSum=0;
        double resultMax=0;

        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filename);
        Instances data = source.getDataSet();
        data.setClassIndex(classAttributeIndex);

        Random rand = new Random(seed);
        Instances randData = new Instances(data);
        randData.randomize(rand);
        randData.stratify(folds);
        for (double ii=starts;ii<=ends;ii+=steps) {
            String[] options = new String[2];
            options[0] = "-C";
            options[1] = String.valueOf(ii);

            // perform cross-validation
            Classifier cls = new J48();
            int runs = 10;
            ((J48) cls).setOptions(options);
            for (int i = 0; i < runs; i++) {
                // randomize data
                seed = i + 1;
                rand = new Random(seed);
                randData = new Instances(data);
                randData.randomize(rand);
                if (randData.classAttribute().isNominal())
                    randData.stratify(folds);

                Evaluation eval = new Evaluation(randData);
                for (int n = 0; n < folds; n++) {
                    Instances train = randData.trainCV(folds, n);
                    Instances test = randData.testCV(folds, n);
                    // build and evaluate classifier
                    Classifier clsCopy = cls;//Classifier.makeCopy(cls);
                    clsCopy.buildClassifier(train);
                    eval.evaluateModel(clsCopy, test);
                }
                results.add(eval.pctCorrect());
                resultsSum += eval.pctCorrect();
            }
           // System.out.println(ii+" "+resultsSum / results.size());
            System.out.print(resultsSum/results.size() + ",");
            if(resultsSum/results.size() > resultMax){
                resultMax=resultsSum/results.size();
                resultMaxParameter=ii;
            }
            results = new ArrayList<>();
            resultsSum=0;
        }

        System.out.println(resultMaxParameter+" : "+resultMax);
    }
    public void optimalParameterBuild() throws Exception {
        starts=resultMaxParameter;
        ends=resultMaxParameter;
        CVparameterSelection();
    }
    public static void main(String[] args) throws Exception {



    }
}
