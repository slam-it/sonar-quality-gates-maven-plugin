package nl.slam_it.maven.plugin;

import java.io.IOException;

import nl.slam_it.maven.plugin.model.Status;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Deserialize the event name to a @{link Status}.
 */
public class EventNameDeserializer extends JsonDeserializer<Status> {
    private static final String GREEN = "Green";

    /**
     * If the event name starts with the word 'Green' {@code Status.OK} is returned; otherwise {@code Status.ERROR} is
     * returned.
     * 
     * @param parser
     *        JsonParser used for reading JSON content
     * @param context
     *        Context that can be used to access information about this deserialization activity.
     * @return The event name as a {@link Status}
     * @throws IOException
     */
    @Override
    public Status deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        if (!parser.getValueAsString().startsWith(GREEN)) {
            return Status.ERROR;
        }

        return Status.OK;
    }
}
