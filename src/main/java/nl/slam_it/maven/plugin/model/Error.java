package nl.slam_it.maven.plugin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    private final String code;
    private final String message;

    @JsonCreator
    public Error(@JsonProperty("err_code") final String code, @JsonProperty("err_msg") final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
