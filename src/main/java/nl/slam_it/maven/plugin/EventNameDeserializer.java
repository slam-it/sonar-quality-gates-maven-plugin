package nl.slam_it.maven.plugin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import nl.slam_it.maven.plugin.model.Status;

import java.io.IOException;

import static nl.slam_it.maven.plugin.model.Status.ERROR;
import static nl.slam_it.maven.plugin.model.Status.OK;

/**
 * Deserialize the event name to a @{link Status}.
 */
public class EventNameDeserializer extends JsonDeserializer<Status> {
    /**
     * If the event name starts with the word 'Green' {@code Status.OK} is returned; otherwise {@code Status.ERROR} is
     * returned.
     *
     * @param parser  JsonParser used for reading JSON content
     * @param context Context that can be used to access information about this deserialization activity.
     *
     * @return The event name as a {@link Status}
     *
     * @throws IOException
     */
    @Override
    public Status deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return parser.getValueAsString().startsWith("Green") ? OK : ERROR;
    }
}
