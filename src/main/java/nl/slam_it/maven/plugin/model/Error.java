package nl.slam_it.maven.plugin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {

    private final String message;

    @JsonCreator
    public Error(@JsonProperty("err_msg") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
