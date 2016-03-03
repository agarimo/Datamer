package datamer.model.boes;

/**
 *
 * @author Agarimo
 */
public enum Kind {

    ND(1),
    RS(2),
    RR(3);
    

    private final int value;

    Kind(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case ND:
                return "ND";
            case RS:
                return "RS";
            case RR:
                return "RR";
            default:
                throw new IllegalArgumentException();
        }
    }

}
