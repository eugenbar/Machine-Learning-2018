package com.machinelearning;

public interface iActivation {
    double getDerivative(double x);
    double getActivation(double x);
}