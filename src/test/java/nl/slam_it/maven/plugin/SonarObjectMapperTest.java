package nl.slam_it.maven.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SonarObjectMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SonarObjectMapper sonarObjectMapper;

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
