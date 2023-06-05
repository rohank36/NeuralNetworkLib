import java.util.*;
public class NNLayer {
    private String layerType;
    private ArrayList<Neuron> neuronsInLayer;
    private int layerSize;


    NNLayer(String layerType, int layerSize) throws Exception{
        this.layerType = layerType;
        this.layerSize = layerSize;
        this.neuronsInLayer = new ArrayList<Neuron>(this.layerSize);

        if(this.layerType.equals("output") && this.layerSize != 1 ){
            throw new Exception("Output can only have a size of 1 for logic functions");
        }

        for(int i=0; i<this.layerSize; i++){
            this.neuronsInLayer.add(new Neuron());
        }
    }

    public String getLayerType(){
        return this.layerType;
    }
    public int getLayerSize(){
        return this.layerSize;
    }
    public ArrayList<Neuron> getNeuronsInLayer(){
        return this.neuronsInLayer;
    }
}
