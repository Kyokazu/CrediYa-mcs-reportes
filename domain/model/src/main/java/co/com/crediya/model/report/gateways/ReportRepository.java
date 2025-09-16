package co.com.crediya.model.report.gateways;

import co.com.crediya.model.report.ReportSummary;
import co.com.crediya.model.report.Report;
import reactor.core.publisher.Mono;

public interface ReportRepository {

    Mono<Report> saveReportFromLambda(Report report);

    Mono<ReportSummary> getReportsInfo();

}
