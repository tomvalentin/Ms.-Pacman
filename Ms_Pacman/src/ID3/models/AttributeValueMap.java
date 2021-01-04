package ID3.models;

import ID3.constants.Attribute;
import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class AttributeValueMap {

    public static HashMap<Attribute, ArrayList<Object>> getAttributeValueMap() {
        HashMap<Attribute, ArrayList<Object>> attributeValueMap = new HashMap<>();

        ArrayList<Object> temp = new ArrayList<>();
        temp.add(true);
        temp.add(false);

        attributeValueMap.put(Attribute.ISCLOSESESTGHOSTEDIBLE, temp);

        temp = new ArrayList<>();
        temp.add(DataTuple.DiscreteTag.VERY_LOW);
        temp.add(DataTuple.DiscreteTag.LOW);
        temp.add(DataTuple.DiscreteTag.MEDIUM);
        temp.add(DataTuple.DiscreteTag.HIGH);
        temp.add(DataTuple.DiscreteTag.VERY_HIGH);

        attributeValueMap.put(Attribute.DISTANCETOCLOSESTGHOST, temp);

        temp = new ArrayList<>();
        temp.add(Constants.MOVE.LEFT);
        temp.add(Constants.MOVE.RIGHT);
        temp.add(Constants.MOVE.UP);
        temp.add(Constants.MOVE.DOWN);

        attributeValueMap.put(Attribute.DIRECTIONOFCLOSESTGHOST, temp);


        return attributeValueMap;

    }

}
