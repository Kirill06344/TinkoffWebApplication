package ru.tinkoff.edu.java.scrapper.environment;

import org.testcontainers.utility.DockerImageName;

public interface PostgreSQLTestImages {

    DockerImageName POSTGRES_TEST_IMAGE = DockerImageName.parse("postgres:15");
}
