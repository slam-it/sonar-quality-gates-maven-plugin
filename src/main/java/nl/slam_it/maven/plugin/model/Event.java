package nl.slam_it.maven.plugin.model;

import nl.slam_it.maven.plugin.EventNameDeserializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Event {
    private final String id;
    private final Status status;
    private final String resourceKey;
    private final String category;
    private final String date;
    private final String description;

    @JsonCreator
    public Event(@JsonProperty("id") final String id,
                 @JsonProperty("n") @JsonDeserialize(using = EventNameDeserializer.class) final Status status,
                 @JsonProperty("rk") final String resourceKey, @JsonProperty("c") final String category,
                 @JsonProperty("dt") final String date, @JsonProperty(value = "ds") final String description) {
        this.id = id;
        this.status = status;
        this.resourceKey = resourceKey;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
