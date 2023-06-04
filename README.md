# NeuralNetworkLib
About: Rudimentary neural network built from scratch in Java that can learn boolean functions via supervised learning. 
Motivation: Wanted to understand what was going on inside the "black box" when using huge NN libraries like tensorflow, pytorch, neuroph etc. Building this for my learning.
Improvements: Not optimized for large trainign sets. Plot error vs epochs graph to show how the error decreases after each epoch.

Specifics: 
- NN represented as a weighted graph. 
- Weights and Bias's randomized such that >-1 and <1.
- Activation Function: Sigmoid
- Loss Function: Mean Square Error 
- Stochastic Gradient Descent
- User can decide on: num of layers, layer size, training set, epochs, learning rate. 
