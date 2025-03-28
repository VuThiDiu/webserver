package constants;

import lombok.Getter;

@Getter
public enum ResCode {
    OK("200", "OK"),
    BAD_REQUEST("400", "Bad Request"),
    NOT_FOUND("404", "Not Found");

    ResCode(String code, String des) {
        this.code = code;
        this.des = des;
    }

    private final String code;
    private final String des;
}
