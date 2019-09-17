package nl.slam_it.maven.plugin;

import static com.mashape.unirest.http.Unirest.setHttpClient;
import static java.lang.String.format;
import static org.apache.http.impl.client.HttpClients.createDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

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

import nl.slam_it.maven.plugin.model.Conditions;
import nl.slam_it.maven.plugin.model.Error;
import nl.slam_it.maven.plugin.model.Measures;
import nl.slam_it.maven.plugin.model.MeasuresContainer;
import nl.slam_it.maven.plugin.model.QualityGateValue;

@Mojo(name = "inspect")
public class SonarQualityGatesMojo extends AbstractMojo {

    private static final String COLON = ":";
    private static final String SONAR_API_URL = "%s/api/measures/search?projectKeys=%s&metricKeys=alert_status,quality_gate_details";
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
        List<Measures> events = retrieveSonarEvents(format(SONAR_API_URL, getSonarHostUrl(topLevelProject.getProperties()), getSonarKey(topLevelProject)));

        if (events.isEmpty()) {
            throw new MojoExecutionException("\nno matching project in sonarqube for project key:" + getSonarKey(topLevelProject));
        }
        
        if (!events.isEmpty() && !events.get(0).getValue().equals("OK")) {
        	
        	QualityGateValue qualityGateValue = sonarObjectMapper.readValue(events.get(1).getValue(), QualityGateValue.class);
        	
        	StringBuilder builder = new StringBuilder();
        	builder.append("\nFailed quality gate\n");
        	ArrayList<Conditions> conditions = qualityGateValue.getConditions();
        	for (Conditions condition : conditions) {
				if (!condition.getLevel().equals("OK")) {
					builder.append(condition);
					builder.append("\n");
				}
			}
        	
            throw new MojoExecutionException(builder.toString());
        }
    }

    private List<Measures> retrieveSonarEvents(String url) throws MojoFailureException {
        try {
        	System.out.println(url);
            HttpResponse<String> response = Unirest.get(url).asString();
            String body = response.getBody();
            System.out.println(body);

            if (response.getStatus() != STATUS_CODE_OK) {
                String errorMessage = sonarObjectMapper.readValue(body, Error.class).getMessage();
                throw new MojoFailureException("Sonar responded with an error message: " + errorMessage);
            }

           
            return sonarObjectMapper.readValue(body, MeasuresContainer.class).getMeasures();
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
