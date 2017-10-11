package nl.slam_it.maven.plugin;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import nl.slam_it.maven.plugin.model.Error;
import nl.slam_it.maven.plugin.model.Event;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static com.mashape.unirest.http.Unirest.setHttpClient;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static nl.slam_it.maven.plugin.model.Status.OK;
import static org.apache.http.impl.client.HttpClients.createDefault;

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
    public SonarQualityGatesMojo(SonarObjectMapper sonarObjectMapper) {
        setHttpClient(createDefault());
        this.sonarObjectMapper = sonarObjectMapper;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenProject topLevelProject = session.getTopLevelProject();
        List<Event> events = retrieveSonarEvents(format(SONAR_API_URL, getSonarHostUrl(topLevelProject.getProperties()), getSonarKey(topLevelProject)));

        if (!events.isEmpty() && events.get(FIRST).getStatus() != OK) {
            throw new MojoExecutionException(events.get(FIRST).getDescription());
        }
    }

    private List<Event> retrieveSonarEvents(String url) throws MojoFailureException {
        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            String body = response.getBody();

            if (response.getStatus() != STATUS_CODE_OK) {
                String errorMessage = sonarObjectMapper.readValue(body, Error.class).getMessage();
                throw new MojoFailureException("Sonar responded with an error message: " + errorMessage);
            }

            return asList(sonarObjectMapper.readValue(body, Event[].class));
        } catch (UnirestException e) {
            throw new MojoFailureException("Could not execute sonar-quality-gates-plugin", e);
        } finally {
            shutdown();
        }
    }

    private String getSonarHostUrl(Properties properties) {
        if (sonarHostUrl != null) {
            return sonarHostUrl;
        }

        return properties.containsKey(SONAR_HOST_URL) ? properties.getProperty(SONAR_HOST_URL) : SONAR_DEFAULT_HOST_URL;
    }

    private String getSonarKey(MavenProject pom) {
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
