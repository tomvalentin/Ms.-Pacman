package ID3.constants;

public class Attribute {

    double informationGain;
    attributeType type;

    public Attribute(attributeType type) {

    }

    private enum attributeType {
        DISATNCETOGHOSTS,
        ISCLOSESESTGHOSTEDIBLE,
        NBROFPILLS,
        NBROFPOWERPILLS,
        CLOSESTGHOSTDIR

    }

    public double getInformationGain() {
        return informationGain;
    }

    public void setInformationGain(double informationGain) {
        this.informationGain = informationGain;
    }


}
