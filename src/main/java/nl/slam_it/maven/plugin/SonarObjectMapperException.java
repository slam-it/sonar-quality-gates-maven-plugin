package nl.slam_it.maven.plugin;

public class SonarObjectMapperException extends RuntimeException {

    public SonarObjectMapperException(final Exception exception) {
        super(exception);
    }
}
