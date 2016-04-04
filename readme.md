# Sonar Quality Gates Maven Plugin

[![Build Status](https://travis-ci.org/slam-it/sonar-quality-gates-maven-plugin.svg?branch=master)](https://travis-ci.org/slam-it/sonar-quality-gates-maven-plugin) [![Coverage Status](https://coveralls.io/repos/github/slam-it/sonar-quality-gates-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/slam-it/sonar-quality-gates-maven-plugin?branch=master)

As of SonarQube version 5.2 SonarSource has discontinued the Build Braker plugin feature. The reason why can be read here: [Why You Shouldn’t Use Build Breaker](http://www.sonarqube.org/why-you-shouldnt-use-build-breaker/)

## Description

This plugin will break the maven build if the project fails the quality gate. These checks should happen after analysis has been submitted to the server.

## Usage

```xml
<plugin>
    <groupId>nl.slam-it.maven</groupId>
    <artifactId>sonar-quality-gates-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration/>
</plugin>
```
