package utils;

import constants.ResCode;

public class Template {
    public static final String HTTP_1_1 = "HTTP/1.1";

    public static String responseTemplate = "%s %s %s\n" +
            "Content-Type: text/html\n" +
            "Content-Length: %d\n" +
            "\n" +
            "%s";

    public static String buildResponse(ResCode resCode, String content) {
        return String.format(responseTemplate, HTTP_1_1, resCode.getCode(), resCode.getDes(), content.length(), content);
    }
}
