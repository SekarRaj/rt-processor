package com.ld.processor.repository;


import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import lombok.Getter;
import lombok.NonNull;
import org.reactivestreams.Publisher;

import static java.util.Objects.nonNull;

public class StatementWrapper {

    private Statement statement;
    @Getter
    private int rowCounter = 0;

    private StatementWrapper(@NonNull Connection connection, @NonNull String sql) {
        this.statement = connection.createStatement(sql);
    }

    public static StatementWrapper of(@NonNull Connection connection, @NonNull String sql) {
        return new StatementWrapper(connection, sql);
    }

    /**
     * Unlike {@link Statement#bind(String, Object)}, this method supports both non-{@code null} as well as {@code null} values.
     */
    public <T> StatementWrapper bind(String name, T value, Class<T> type) {
        if (nonNull(value)) {
            statement = statement.bind(name, value);
        } else {
            statement = statement.bindNull(name, type);
        }
        return this;
    }

    /**
     * Unlike {@link Statement#add()}, this method is loop-friendly. 
     */
    public StatementWrapper add() {
        if (rowCounter++ > 0) { // the first call is ignored
            statement = statement.add();
        }
        return this;
    }

    public Publisher<? extends Result> execute() {
        return statement.execute();
    }
}