package ID3.Utils;

import ID3.constants.Attribute;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.ArrayList;
import java.util.HashMap;

public class DataProcessorUtil {

    /**
     * Used to get the 80% training data and 20% test data (approximately),
     * returned in an array containing ArrayLists with ProcessedDataTuple
     * of training data at index 0 and test data at index 1
     */
    public ArrayList<ProcessedDataTuple>[] generateData(){
        ArrayList<ProcessedDataTuple>[] res = new ArrayList[2];
        ArrayList<ProcessedDataTuple> trainData;
        ArrayList<ProcessedDataTuple> testData;
        ArrayList<ProcessedDataTuple> temp = processTuples();


        int approx80percent = (temp.size()/100)*80;
        System.out.println("size of list: " + temp.size());
        System.out.println("80% is: " + approx80percent);

        trainData = new ArrayList<>(temp.subList(0, approx80percent));
        testData = new ArrayList<>(temp.subList(approx80percent, temp.size()));
        System.out.println("train data size: " + trainData.size());
        System.out.println("test data size: " + testData.size());

        res[0] = trainData;
        res[1] = testData;
        return res;
    }

    /**
     * Takes the Generated game data and process it to create attributes
     * @return
     */
    private ArrayList<ProcessedDataTuple> processTuples() {
        DataTuple[] trainingData = DataSaverLoader.LoadPacManData();

        ArrayList<ProcessedDataTuple> processedDataTuples = new ArrayList<>();

        for (int i = 0; i < trainingData.length; i++) {

            ProcessedDataTuple tempTuple = new ProcessedDataTuple();

            tempTuple.setMove(trainingData[i].DirectionChosen);

            /**
             * Adding direction and closest edible to processed Datatuple hashmap
             */
            if (trainingData[i].blinkyDist < trainingData[i].inkyDist && trainingData[i].blinkyDist < trainingData[i].pinkyDist && trainingData[i].blinkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].blinkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isBlinkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].blinkyDist));
            } else if (trainingData[i].inkyDist < trainingData[i].blinkyDist && trainingData[i].inkyDist < trainingData[i].pinkyDist && trainingData[i].inkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].inkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isInkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].inkyDist));

            } else if (trainingData[i].pinkyDist < trainingData[i].blinkyDist && trainingData[i].pinkyDist < trainingData[i].inkyDist && trainingData[i].pinkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].pinkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isPinkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].pinkyDist));

            } else {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].sueDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isSueEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].sueDist));
            }
            //adding Pills, powerpills, pacman lives left.
            tempTuple.setAttributeValue(Attribute.PILLSLEFT, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].numOfPillsLeft));
            tempTuple.setAttributeValue(Attribute.POWERPILLSLEFT, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].numOfPowerPillsLeft));
            tempTuple.setAttributeValue(Attribute.LIVESLEFT, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].pacmanLivesLeft));
            processedDataTuples.add(tempTuple);
        }
        return processedDataTuples;
    }

    //for testing
    public static void main(String[] args) {
        DataProcessorUtil dataProcessorUtil = new DataProcessorUtil();
        dataProcessorUtil.generateData();
    }

}
