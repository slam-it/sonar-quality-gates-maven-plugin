package nl.slam_it.maven.plugin;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import nl.slam_it.maven.plugin.model.Error;
import nl.slam_it.maven.plugin.model.Event;
import nl.slam_it.maven.plugin.model.Status;
import org.apache.http.impl.client.HttpClients;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Mojo(name = "inspect")
public class SonarQualityGatesMojo extends AbstractMojo {

    private static final String COLON = ":";
    private static final String SONAR_API_URL = "%s/api/events?categories=Alert&resource=%s";
    private static final String SONAR_DEFAULT_HOST_URL = "http://localhost:9000";
    private static final String SONAR_HOST_URL = "sonar.host.url";
    private static final String SONAR_PROJECT_KEY = "sonar.projectKey";
    private static final int FIRST = 0;
    private static final int STATUS_CODE_OK = 200;

    private final SonarObjectMapper sonarObjectMapper;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Parameter(property = SONAR_HOST_URL)
    private String sonarHostUrl;

    @Inject
    public SonarQualityGatesMojo(final SonarObjectMapper sonarObjectMapper) {
        Unirest.setHttpClient(HttpClients.createDefault());
        this.sonarObjectMapper = sonarObjectMapper;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        final MavenProject topLevelProject = session.getTopLevelProject();
        final String sonarKey = getSonarKey(topLevelProject);
        final String sonarHostUrl = getSonarHostUrl(topLevelProject.getProperties());
        final String sonarApiUrl = String.format(SONAR_API_URL, sonarHostUrl, sonarKey);
        final List<Event> events = retrieveSonarEvents(sonarApiUrl);

        if (!events.isEmpty() && events.get(FIRST).getStatus() != Status.OK) {
            throw new MojoExecutionException(events.get(FIRST).getDescription());
        }
    }

    private List<Event> retrieveSonarEvents(final String url) throws MojoFailureException {
        try {
            final HttpResponse<String> response = Unirest.get(url).asString();
            final String body = response.getBody();

            if (response.getStatus() != STATUS_CODE_OK) {
                final String errorMessage = sonarObjectMapper.readValue(body, Error.class).getMessage();
                throw new MojoFailureException("Sonar responded with an error message: " + errorMessage);
            }

            return asList(sonarObjectMapper.readValue(body, Event[].class));
        } catch (UnirestException e) {
            throw new MojoFailureException("Could not execute sonar-quality-gates-plugin", e);
        } finally {
            shutdown();
        }
    }

    private String getSonarHostUrl(final Properties properties) {
        return sonarHostUrl != null ? sonarHostUrl
            : properties.containsKey(SONAR_HOST_URL) ? properties.getProperty(SONAR_HOST_URL) : SONAR_DEFAULT_HOST_URL;
    }

    private String getSonarKey(final MavenProject pom) {
        if (pom.getModel().getProperties().containsKey(SONAR_PROJECT_KEY)) {
            return pom.getModel().getProperties().getProperty(SONAR_PROJECT_KEY);
        }

        return pom.getGroupId() + COLON + pom.getArtifactId();
    }

    private void shutdown() throws MojoFailureException {
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            throw new MojoFailureException("Could not properly shutdown", e);
        }
    }
}
