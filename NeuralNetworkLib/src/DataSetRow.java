import java.util.*;
public class DataSetRow {
    private double[]input;
    private double[]output;

    DataSetRow(double[]input, double[]output){
        this.input = input;
        this.output = output;
    }
    @Override
    public String toString(){
        return "input: "+ Arrays.toString(this.input)+", output: "+Arrays.toString(this.output);
    }
    public double[] getInput(){return this.input;}
    public double[] getOutput(){return this.output;}


}
