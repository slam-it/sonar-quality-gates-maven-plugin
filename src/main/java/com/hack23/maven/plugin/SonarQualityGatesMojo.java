package com.hack23.maven.plugin;

import static com.mashape.unirest.http.Unirest.setHttpClient;
import static java.lang.String.format;
import static org.apache.http.impl.client.HttpClients.createDefault;

import java.io.IOException;
import java.text.MessageFormat;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hack23.maven.plugin.model.Conditions;
import com.hack23.maven.plugin.model.Measures;
import com.hack23.maven.plugin.model.MeasuresContainer;
import com.hack23.maven.plugin.model.QualityGateValue;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Mojo(name = "inspect")
public class SonarQualityGatesMojo extends AbstractMojo {

    private static final String COLON = ":";
    private static final String SONAR_API_URL = "%s/api/measures/search?projectKeys=%s&metricKeys=alert_status,quality_gate_details";
    private static final String SONAR_DEFAULT_HOST_URL = "http://localhost:9000";
    private static final String SONAR_HOST_URL = "sonar.host.url";
    private static final String SONAR_PROJECT_KEY = "sonar.projectKey";
    private static final int STATUS_CODE_OK = 200;
        
    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Parameter(property = SONAR_HOST_URL)
    private String sonarHostUrl;

    @Inject
    public SonarQualityGatesMojo() {
        setHttpClient(createDefault());
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenProject topLevelProject = session.getTopLevelProject();
        List<Measures> events = retrieveSonarMeasures(format(SONAR_API_URL, getSonarHostUrl(topLevelProject.getProperties()), getSonarKey(topLevelProject)));

        if (events.isEmpty()) {
            throw new MojoExecutionException("\nno matching project in sonarqube for project key:" + getSonarKey(topLevelProject));
        }
        
        if (!events.isEmpty() && !events.get(0).getValue().equals("OK")) {
        	
			try {
				QualityGateValue qualityGateValue = new ObjectMapper().readValue(events.get(1).getValue(), QualityGateValue.class);
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

			} catch (IOException e) {
				new MojoFailureException("",e);
			}
        	
         }
    }

    private List<Measures> retrieveSonarMeasures(String url) throws MojoFailureException {
        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            String body = response.getBody();

            if (response.getStatus() != STATUS_CODE_OK) {
                throw new MojoFailureException(MessageFormat.format("Attempt to call Sonarqube responded with an error status :{0} : for url:{1} : response{2}: ",response.getStatus(), url, body)) ;
            }
          
			return new ObjectMapper().readValue(body, MeasuresContainer.class).getMeasures();
        } catch (IOException| UnirestException e) {
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
