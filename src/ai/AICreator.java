package ai;


import java.io.File;

import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class AICreator {

    public static void main(String[] args) throws Exception {
        //number of rows and columns in the input pictures
        final int numRows = 3;
        final int numColumns = 1;
        int outputNum = 4; // number of output classes
        int batchSize = 50;
        int rngSeed = 123; // random number seed for reproducibility
        int numEpochs = 15; // number of epochs to perform
        double rate = 0.0015; // learning rate

        //Get the DataSetIterators:
        
        int numLinesToSkip = 0;
        char delimiter = ';';
        CSVRecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter);
        recordReader.initialize(new FileSplit(new File("resources/DataSet.csv")));
        int labelIndex = 3;
        int numClasses = 4;
        
        DataSetIterator ALIVETrain = new RecordReaderDataSetIterator(recordReader,batchSize,labelIndex,numClasses);

        System.out.println(("Build model...."));
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(rngSeed) //include a random seed for reproducibility
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nadam())
            .l2(rate * 0.005) // regularize learning model
            .list()
            .layer(new DenseLayer.Builder() //create the first input layer.
                    .nIn(numRows * numColumns)
                    .nOut(500)
                    .build())
            .layer(new DenseLayer.Builder() //create the second input layer
                    .nIn(500)
                    .nOut(100)
                    .build())
            .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                    .activation(Activation.SOFTMAX)
                    .nOut(outputNum)
                    .build())
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(5));  //print the score with every iteration

        System.out.println("Train model....");
        model.fit(ALIVETrain, numEpochs);

        System.out.println("Evaluate model....");
        Evaluation eval = model.evaluate(ALIVETrain);

        System.out.println(eval.stats());
        model.save(new File("AI.zip"));
        System.out.println("****************Training finished********************");

    }

}
