package datamer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Agárimo
 */
public class Regex {

    private Pattern pt;
    private Matcher mt;

    private String[] patrones;

    public static String[] matriculas = {
        "[0-9]{4}[A-Z]{3}",
        "[A-Z]{1,2}[0-9]{4}[A-Z]{1,2}",
        "[CEPRST]{1}[0-9]{4}[A-Z]{3}",
        "[A-Z]{1,2}[0-9]{4}[A-Z]{1,2}",
        "PROMOTOR",
        "BICI",
        "CARECE",
        "EX",
        "PEATON",
        "EMPRESA",
        "QUAD",
        "BICICLETA"
    };

    public static String[] nif = {
        "[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]{1}",
        "[XYZ]{1}[0-9]{7}[TRWAGMYFPDXBNJZSQVHLCKE]{1}",
        "[ABCDEFGHJKLMNPQRSUVW]{1}[0-9]{8}",
        "[ABCDEFGHJKLMNPQRSUVW]{1}[0-9]{7}[JABCDEFGHI]{1}",
        "NOCONSTA"
    };

    public Regex() {
    }

    public static String buscar(String patron, String str) {
        Pattern pt;
        Matcher mt;
        String aux = null;
        pt = Pattern.compile(patron);
        mt = pt.matcher(str);

        if (mt.matches()) {
            aux = mt.group();
        }

        return aux;
    }

    public boolean isBuscar(String patron, String str) {
        pt = Pattern.compile(patron);
        mt = pt.matcher(str);

        return mt.matches();
    }

    public String buscar(List<String> patrones, String str) {
        String found = null;

        for (String aux : patrones) {
            pt = Pattern.compile(aux);
            mt = pt.matcher(str);

            if (mt.matches()) {
                found = mt.group().trim();
            }
        }
        return found;
    }

    public boolean isBuscar(List<String> patrones, String str) {
        boolean found = false;

        for (String aux : patrones) {
            pt = Pattern.compile(aux);
            mt = pt.matcher(str);

            if (mt.matches()) {
                found = true;
            }
        }

        return found;
    }
}
