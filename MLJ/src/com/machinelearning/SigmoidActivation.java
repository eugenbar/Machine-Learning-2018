package com.machinelearning;

public class SigmoidActivation implements iActivation {

    @Override
    public double getDerivative(double x) {

        return (x * (1.0-x));
    }

    @Override
    public double getActivation(double x) {
        double expVal=Math.exp(-x);

        return 1.0/(1.0+expVal);
    }

}