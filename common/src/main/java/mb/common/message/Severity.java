package mb.common.message;

import java.io.Serializable;

public enum Severity implements Serializable {
    Trace, Debug, Info, Warning, Error;

    public String toDisplayString() {
        switch(this) {
            case Trace:
                return "trace";
            case Debug:
                return "debug";
            case Info:
                return "info";
            case Warning:
                return "warning";
            case Error:
                return "error";
            default:
                return "???";
        }
    }

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
