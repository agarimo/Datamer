package datamer.model.boes;

/**
 *
 * @author Agarimo
 */
public enum Status {
    USER {
        @Override
        public String toString() {
            return "USER";
        }
    },
    SOURCE {
        @Override
        public String toString() {
            return "SOURCE";
        }
    },
    APP {
        @Override
        public String toString() {
            return "APP";
        }
    },
    DELETED {
        @Override
        public String toString() {
            return "DELETED";
        }
    },
    DUPLICATED {
        @Override
        public String toString() {
            return "DUPLICATED";
        }
    },
    PENDING {
        @Override
        public String toString() {
            return "PENDING";
        }
    }
}
