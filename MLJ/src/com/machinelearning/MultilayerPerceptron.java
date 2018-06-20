package com.machinelearning;

import java.util.Random;

public class MultilayerPerceptron {
    private int inCount;
    private int hiddenCount;
    private int outCount;
    private double[][]weights1;
    private double[][]weights2;
    private double[][]deltaWeights1;
    private double[][]deltaWeights2;
    private double[]preActivation1;
    private double[]preActivation2;
    private iActivation activation = null;
    private double[]inValues;
    private double[]outValues;
    private double[]hiddenValues;
    public MultilayerPerceptron(int inCount, int hiddenCount ,int outCount, iActivation activation){
        this.inCount=inCount;
        this.hiddenCount=hiddenCount;
        this.outCount=outCount;
        this.activation=activation;

        // Initialise the first layer weight and delta weight matrices (accounting for bias unit)
        weights1 = initWeights(new double[hiddenCount][inCount+1]);
        deltaWeights1 =initDeltaWeights(new double[hiddenCount][inCount+1]);

        // Initialise the second layer weight and delta weight matrices (accounting for bias unit)
        weights2 = initWeights(new double[outCount][hiddenCount+1]);
        deltaWeights2 = initDeltaWeights(new double[outCount][hiddenCount+1]);

        // Initialise the activation vectors
        preActivation1 = initActivations(new double[hiddenCount]);
        preActivation2 = initActivations(new double[outCount]);

        // Initialise the hidden and output value vectors (same dimensions as activation vectors)
        outValues=preActivation2.clone();
        hiddenValues=preActivation1.clone();
    }
    private double[][] initWeights(double w[][]){
        Random rn = new Random();

        double offset = 1/(Math.sqrt(inCount));

        for(int i=0; i<w.length; i++){
            for(int j=0; j<w[i].length;j++){ // No bias unit
                w[i][j]=offset-rn.nextDouble();
            }
        }
        return w;
    }

    private double[][] initDeltaWeights(double w[][]){

        for(int i=0; i<w.length; i++){
            for(int j=0; j<w[i].length;j++){
                w[i][j]=0;
            }
        }
        return w;
    }

    private double[] initDelta(double d[]){
        for(int i=0; i<d.length; i++){
            d[i]=0;
        }

        return d;
    }

    private double[] initActivations(double z[]){
        for(int i=0; i<z.length; i++){
            z[i]=0;
        }
        return z;
    }

    public void kick(){
        // Kick the candidate solution into a new neighbourhood (random restart)
        weights2 = initWeights(new double[outCount][hiddenCount+1]); // Account for bias unit
        weights1 = initWeights(new double[hiddenCount][inCount+1]); // Account for bias unit
    }
    public double[] forwardProp(double[] inputs){
        this.inValues = new double[(inputs.length+1)];

        // Add bias unit to inputs
        inValues[0]=1;
        for(int i = 1; i<inValues.length; i++){
            this.inValues[i]=inputs[i-1];
        }

        hiddenValues = new double[hiddenCount+1];
        hiddenValues[0]=1; // Add bias unit

        // Get hidden layer activations
        for(int i = 0; i<preActivation1.length;i++){
            preActivation1[i]=0;
            for(int j = 0; j<inValues.length; j++){
                // Hidden Layer Activation
                preActivation1[i]+=weights1[i][j] * inValues[j];
            }

            // Hidden Layer Output value
            hiddenValues[i+1]= activation.getActivation(preActivation1[i]);
        }

        // Get output layer activations
        for(int i = 0; i<preActivation2.length;i++){
            preActivation2[i]=0;
            for(int j=0; j<hiddenValues.length; j++){
                // Get output layer Activation
                preActivation2[i] += weights2[i][j] * hiddenValues[j];
            }
            // Get output layer output Value
            outValues[i]=activation.getActivation(preActivation2[i]);
        }

        return outValues;
    }
    // Support a single double value as the target
    public double backProp(double targets){
        double[] t = new double[1];
        t[0]=targets;
        return backProp(t);
    }

    public double backProp(double[] targets){
        double error=0;
        double errorSum=0;
        double[] d1; // First Layer Deltas
        double[] d2; // Second Layer Deltas
        d1 = initDelta(new double[hiddenCount]);
        d2 = initDelta(new double[outCount]);

        // Calculate Deltas for the second layer and the error
        for(int i = 0;i<d2.length; i++){
            d2[i]=(targets[i]-outValues[i]) * activation.getDerivative(outValues[i]);

            errorSum+= Math.pow((targets[i]-outValues[i]),2); // Squared Error
        }
        error = errorSum / 2;

        // Update Delta Weights for second layer
        for(int i = 0; i<outCount; i++){
            for(int j = 0; j<hiddenCount+1; j++){
                deltaWeights2[i][j] += d2[i] * hiddenValues[j];
            }
        }

        // Calculate Deltas for first layer of weights
        for(int j = 0; j<hiddenCount; j++){
            for(int k = 0; k<outCount; k++){
                d1[j] += (d2[k] * weights2[k][j+1]) * activation.getDerivative(hiddenValues[j+1]);
            }
        }

        // Update first layer deltas
        for(int i=0; i<hiddenCount;i++){
            for(int j=0; j<inCount+1; j++){ // Account for bias unit
                deltaWeights1[i][j] += d1[i] * inValues[j];

            }
        }
        return error;
    }
    public void updateWeights(double learningRate){
        // Update first layer of weights
        for(int i = 0; i<weights2.length; i++){
            for(int j = 0; j<weights2[i].length; j++){
                weights2[i][j] += learningRate * deltaWeights2[i][j];
            }
        }

        // Update second layer of weights
        for(int i = 0; i<weights1.length; i++){
            for(int j = 0; j<weights1[i].length; j++){
                weights1[i][j] += learningRate * deltaWeights1[i][j];
            }
        }

        // Reset delta weights
        deltaWeights1 = initDeltaWeights(new double[hiddenCount][inCount+1]);
        deltaWeights2 = initDeltaWeights(new double[outCount][hiddenCount+1]);
    }


}
