import java.util.*;
public class Main {
    public static void main(String[] args) throws Exception {
        // Project:
        // Building a multi layer perceptron library from scratch that can learn logic functions via supervised learning
        // This neural network will be able to learn any basic logic (or, and, xor, nor, nand etc)

        // Motivation:
        // When looking at all these huge NN libraries  (tensorflow, pytorch, neuroph etc). I always wanted to know
        // how these NN's truly worked, I wanted to understand what was going on inside the "black box". So I built this
        // for my learning.

        // Improvements:
        // Not optimized for large training sets.
        // Plot error vs epochs graph to show how the error decreases after each epoch.

        NN neuralNetwork = new NN(2,1,2,1, true); //Optimal Hyper Parameters for XOR

        DataSet trainingSet = new DataSet(2,1);
        trainingSet.addRow(new DataSetRow(new double[]{0,0},new double[]{0}));
        trainingSet.addRow(new DataSetRow(new double[]{0,1},new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1,0},new double[]{1}));
        trainingSet.addRow(new DataSetRow(new double[]{1,1},new double[]{0}));
        //System.out.println(trainingSet)

        neuralNetwork.train(trainingSet,10000,false, true,0.1);

        neuralNetwork.test(trainingSet);

    }

}
