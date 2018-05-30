package com.machinelearning;

import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

import java.awt.image.BufferedImage;

public class RandomForestsWeka extends Model {
    Classifier cls;
    Evaluation eval;
    RandomForestsWeka(){
    }
    @Override
    protected void predict(Instance instance) {
        try {
            eval = new Evaluation((Instances) instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void predict(Instances instances) {
        try {
            eval = new Evaluation(instances);
            eval.evaluateModel(cls,instances);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public double getAccuracy(Instances instances){
        return 1-eval.errorRate();
    }


    @Override
    public void print() {

    }

    @Override
    public void build(Instances d) {
        try {
            cls = new RandomForest();
            cls.buildClassifier(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getPlotBuffer() {
        return null;
    }


}
