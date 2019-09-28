# Sonar Quality Gates Maven Plugin

## Description

This plugin will break the maven build if the project fails the quality gate. These checks should happen after analysis has been submitted to the server.

### Origin

Fork of https://github.com/slam-it/sonar-quality-gates-maven-plugin, added support for sonarqube 7.9.x, migrated package to com.hack23.maven released to maven central.

### Usage

```bash
$ mvn sonar-quality-gates:inspect
```

### Configuration

* `sonarHostUrl`
 * The Sonar host url
 * Default: `http://localhost:9000`

*Note:* `sonarHostUrl` property is optional and will be inferred from the `sonar.host.url` property (in the pom.xml or as a property in a [settings.xml profile](#Example Sonar profile (settings.xml)) if not specified

### Example

```xml
<build>
  <plugins>
    <plugin>
      <groupId>com.hack23.maven</groupId>
      <artifactId>sonar-quality-gates-maven-plugin</artifactId>
      <version>1.0-SNAPSHOT</version>
      <!-- Optional configuration -->
      <configuration>
        <sonarHostUrl>SONAR-HOST-URL</sonarHostUrl>
      </configuration>
    </plugin>
  </plugins>
</build>
```

### Example Sonar profile (settings.xml)

```xml
<profile>
  <id>sonar</id>
  <properties>
    <sonar.host.url>http://localhost:9000</sonar.host.url>
    <sonar.jdbc.url>jdbc:postgresql://localhost/sonar</sonar.jdbc.url>
    <sonar.jdbc.username>sonar</sonar.jdbc.username>
    <sonar.jdbc.password>xxxxx</sonar.jdbc.password>
    <sonar.jdbc.driver>org.postgresql.Driver</sonar.jdbc.driver>
  </properties>
</profile>

<activeProfiles>
  <activeProfile>sonar</activeProfile>
</activeProfiles>
```
