package com.ld.processor.repository;

import com.ld.processor.entity.Deal;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class DealRepository {
    private final DatabaseClient databaseClient;

    private static final String INSERT_QUERY = """
            INSERT INTO
                public.DEALS  (code, description, validFrom, validTo, createdOn, updatedOn)
                VALUES (:code, :description, :validFrom, :validTo, :createdOn, :updatedOn)
            """;
//    RETURNING id, code, description, validFrom, validTo, createdOn, updatedOn

    final BiFunction<Row, RowMetadata, Deal> GET_MAPPING_FUNCTION = (r, rm) -> Deal.builder().id(r.get("id", Long.class)).code(r.get("code", String.class)).description(r.get("description", String.class)).validFrom(r.get("validFrom", LocalDateTime.class)).validTo(r.get("validTo", LocalDateTime.class)).createdOn(r.get("createdOn", LocalDateTime.class)).updatedOn(r.get("updatedOn", LocalDateTime.class)).build();

    final Function<Map<String, Object>, Deal> MAPPING_FUNCTION = (r) -> Deal.builder().id((Long) r.get("id")).code((String) r.get("code")).description((String) r.get("description")).validFrom((LocalDateTime) r.get("validFrom")).validTo((LocalDateTime) r.get("validTo")).createdOn((LocalDateTime) r.get("createdOn")).updatedOn((LocalDateTime) r.get("updatedOn")).build();

    public Flux<Deal> findAll() {
        return this.databaseClient.sql("SELECT * FROM public.DEALS")
                .filter((stmt, exeFn) -> stmt.fetchSize(10).execute())
                .map(GET_MAPPING_FUNCTION).all();
    }

    public Mono<Deal> save(Deal deal) {
        return this.databaseClient.sql(INSERT_QUERY)
                .filter((stmt, exeFn) -> stmt.returnGeneratedValues("id").execute())
                .bind("code", deal.code())
                .bind("description", deal.description())
                .bind("validFrom", deal.validFrom())
                .bind("validTo", deal.validTo())
                .bind("createdOn", deal.createdOn())
                .bindNull("updatedOn", LocalDateTime.class)
                .fetch().first()
                .map(MAPPING_FUNCTION);
    }

    /**
     * Doesn't wrk as intended. Inserts only the last item of the collection
     *
     * @param deals
     * @return Flux of Deal
     */
    public Flux<Deal> saveAll(Flux<Deal> deals) {
        return deals.reduce(this.databaseClient.sql(INSERT_QUERY),
                        (genericExecuteSpec, deal) -> genericExecuteSpec
                                .bind("code", deal.code())
                                .bind("description", deal.description())
                                .bind("validFrom", deal.validFrom())
                                .bind("validTo", deal.validTo())
                                .bind("createdOn", deal.createdOn())
                                .bindNull("updatedOn", LocalDateTime.class))
                .flatMapMany(spec -> spec.fetch().all()).map(MAPPING_FUNCTION);
    }

    public Flux<Deal> saveAllByIteration(Flux<Deal> deals) {
        return deals.flatMap(deal ->
                this.databaseClient.sql(INSERT_QUERY)
                        .bind("code", deal.code())
                        .bind("description", deal.description())
                        .bind("validFrom", deal.validFrom())
                        .bind("validTo", deal.validTo())
                        .bind("createdOn", deal.createdOn())
                        .bindNull("updatedOn", LocalDateTime.class)
                        .fetch().all().map(MAPPING_FUNCTION));
    }

    public Flux<?> saveAllByConnection(Flux<Deal> deals) {
        final String QUERY = """
                INSERT INTO
                    public.DEALS  (code)
                    VALUES ($1)
                    RETURNING code
                """;

        return this.databaseClient.inConnectionMany(conn -> {
            var stmt = conn.createStatement(QUERY);

            return deals
                    .map(deal ->
                            stmt
//                                    .bind(0, deal.code())
//                                    .bind(1, deal.description())
//                                    .bindNull(2, LocalDateTime.class)
//                                    .bindNull(3, LocalDateTime.class)
//                                    .bindNull(4, LocalDateTime.class)
                                    .add())
                    .thenMany(Flux.defer(stmt::execute))
                    .flatMap(res -> res.map(((row, rowMetadata) -> Deal.builder().build())))
                    .map(Deal::id);
        });
    }

//    public Flux<?> saveBySpringInterpolation(List<Deal> deals) {
//        final String QUERY = """
//                INSERT INTO
//                    public.DEALS  (id, code, description, validFrom, validTo, createdOn)
//                    VALUES
//                    """;
//        deals.stream().map()
//    }
//    public Flux<?> saveAllByWrapper(List<Deal> deals) {
//        final String QUERY = """
//                INSERT INTO
//                    public.DEALS  (id, code, description, validFrom, validTo, createdOn)
//                    VALUES ($1, $2, $3, $4, $5, $6)
//                    RETURNING id, code, description, validFrom, validTo, createdOn
//                """;
//
//        return this.databaseClient.inConnectionMany(conn -> {
//            StatementWrapper wrapper = StatementWrapper(conn, QUERY);
//            var stmt = conn.createStatement(QUERY);
//
//            return deals.forEach(deal ->
//                            stmt
//                                    .add()
//                                    .bind(0, deal.id())
//                                    .bind(1, deal.code())
//                                    .bind(2, deal.description())
//                                    .bind(3, deal.validFrom())
//                                    .bind(4, deal.validTo())
//                                    .bind(5, deal.createdOn())
//                                    .add())
//                    .thenMany(Flux.defer(stmt::execute))
//                    .flatMap(res -> res.map(((row, rowMetadata) -> Deal.builder().build())))
//                    .map(Deal::id);
//        });
//    }
}
