package datamer.model.tkm;

/**
 *
 * @author Agarimo
 */
public enum Estado {

    SIN_ASIGNAR(0),
    CONTRATO(1),
    INTERESADO(2),
    NO_INTERESADO(3),
    ENVIADA_INFO(4),
    VOLVER_LLAMAR(5),
    DEJADO_MSG(6),
    SOCIO_OTRA_DELEGACION(7),
    NO_CONTESTA(8),
    NO_LLAMAR(9),
    EMPRESA_CERRADA(10),
    OTRA_CIA(11),
    BAJA_CONTRATO(12),
    TELF_ERROR(13),
    INFORMADO(14),
    TELF_NO_LOCALIZADO(15);

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
            case SIN_ASIGNAR:
                return "Sin asignar";
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
