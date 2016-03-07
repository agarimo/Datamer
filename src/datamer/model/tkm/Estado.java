package datamer.model.tkm;

/**
 *
 * @author Agarimo
 */
public enum Estado {

    CONTRATO(0),
    INTERESADO(1),
    NO_INTERESADO(2),
    ENVIADA_INFO(3),
    VOLVER_LLAMAR(4),
    DEJADO_MSG(5),
    SOCIO_OTRA_DELEGACION(6),
    NO_CONTESTA(7),
    NO_LLAMAR(8),
    EMPRESA_CERRADA(9),
    OTRA_CIA(10),
    BAJA_CONTRATO(11),
    TELF_ERROR(12),
    INFORMADO(13),
    TELF_NO_LOCALIZADO(14);

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
            case CONTRATO:
                return "Contrato";
            case INTERESADO:
                return "Interesado";
            case NO_INTERESADO:
                return "No interesado";
            case ENVIADA_INFO:
                return "Enviada información";
            case VOLVER_LLAMAR:
                return "Volver a llamar";
            case DEJADO_MSG:
                return "Dejado mensaje";
            case SOCIO_OTRA_DELEGACION:
                return "Socio otra delegación";
            case NO_CONTESTA:
                return "No contesta";
            case NO_LLAMAR:
                return "No volver a llamar";
            case EMPRESA_CERRADA:
                return "Empresa de baja/cerrada";
            case OTRA_CIA:
                return "Socio de otra compañía";
            case BAJA_CONTRATO:
                return "Baja de contrato";
            case TELF_ERROR:
                return "Teléfono erroneo";
            case INFORMADO:
                return "Informado";
            case TELF_NO_LOCALIZADO:
                return "Teléfono no localizado";
            default:
                throw new IllegalArgumentException();
        }
    }

}
