import java.util.*;

public class DataSet {
    private ArrayList<DataSetRow> dataRow;
    private int inputSize;
    private int outputSize;

    DataSet(int inputSize, int outputSize){
        this.dataRow = new ArrayList<DataSetRow>();
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    @Override
    public String toString(){
        String returnString="";
        for(int i=0; i<this.dataRow.size();i++){
            returnString=returnString+this.dataRow.get(i)+"\n";
        }
        return returnString;
    }

    public void addRow(DataSetRow row) throws Exception{
        if(row.getInput().length!=this.inputSize || row.getOutput().length != outputSize){
            throw new Exception("Row input or output size does not match DataSet's I/O size.");
        }
        else{
            this.dataRow.add(row);
        }
    }

    public ArrayList<DataSetRow> getDataSet(){
        return this.dataRow;
    }

    public int getInputSize(){return this.inputSize;}
    public int getOutputSize(){return this.outputSize;}
}

