package nl.slam_it.maven.plugin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.slam_it.maven.plugin.EventNameDeserializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private final Status status;
    private final String description;

    @JsonCreator
    public Event(@JsonProperty("n") @JsonDeserialize(using = EventNameDeserializer.class) final Status status,
                 @JsonProperty(value = "ds") final String description) {
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
