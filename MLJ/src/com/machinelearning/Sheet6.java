package com.machinelearning;

public class Sheet6 {
    public static void main(String[] args) throws Exception {
        OptimalJ48 oj48 = new OptimalJ48("diabetes.arff",8,0.05,0.01,0.5);
        oj48.CVparameterSelection();
        //oj48.optimalParameterBuild();
    }
}
