package constants;

public enum ResCode {
    OK("200", "OK"),
    BAD_REQUEST("400", "Bad Request"),
    NOT_FOUND("404", "Not Found");

    ResCode(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }
    public String getDes(){
        return des;
    }

    private String code;
    private String des;
}
