import java.util.*;

public class NN {
    private ArrayList<NNLayer> layers;
    private ArrayList<LinkedList<Edge>> adjList;
    private int NNSize=0;
    int numOfHiddenLayers;

    NN(int inputLayerSize, int numOfHiddenLayers, int hiddenLayerSize, int outputLayerSize, boolean displayNN) throws Exception {
        this.numOfHiddenLayers = numOfHiddenLayers;

        ArrayList<NNLayer> NNLayers = new ArrayList<NNLayer>();
        NNLayers.add(new NNLayer("input",inputLayerSize));

        for(int i=0; i<numOfHiddenLayers; i++){
            NNLayers.add(new NNLayer("hidden",hiddenLayerSize));
        }
        NNLayers.add(new NNLayer("output",outputLayerSize));

        this.layers = NNLayers;

        for(int i=0; i<layers.size(); i++){
            this.NNSize += layers.get(i).getLayerSize();
        }

        this.adjList = new ArrayList<LinkedList<Edge>>(this.NNSize);


        Random random = new Random();
        int NeuronPositionCounter = 0;

        for(int j=0; j<layers.size()-1;j++){ //cycle through layers: [input,last hidden layer]
            for(int n=0;n<layers.get(j).getLayerSize();n++){ // layer n, Neuron src
                this.adjList.add(new LinkedList<>());
                for(int m=0; m<layers.get(j+1).getLayerSize(); m++){ //layer n+1, Neuron dest
                    this.adjList.get(NeuronPositionCounter).add(new Edge(j,layers.get(j).getNeuronsInLayer().get(n),layers.get(j+1).getNeuronsInLayer().get(m)));
                }
                NeuronPositionCounter++;
            }
        }

        if(displayNN){
            System.out.println(this);
        }

    }

    public ArrayList<NNLayer> getLayers(){return this.layers;}
    public ArrayList<LinkedList<Edge>> getAdjList(){return this.adjList;}
    public int getNNSize(){return this.NNSize;}


    @Override
    public String toString(){
        String returnString = "";
        for(int i=0; i< layers.size(); i++){
            returnString=returnString+this.layers.get(i).getLayerType()+": "+this.layers.get(i).getNeuronsInLayer()+"\n";
        }

        returnString=returnString+"NN Edges: "+this.adjList+"\n";

        returnString=returnString+"NN Size: "+this.NNSize;

        return returnString;
    }



    public double sigmoid(double x){ return (double)1/(double)(1+Math.exp(-x));}
    public double relu(double x){return Math.max(0,x);}

    public double calculateActivation(Neuron neuron, NNLayer layerOfNeuron){
        double sum = 0.0;
        for(int i=0; i<this.adjList.size(); i++){
           for(int j=0; j<this.adjList.get(i).size(); j++){
                if(this.adjList.get(i).get(j).getDest() == neuron){
                    sum+= this.adjList.get(i).get(j).getSrc().getActivation() * this.adjList.get(i).get(j).getWeight();
                }
           }
        }
        sum+=neuron.getBias();
        return sigmoid(sum);
    }

    public void displayEdges(boolean isFwdPropagation, int propagation){
        if(isFwdPropagation){
            System.out.println("Fwd Propagation #"+propagation+": "+this.adjList);
        }
        else{
            System.out.println("Bwd Propagation #"+propagation+": "+this.adjList);
        }
    }

    public void train(DataSet trainingSet, int epochs, boolean showLearning, boolean showError, double learningRate){
        int fwdPropagationCounter = 0;
        System.out.println("Training...");
        for(int count=0; count<epochs; count++){
            double error = 0.0;
            double totalError = 0.0;
            for(int row=0; row<trainingSet.getDataSet().size(); row++){

                //Set input
                int inputCounter =0;
                for(Neuron neuron : this.layers.get(0).getNeuronsInLayer()){
                     neuron.setActivation(trainingSet.getDataSet().get(row).getInput()[inputCounter]);
                     inputCounter++;
                }

                //Forward Pass
                forwardPropagation();
                fwdPropagationCounter++;
                if(showLearning){
                    this.displayEdges(true,fwdPropagationCounter);
                }
                double labelOutput = trainingSet.getDataSet().get(row).getOutput()[0];
                double nnOuput = this.layers.get(this.layers.size()-1).getNeuronsInLayer().get(0).getActivation();
                error = Math.pow(nnOuput-labelOutput,2); //Cost Function = Mean Squared Error
                totalError += error;

                if(showError && count%1000 == 0){
                    System.out.println(count+"th Epoch nnOutput: "+nnOuput+" desiredOutput: "+labelOutput);
                }

                //Backwards Pass
                backwardPropagation(labelOutput,row);

            }

            //Gradient Descent
            gradientDescent(learningRate, showLearning,trainingSet.getDataSet().size());//BATCH GRADIENT LEARNING

            if(showError && count%1000 == 0){
                System.out.println(count+"th Epoch Error: "+totalError/trainingSet.getDataSet().size());
            }

        }

        System.out.println("Finished Training.");
    }


    public void forwardPropagation(){
        //loop hidden and output layer(s) to propagate fwd
        for(int indexLayer=1; indexLayer<this.layers.size();indexLayer++){
            for(Neuron neuron : this.layers.get(indexLayer).getNeuronsInLayer()){
                neuron.setActivation(calculateActivation(neuron,this.layers.get(indexLayer)));
            }
        }
    }


    public void backwardPropagation(double output, int row){
        for(int indexLayer=this.layers.size()-1; indexLayer>0; indexLayer--){ //Loop backwards through layers starting at the output layer
            for(Neuron neuronJ : this.layers.get(indexLayer).getNeuronsInLayer()){ //get neurons in layer
                for(int i=0; i<this.adjList.size(); i++){ //Access correct edge and calculate the deltas
                    for(int j=0; j<this.adjList.get(i).size(); j++){

                        if(this.adjList.get(i).get(j).getDest() == neuronJ){
                            double delta = calculateDelta(this.layers.get(indexLayer),this.adjList.get(i).get(j),output,row);
                            //neuronJ.setNeuronError(delta);
                            //neuronJ.setDeltaB(delta);
                            //this.adjList.get(i).get(j).setDeltaW(delta);

                            neuronJ.arrDeltaB.add(delta);
                            this.adjList.get(i).get(j).arrDeltaW.add(delta);
                            this.adjList.get(i).get(j).arrDeltaWAndPrevA.add(this.adjList.get(i).get(j).getSrc().getActivation()*delta);

                        }
                    }
                }

            }
        }
    }

    public double calculateDelta(NNLayer layer, Edge edge, double desiredOutput, int row){
        double prevNeuronActivation = edge.getSrc().getActivation();
        double curNeuronBias = edge.getDest().getBias();
        double curNeuronActivation = edge.getDest().getActivation();
        if(layer.getLayerType().equals("output")){
            return derivativeSigmoid(edge.getWeight()*prevNeuronActivation+curNeuronBias) * (curNeuronActivation - desiredOutput);
        }
        else{
            return derivativeSigmoid(edge.getWeight()*prevNeuronActivation+curNeuronBias) * sum(edge.getDest(),row);
        }
    }

    public double sum(Neuron curNeuron, int row){
        double total = 0;
        for(int i=0; i<this.adjList.size(); i++){
            for(int j=0; j<this.adjList.get(i).size(); j++){
                if(this.adjList.get(i).get(j).getSrc() == curNeuron){
                    //total += this.adjList.get(i).get(j).getWeight() * this.adjList.get(i).get(j).getDeltaW();
                    total += this.adjList.get(i).get(j).getWeight() * this.adjList.get(i).get(j).arrDeltaW.get(row); //BATCH GRADIENT LEARNING
                }
            }
        }
        return total;
    }

    public double derivativeSigmoid(double x){
        double sigmoid = sigmoid(x);
        return sigmoid * (1 - sigmoid);
    }

    public double derivativeReLU(double x){
        return x > 0 ? 1 : 0;
    }

    public void gradientDescent(double learningRate, boolean showLearning,double trainingSetSize){
        //loop through all neurons, set the Bias and reset the neuronError
        for(int layer=1;layer<this.layers.size();layer++){
            for(Neuron neuron : this.layers.get(layer).getNeuronsInLayer()){

                double totalBias = 0.0;
                for(double b : neuron.arrDeltaB){
                    totalBias+=b;
                }
                double biasGradient = (learningRate*(totalBias/trainingSetSize));
                //double biasGradient = (learningRate*(neuron.getDeltaB()));
                neuron.setBias(neuron.getBias()-biasGradient);
                //neuron.setNeuronError(0.0);
                //neuron.setDeltaB(0.0);
                neuron.arrDeltaB.clear();
            }

        }
        //loop through all edges, set the new weight and reset deltaW value
        for(int i=0; i<this.adjList.size(); i++){
            for(int j=0; j<this.adjList.get(i).size(); j++){
                Edge curEdge = this.adjList.get(i).get(j);

                double totalWeight = 0.0;
                for(double w : curEdge.arrDeltaWAndPrevA){
                    totalWeight+=w;
                }
                double weightGradient = (learningRate*(totalWeight/trainingSetSize));
                //double weightGradient = (learningRate*(curEdge.getDeltaW()*curEdge.getSrc().getActivation()));
                curEdge.setWeight(curEdge.getWeight()-weightGradient);
                curEdge.arrDeltaW.clear(); //DELETE LATER
                //curEdge.setDeltaW(0.0);
            }
        }
    }


    public void test(DataSet testingSet){
        System.out.println("Testing...");
        double classificationScore = 0.0;
        double countCorrect = 0.0;
        for(int row=0; row < testingSet.getDataSet().size(); row++){
            int inputCounter = 0;
            for(Neuron neuron : this.layers.get(0).getNeuronsInLayer()){
                neuron.setActivation(testingSet.getDataSet().get(row).getInput()[inputCounter]);
                inputCounter++;
            }
            forwardPropagation();
            double modelOutput = this.layers.get(this.layers.size()-1).getNeuronsInLayer().get(0).getActivation();
            displayResults(testingSet,row,modelOutput);
            if(Math.abs(testingSet.getDataSet().get(row).getOutput()[0] - modelOutput) <= 0.1){
                countCorrect++;
            }

        }
        classificationScore = (countCorrect / (double)testingSet.getDataSet().size()) * 100;
        System.out.println("Model Classification Score: "+classificationScore+"%");
        System.out.println("Finished Testing.");
    }

    public void displayResults(DataSet testingSet, int curRow, double output){
        System.out.println("Input: "+Arrays.toString(testingSet.getDataSet().get(curRow).getInput())+" --> Model Output: "+output+" Desired Output: "+Arrays.toString(testingSet.getDataSet().get(curRow).getOutput()));
    }

}
