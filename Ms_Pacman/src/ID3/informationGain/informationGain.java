package ID3.informationGain;

import ID3.constants.Attribute;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class informationGain {

    public static Attribute attributeSelection(ArrayList<ProcessedDataTuple> dataset, ArrayList<Attribute> attributeList) {

        double currentBest = -Double.MAX_VALUE;
        Attribute bestCandidate = attributeList.get(0);

        double expectedInfo = expectedInfo(dataset);

        for(Attribute attribute: attributeList) {

            double currentInfo = calculateInfoOfOneAttribute(dataset, attribute);

            double currentGain = expectedInfo - currentInfo;

            if(currentGain > currentBest) {
                currentBest = currentGain;
                bestCandidate = attribute;
            }

        }

        return bestCandidate;

    }

    public static double expectedInfo(ArrayList<ProcessedDataTuple> dataset) {
        int nbrOfNEUTRAL = 0;
        int nbrOfUP = 0;
        int nbrOfDOWN = 0;
        int nbrOfLEFT = 0;
        int nbrOfRIGHT = 0;

        int totalNbrOfTuples = dataset.size();

        for(ProcessedDataTuple tuple : dataset) {

            if (tuple.getMove() == Constants.MOVE.NEUTRAL) {
                nbrOfNEUTRAL++;

            } else if (tuple.getMove() == Constants.MOVE.UP) {
                nbrOfUP++;

            } else if (tuple.getMove() == Constants.MOVE.DOWN) {
                nbrOfDOWN++;

            } else if (tuple.getMove() == Constants.MOVE.LEFT) {
                nbrOfLEFT++;

            } else if (tuple.getMove() == Constants.MOVE.RIGHT) {
                nbrOfRIGHT++;

            }

        }

        int[] values;

        if(nbrOfNEUTRAL == 0) {

            values = new int[4];

            values[0] = nbrOfUP;
            values[1] = nbrOfDOWN;
            values[2] = nbrOfLEFT;
            values[3] = nbrOfRIGHT;

        } else {

            values = new int[5];

            values[0] = nbrOfNEUTRAL;
            values[1] = nbrOfUP;
            values[2] = nbrOfDOWN;
            values[3] = nbrOfLEFT;
            values[4] = nbrOfRIGHT;

        }



      double info = calculateInfo(values, totalNbrOfTuples);

      return info;

    }


    public static double calculateInfoOfOneAttribute(ArrayList<ProcessedDataTuple> dataset, Attribute attribute) {

        HashMap<Object, ArrayList<ProcessedDataTuple>> splitDataset = new HashMap<>();

        for(ProcessedDataTuple tuple: dataset) {

            Object value = tuple.getAttributeValue(attribute);

            if(!splitDataset.containsKey(value)) {
                splitDataset.put(value, new ArrayList<ProcessedDataTuple>());
            }

            splitDataset.get(value).add(tuple);

        }

        double attributeInfo = 0;

        for(Map.Entry<Object, ArrayList<ProcessedDataTuple>> entry: splitDataset.entrySet()) {
            attributeInfo += ((double)entry.getValue().size()/(double)dataset.size()) * expectedInfo(entry.getValue());
        }

        return attributeInfo;

    }


    public static double calculateInfo(int[] values, int totalNbr) {

        double totalInfo = 0;

        for(int x : values) {

            double probability = calculateProbability(x, totalNbr);
            double expectedX = -probability*log2(probability);

            totalInfo += expectedX;
        }

        return totalInfo;
    }

    public static double log2(double x) {
        return (Math.log(x) / Math.log(2));
    }


    public static double calculateProbability(int frequency, int totalNumber) {

        return (double)frequency / (double)totalNumber;

    }


//    public static void main (String[] args) {
//
//        DataTuple[] dataset = DataSaverLoader.LoadPacManData();
//
//        System.out.println(expectedInfo(dataset));
//
//    }

}
