package com.ld.processor.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.r2dbc.core.DatabaseClient;

@RequiredArgsConstructor
public class DatabaseConfig {

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        return new PostgresqlConnectionFactory(
//                PostgresqlConnectionConfiguration.builder()
//                        .host("localhost")
//                        .port(5432)
//                        .username("postgres")
//                        .password("password")
//                        .database("postgres")
//                        .build()
//        );
//    }

    private final ConnectionFactory connectionFactory;

    @Bean
    public DatabaseClient databaseClient() {
        return DatabaseClient.create(connectionFactory);
    }

//    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//        initializer.setConnectionFactory(connectionFactory);
//
//        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
//        return initializer;
//    }
}
