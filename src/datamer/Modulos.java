package datamer;

/**
 *
 * @author Agarimo
 */
public enum Modulos {

    MAIN_WINDOW("MAIN"),
    BOES_BOLETINES("BOLETINES"),
    BOES_CLASIFICACION("CLASIFICACION"),
    BOES_EXTRACCION("EXTRACCIÓN"),
    BOES_FASES("FASES"),
    BOES_PATTERN("PATRONES"),
    TELEMARK("TELEMARKETING");

    private final String nombre;

    Modulos(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRuta() {
        switch (this) {
            case MAIN_WINDOW:
                return "/datamer/view/Win.fxml";
            case BOES_BOLETINES:
                return "/datamer/view/boes/Boletines.fxml";
            case BOES_CLASIFICACION:
                return "/datamer/view/boes/Clasificacion.fxml";
            case BOES_EXTRACCION:
                return "/datamer/view/boes/Ext.fxml";
            case BOES_FASES:
                return "/datamer/view/boes/Fases.fxml";
            case BOES_PATTERN:
                return "/datamer/view/boes/Pattern.fxml";
            case TELEMARK:
                return "/datamer/view/tkm/Telemark.fxml";
            default:
                throw new IllegalArgumentException();
        }
    }
    
    @Override
    public String toString(){
        return "";
    }
}
