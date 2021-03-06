package com.machinelearning;

public class Sheet7 {
    public static double[][] X; // Matrix to contain input values
    public static double[] y; // Vector to contain expected values
    public static MultilayerPerceptron NN; // Neural Network
    public static void main(String[] args) {

        X=new double[4][2]; // Input values
        y=new double[4]; // Target values

        // Instantiate the neural network class
        NN = new MultilayerPerceptron(2,2,1,new SigmoidActivation());

        initialiseDataSet();
        trainNetwork(200000,0.01);
        testNetwork();
    }
    private static void initialiseDataSet(){
        //XOR
        X[0][0] = 0;
        X[0][1] = 0;
        y[0] = 0;

        X[1][0] = 0;
        X[1][1] = 1;
        y[1] = 1;

        X[2][0] = 1;
        X[2][1] = 0;
        y[2] = 1;

        X[3][0] = 1;
        X[3][1] = 1;
        y[3] = 0;
        //A AND NOT B
  /*      X[0][0] = 0;
        X[0][1] = 0;
        y[0] = 0;

        X[1][0] = 0;
        X[1][1] = 1;
        y[1] = 0;

        X[2][0] = 1;
        X[2][1] = 0;
        y[2] = 1;

        X[3][0] = 1;
        X[3][1] = 1;
        y[3] = 0;*/
    }
    private static void trainNetwork(int epochs, double targetError){
        double error=0;
        double baseError=Double.POSITIVE_INFINITY;

        // Iterate over the number of epochs to be completed
        for(int epoch = 0; epoch<epochs; epoch++){
            error=0;

            // Iterate over all training examples
            for(int i = 0; i<X.length; i++){
                // Feed forward
                NN.forwardProp(X[i]);

                // Run backpropagation and add the squared error to the sum of error for this epoch
                error+=NN.backProp(y[i]);

                // update the weights
                NN.updateWeights(0.1);
            }

            // Every 500th epoch check whether progress is too slow and if so, reset the weights
            if(epoch % 500==0) {
                // If not
                if(baseError-error<0.00001) {
                    NN.kick(); // Kick the candidate solution into a new neighbourhood with random restart
                    baseError=Double.POSITIVE_INFINITY;
                }
            else
                baseError=error; // Record the base error
            }

            // Print the sum of squared error for the current epoch to the console
            System.out.println("Epoch: " + epoch + " - Sum of Squared Error: "+ error);

            // If the error is smaller than 0.01 stop training
            if(error<targetError)
            break;
        }

    }
    private static void testNetwork(){
        double correct=0;
        double incorrect=0;
        double output=0;

        // Iterate over the testing examples (which happen to double as training examples)
        for(int i = 0; i<X.length; i++){ output=NN.forwardProp(X[i])[0]; // Feed forward to get the output for the current example // If the output is>= 0.5, we deem the output to be 1.0
            if(output>=0.5)
            output=1.0;
            else // Otherwise it is 0.0
            output=0.0;

            // If the output value matches the target
            if(output==y[i])
                correct++; // Increment the number of successful classifications
            else
                incorrect++; // Increment the number of unsuccessful classifications
        }

        // Print the test accuracy to the console
        System.out.println("Test Accuracy: " + String.valueOf((correct/(incorrect+correct))));
    }
}

