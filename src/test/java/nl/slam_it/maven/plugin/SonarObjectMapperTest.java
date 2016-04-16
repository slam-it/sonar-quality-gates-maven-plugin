package nl.slam_it.maven.plugin;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SonarObjectMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    private SonarObjectMapper sonarObjectMapper;

    @Before
    public void setUp() {
        initMocks(this);
        sonarObjectMapper = new SonarObjectMapper(objectMapper);
    }

    @Test
    public void readValue() throws IOException {
        when(objectMapper.readValue("value", String.class)).thenReturn("value");

        assertThat(sonarObjectMapper.readValue("value", String.class), is("value"));
    }

    @Test(expected = RuntimeException.class)
    public void readValueIOException() throws Exception {
        when(objectMapper.readValue("value", String.class)).thenThrow(new IOException());

        sonarObjectMapper.readValue("value", String.class);
    }

    @Test
    public void writeValue() throws JsonProcessingException {
        when(objectMapper.writeValueAsString("value")).thenReturn("value");

        assertThat(sonarObjectMapper.writeValue("value"), is("value"));
    }

    @Test(expected = RuntimeException.class)
    public void writeValueJsonProcessingException() throws Exception {
        when(objectMapper.writeValueAsString("value")).thenThrow(mock(JsonProcessingException.class));

        sonarObjectMapper.writeValue("value");
    }
}
