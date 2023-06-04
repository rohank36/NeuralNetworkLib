import java.util.*;
public class Neuron {
    private double activation=0.0;
    private double bias;

    private double deltaB=0.0;
    private double neuronError = 0.0;

    Neuron(){
        Random random = new Random();
        double randomValue = -1 + (random.nextDouble()*2);
        this.bias = randomValue;
    }


    public void setBias(double bias){
        this.bias = bias;
    }
    public double getBias(){
        return this.bias;
    }



    public void setActivation(double activation_num){
        this.activation = activation_num;
    }

    public double getActivation(){
        return this.activation;
    }

    public void addDeltaB(double x){
        this.deltaB += x;
    }

    public void setDeltaB(double x){
        this.deltaB = x;
    }

    public double getDeltaB(){
        return this.deltaB;
    }

    public void setNeuronError(double x){
        this.neuronError = x;
    }
    public double getNeuronError(){
        return this.neuronError;
    }


}
