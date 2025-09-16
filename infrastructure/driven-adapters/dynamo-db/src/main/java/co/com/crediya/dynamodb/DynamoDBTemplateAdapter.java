package co.com.crediya.dynamodb;

import co.com.crediya.dynamodb.helper.TemplateAdapterOperations;
import co.com.crediya.model.report.Report;
import co.com.crediya.model.report.ReportSummary;
import co.com.crediya.model.report.gateways.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.math.BigDecimal;
import java.util.List;


@Repository
@Slf4j
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Report, String, ReportEntity> implements ReportRepository {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        super(connectionFactory,
                mapper,
                d -> mapper.map(d, Report.class),
                "reporte_aprobados",
                ReportEntity.class);
    }

    public Mono<List<Report>> getEntityBySomeKeys(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return query(queryExpression);
    }

    public Mono<List<Report>> getEntityBySomeKeysByIndex(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return queryByIndex(queryExpression);
    }

    private QueryEnhancedRequest generateQueryExpression(String partitionKey, String sortKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(partitionKey).build()))
                .queryConditional(QueryConditional.sortGreaterThanOrEqualTo(Key.builder().sortValue(sortKey).build()))
                .build();
    }

    @Override
    public Mono<Report> saveReportFromLambda(Report report) {
        return super.save(report);
    }

    public Mono<ReportSummary> getReportsInfo() {
        return super.findAll()
                .reduce(
                        new ReportSummary(0L, BigDecimal.ZERO),
                        (summary, entity) -> {
                            return new ReportSummary(
                                    summary.getQuantity() + 1,
                                    summary.getAmountBorrowed().add(entity.getAmount())
                            );
                        }
                );
    }
}
