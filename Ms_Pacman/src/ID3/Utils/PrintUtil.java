package ID3.Utils;

import ID3.constants.NodeType;
import ID3.models.Branch;
import ID3.models.Node;

public class PrintUtil {

    public static void printTree(Node node, int nbrOfIndents) {

        //System.out.println(nbrOfIndents);

        for(int i =0; i<nbrOfIndents; i++) {
            System.out.print("\t");
        }

        if(node.getType() == NodeType.LEAF) {
            System.out.println(node.getDirection());
        } else {
            System.out.println(node.getAttribute());

            for(Branch b: node.getBranches()) {
                for(int i =0; i<nbrOfIndents; i++) {
                    System.out.print("\t");
                }
                System.out.println(b.getValue());
                printTree(b.getChild(), nbrOfIndents+1);

            }

        }
    }
}
