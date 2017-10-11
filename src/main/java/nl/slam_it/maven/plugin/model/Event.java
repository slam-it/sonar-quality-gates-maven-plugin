package nl.slam_it.maven.plugin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nl.slam_it.maven.plugin.EventNameDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private final Status status;
    private final String description;

    @JsonCreator
    public Event(@JsonProperty("n") @JsonDeserialize(using = EventNameDeserializer.class) Status status, @JsonProperty(value = "ds") String description) {
        this.status = status;
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
