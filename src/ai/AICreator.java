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
/**
 * Creates and train the AI to control the car based on the data from 3 ultrasonic sensors
 * @author Émile Gagné
 */
public class AICreator {
	
    public static void main(String[] args) throws Exception {
        // Number of rows and columns for the data set
        final int numRows = 3;
        final int numColumns = 1;
        int outputNum = 4; // Number of output classes
        int batchSize = 50;
        int rngSeed = 123; // Random number seed for reproducibility
        int numEpochs = 15; // Number of epochs to perform
        double rate = 0.0015; // Learning rate

        //Creates the DataSetIterator after correctly parsing the data from the .csv sheet
        
        int numLinesToSkip = 0;
        char delimiter = ';'; // Delimiting character in the .csv file
        CSVRecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter); // Reader to input the .csv file and parse it
        recordReader.initialize(new FileSplit(new File("resources/DataSet.csv"))); // Reader reads the file
        int labelIndex = 3; // On what column is the "right" answer (Also called label)
        int numClasses = 4; // Number of possible outputs
        
        DataSetIterator ALIVETrain = new RecordReaderDataSetIterator(recordReader,batchSize,labelIndex,numClasses); // Create the DataSetIterator

        System.out.println(("Build model....")); //Update on the creation process
        // Creates the configuration of the neural network witht the different layers
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder() 
            .seed(rngSeed) // Include a random seed for reproducibility
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nadam())
            .l2(rate * 0.005) // Regularize learning model
            .list()
            .layer(new DenseLayer.Builder() // Create the first input layer.
                    .nIn(numRows * numColumns)
                    .nOut(500)
                    .build())
            .layer(new DenseLayer.Builder() // Create the second input layer
                    .nIn(500)
                    .nOut(100)
                    .build())
            .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) // Create hidden layer
                    .activation(Activation.SOFTMAX)
                    .nOut(outputNum)
                    .build())
            .build();
        // Build the network using the previously made configuration
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        // Initialize the network
        model.init();
        // Create listeners to get updates on every iteration
        model.setListeners(new ScoreIterationListener(5));

        System.out.println("Train model...."); // Update on the training process
        model.fit(ALIVETrain, numEpochs); // Trains the model with the selected DataSet and configuration

        System.out.println("Evaluate model...."); // Update on the evaluation process
        Evaluation eval = model.evaluate(ALIVETrain); // Evaluates the model and it's answers

        System.out.println(eval.stats()); 
        model.save(new File("AI.zip")); // Exports the file as a .zip containing all the weights and required paramaters to load the AI
        System.out.println("****************Training finished********************");

    }

}
