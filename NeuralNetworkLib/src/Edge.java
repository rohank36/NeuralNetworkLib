import java.lang.reflect.Array;
import java.util.*;
public class Edge {
    private Neuron src;
    private Neuron dest;
    private double weight;

    private int srcLayer;

    //private double deltaW=0.0;

    public ArrayList<Double> arrDeltaW = new ArrayList<Double>(); //BATCH GRADIENT LEARNING
    public ArrayList<Double> arrDeltaWAndPrevA = new ArrayList<Double>();//BATCH GRADIENT LEARNING


    Edge(int srcLayer, Neuron src, Neuron dest){
        Random random = new Random();
        double weight = -0.1 + (random.nextDouble()*0.2);
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.srcLayer = srcLayer;
    }


    @Override
    public String toString(){
        return "SrcLayer:"+this.srcLayer+" "+this.src+"("+this.src.getActivation()+") --> "+this.dest+"("+this.dest.getActivation()+"): "+this.weight;
    }


    public int getSrcLayer(){return this.srcLayer;}

    public Neuron getSrc(){
        return this.src;
    }
    public Neuron getDest(){
        return this.dest;
    }
    public double getWeight(){
        return this.weight;
    }
    public void setWeight(double newWeight){this.weight = newWeight;}

    /*
    public void addDeltaW(double x){
        this.deltaW+=x;
    }
    public double getDeltaW(){
        return this.deltaW;
    }

    public void setDeltaW(double x){
        this.deltaW = x;
    }

     */

    public String displayWeightAdjustment(double adjustment){
        return "SrcLayer:"+this.srcLayer+" "+this.src+" --> "+this.dest+" Weight Adjustment: "+adjustment;
    }

}
