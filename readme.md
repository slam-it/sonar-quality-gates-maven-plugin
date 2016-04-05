# Sonar Quality Gates Maven Plugin

[![Build Status](https://travis-ci.org/slam-it/sonar-quality-gates-maven-plugin.svg?branch=master)](https://travis-ci.org/slam-it/sonar-quality-gates-maven-plugin) [![Coverage Status](https://coveralls.io/repos/github/slam-it/sonar-quality-gates-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/slam-it/sonar-quality-gates-maven-plugin?branch=master)

As of SonarQube version 5.2 SonarSource has discontinued the Build Braker plugin feature. The reason why can be read here: [Why You Shouldn’t Use Build Breaker](http://www.sonarqube.org/why-you-shouldnt-use-build-breaker/)

## Description

This plugin will break the maven build if the project fails the quality gate. These checks should happen after analysis has been submitted to the server.

### Usage

```bash
$ mvn sonar-quality-gates:inspect
```

### Configuration

* `sonarHostUrl`
 * The Sonar host url
 * Default: `http://localhost:9000`

*Note:* `sonarHostUrl` property is optional and will be inferred from the `sonar.host.url` property (in the pom.xml or as a property in a [settings.xml profile](#Example Sonar profile (settings.xml))

### Example

```xml
<build>
  <plugins>
    <plugin>
      <groupId>nl.slam-it.maven</groupId>
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
