package mb.common.message;

import java.io.Serializable;

public enum Severity implements Serializable {
    Trace, Debug, Info, Warning, Error;

    @Override public String toString() {
        switch(this) {
            case Trace:
                return "TRACE";
            case Debug:
                return "DEBUG";
            case Info:
                return " INFO";
            case Warning:
                return " WARN";
            case Error:
                return "ERROR";
            default:
                return "?????";
        }
    }
}
