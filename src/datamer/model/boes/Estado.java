package datamer.model.boes;

/**
 *
 * @author Agarimo
 */
public enum Estado {

    SIN_PROCESAR(0),
    LISTO_PROCESAR(1),
    PROCESADO_XLSX(2),
    ERROR_PROCESAR(3),
    PDF_NO_GENERADO(4),
    XLSX_NO_GENERADO(5),
    PROCESAR_MANUAL(6);

    private final int value;

    Estado(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case SIN_PROCESAR:
                return "Sin procesar";
            case LISTO_PROCESAR:
                return "Listo para procesar";
            case PROCESADO_XLSX:
                return "Procesado";
            case ERROR_PROCESAR:
                return "Error en procesado";
            case PDF_NO_GENERADO:
                return "Pdf no generado";
            case XLSX_NO_GENERADO:
                return "XLSX no generado";
            case PROCESAR_MANUAL:
                return "Procesar manualmente";
            default:
                throw new IllegalArgumentException();
        }
    }

}
