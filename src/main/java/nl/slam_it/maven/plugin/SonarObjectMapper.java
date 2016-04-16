package nl.slam_it.maven.plugin;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;

@Named
@Singleton
public class SonarObjectMapper implements ObjectMapper {
    private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;

    @Inject
    public SonarObjectMapper(final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Override
    public <T> T readValue(final String value, final Class<T> valueType) {
        try {
            return jacksonObjectMapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new SonarObjectMapperException(e);
        }
    }

    @Override
    public String writeValue(final Object value) {
        try {
            return jacksonObjectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new SonarObjectMapperException(e);
        }
    }
}
