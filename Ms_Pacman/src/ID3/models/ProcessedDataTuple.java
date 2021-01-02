package ID3.models;

import ID3.constants.Attribute;
import pacman.game.Constants;

import java.util.HashMap;

public class ProcessedDataTuple {


    private Constants.MOVE move;

    HashMap<Attribute, Object> values = new HashMap<>();

    public Object getAttributeValue(Attribute attribute) {
        return values.get(attribute);

    }

    public void setAttributeValue(Attribute  attribute, Object value) {
        values.put(attribute, value);
    }

    public HashMap<Attribute, Object> getValues() {
        return values;
    }

    public Constants.MOVE getMove() {
        return move;
    }

    public void setMove(Constants.MOVE move) {
        this.move = move;
    }


}
