# NeuralNetworkLib
About: Rudimentary neural network built from scratch in Java that can learn boolean functions via supervised learning. 

Motivation: Wanted to understand what was going on inside the "black box" when using huge NN libraries like tensorflow, pytorch, neuroph etc. Building this for my learning.

Improvements: Doesn't work on XOR yet. Not optimized for large training sets. Plot error vs epochs graph to show how the error decreases after each epoch.

Specifics: 
- NN represented as a weighted graph. 
- Weights and Bias's randomized such that >-0.1 and <0.1 
- Activation Function: Sigmoid
- Loss Function: Mean Square Error 
- Batch Gradient Descent
- User can decide on: num of layers, layer size, training set, epochs, learning rate. 
