package nl.slam_it.maven.plugin;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mashape.unirest.http.Unirest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SonarQualityGatesMojoTest {

    @Rule
    public MojoRule rule = new MojoRule();
    @Rule
    public TestResources resources = new TestResources();
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HttpServer server;
    private SonarEventHandler sonarEventHandler;
    private Mojo mojo;

    @Before
    public void setUp() throws Exception {
        sonarEventHandler = new SonarEventHandler();

        server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/", sonarEventHandler);
        server.setExecutor(null);
        server.start();

        mojo = rule.lookupConfiguredMojo(rule.readMavenProject(resources.getBasedir("")), "inspect");
    }

    @After
    public void tearDown() {
        server.stop(0);
    }

    @Test
    public void redEvent() throws Exception {
        exception.expect(MojoExecutionException.class);
        exception.expectMessage("Critical issues != 0, Coverage < 75");

        sonarEventHandler.setResponse(200, getResponse("red.json"));

        mojo.execute();
    }

    @Test
    public void recoveredEvent() throws MojoFailureException, MojoExecutionException {
        sonarEventHandler.setResponse(200, getResponse("recovered.json"));

        mojo.execute();
    }

    @Test
    public void noEvents() throws Exception {
        mojo.execute();
    }

    @Test
    public void error() throws Exception {
        exception.expect(MojoFailureException.class);
        exception.expectMessage(
            "Sonar responded with an error message: Resource not found: nl.slam-it.foo:no-existing-project");

        sonarEventHandler.setResponse(404, getResponse("error.json"));

        mojo.execute();
    }

    @Test
    public void unirestException() throws MojoFailureException, MojoExecutionException {
        exception.expect(MojoFailureException.class);
        exception.expectMessage("Could not execute sonar-quality-gates-plugin");

        Unirest.setHttpClient(null);

        mojo.execute();
    }

    private String getResponse(final String file) {
        final StringBuilder builder = new StringBuilder();

        try (final Scanner scanner = new Scanner(this.getClass().getClassLoader().getResourceAsStream(file))) {
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
        }

        return builder.toString();
    }

    private static final class SonarEventHandler implements HttpHandler {

        private String response = "[]";
        private int status = 200;

        public void handle(final HttpExchange httpExchange) throws IOException {
            final OutputStream responseBody = httpExchange.getResponseBody();

            httpExchange.sendResponseHeaders(status, response.length());

            responseBody.write(response.getBytes());
            responseBody.close();
        }

        public void setResponse(final int status, final String response) {
            this.status = status;
            this.response = response;
        }
    }

}
