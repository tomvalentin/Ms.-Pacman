package pacman.controllers;

import ID3.constants.Attribute;
import ID3.informationGain.informationGain;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;

import java.util.ArrayList;
import java.util.Arrays;

public class AIController {
    DataTuple[] trainingData = DataSaverLoader.LoadPacManData();

    public AIController() {


    }

    public void test() {

        ProcessedDataTuple[] processedTuples = processTuples();

        ArrayList<ProcessedDataTuple> temp = new ArrayList<>(Arrays.asList(processedTuples));

        ArrayList<Attribute> attributes = new ArrayList<>();

        for(Attribute attribute: Attribute.values())  {

            attributes.add(attribute);

        }

        Attribute chosenAttribute = informationGain.attributeSelection(temp, attributes);

        System.out.println("Chosen attribute " + chosenAttribute);

    }

    public ProcessedDataTuple[] processTuples() {

        ProcessedDataTuple[] processedDataTuples = new ProcessedDataTuple[trainingData.length];

        for(int i = 0; i<trainingData.length; i++) {

            ProcessedDataTuple tempTuple = new ProcessedDataTuple();

            tempTuple.setMove(trainingData[i].DirectionChosen);

            /**
             * Adding direction and closest edible to processed Datatuple hashmap
             */
            if(trainingData[i].blinkyDist < trainingData[i].inkyDist && trainingData[i].blinkyDist < trainingData[i].pinkyDist && trainingData[i].blinkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].blinkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isBlinkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].blinkyDist));
            }
            else if(trainingData[i].inkyDist < trainingData[i].blinkyDist && trainingData[i].inkyDist < trainingData[i].pinkyDist && trainingData[i].inkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].inkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isInkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].inkyDist));

            }
            else if(trainingData[i].pinkyDist < trainingData[i].blinkyDist && trainingData[i].pinkyDist < trainingData[i].inkyDist && trainingData[i].pinkyDist < trainingData[i].sueDist) {
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].pinkyDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isPinkyEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].pinkyDist));

            }
            else if(trainingData[i].sueDist < trainingData[i].blinkyDist && trainingData[i].sueDist < trainingData[i].inkyDist && trainingData[i].sueDist < trainingData[i].pinkyDist){
                tempTuple.setAttributeValue(Attribute.DIRECTIONOFCLOSESTGHOST, trainingData[i].sueDir);
                tempTuple.setAttributeValue(Attribute.ISCLOSESESTGHOSTEDIBLE, trainingData[i].isSueEdible);
                tempTuple.setAttributeValue(Attribute.DISTANCETOCLOSESTGHOST, DataTuple.DiscreteTag.DiscretizeDouble(trainingData[i].sueDist));


            }

            processedDataTuples[i] = tempTuple;


        }

        return processedDataTuples;
        

    }

    public static void main (String[] args) {

        AIController ai = new AIController();

        ai.test();



    }


}
