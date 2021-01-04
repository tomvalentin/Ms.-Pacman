package ID3.Utils;

import ID3.models.Node;
import ID3.models.ProcessedDataTuple;
import pacman.game.Constants;

import java.util.ArrayList;

public class TreeAccuracy {
    TreeUtil treeUtil;

    public String testAccuracy(Node decisionTree, ArrayList<ProcessedDataTuple> testdata){
        int amountOfTests = 0;
        int correct = 0;
        int faulty = 0;
        treeUtil = new TreeUtil();

        for(ProcessedDataTuple p: testdata){
            amountOfTests++;
            Constants.MOVE m = treeUtil.findMoveInTree(decisionTree, p.getValues());
            if(m == p.getMove())
                correct++;
            else
                faulty ++;
        }

        float accuracy = ((correct * 100.0f) / amountOfTests);

        return "----------------------------\n" +
                "Amount of tests performed: " + amountOfTests + "\n"+
                "Correct prediction: " + correct + "\nFaulty prediction: " + faulty +  "\n"+
                "Accuracy = " + accuracy + "%\n" +
                "----------------------------\n";
    }
}
