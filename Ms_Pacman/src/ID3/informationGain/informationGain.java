package ID3.informationGain;

import ID3.constants.Attribute;
import ID3.models.ProcessedDataTuple;
import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.HashMap;

public class informationGain {

    public static double expectedInfo(DataTuple[] dataset) {
        int nbrOfNEUTRAL = 0;
        int nbrOfUP = 0;
        int nbrOfDOWN = 0;
        int nbrOfLEFT = 0;
        int nbrOfRIGHT = 0;

        int totalNbrOfTuples = dataset.length;

        for(DataTuple tuple : dataset) {

            if(tuple.DirectionChosen == Constants.MOVE.NEUTRAL ) {
                nbrOfNEUTRAL++;

            } else if (tuple.DirectionChosen == Constants.MOVE.UP) {
                nbrOfUP++;

            } else if (tuple.DirectionChosen == Constants.MOVE.DOWN) {
                nbrOfDOWN++;

            } else if (tuple.DirectionChosen == Constants.MOVE.LEFT) {
                nbrOfLEFT++;

            } else if (tuple.DirectionChosen == Constants.MOVE.RIGHT) {
                nbrOfRIGHT++;

            }

        }


      int[] values = new int[5];

      values[0] = nbrOfNEUTRAL;
      values[1] = nbrOfUP;
      values[2] = nbrOfDOWN;
      values[3] = nbrOfLEFT;
      values[4] = nbrOfRIGHT;

      double info = calculateInfo(values, totalNbrOfTuples);


      return info;

    }

    public static double calculateGain(ProcessedDataTuple[] dataset) {







    }

    public Attribute attributeSelection(ProcessedDataTuple[] dataset) {

        for(ProcessedDataTuple tuple: dataset ) {



        }

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


    public static void main (String[] args) {

        DataTuple[] dataset = DataSaverLoader.LoadPacManData();

        System.out.println(expectedInfo(dataset));

    }

}
