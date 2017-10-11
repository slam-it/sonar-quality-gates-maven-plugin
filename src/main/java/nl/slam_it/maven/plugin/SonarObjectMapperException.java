package nl.slam_it.maven.plugin;

public class SonarObjectMapperException extends RuntimeException {

    public SonarObjectMapperException(Exception exception) {
        super(exception);
    }
}
