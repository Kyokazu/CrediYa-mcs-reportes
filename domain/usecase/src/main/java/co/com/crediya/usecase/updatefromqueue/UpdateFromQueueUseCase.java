package co.com.crediya.usecase.updatefromqueue;

import co.com.crediya.model.exception.ApprovedReportNotSavedException;
import co.com.crediya.model.report.Report;
import co.com.crediya.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateFromQueueUseCase {

    private final ReportRepository reportRepository;

    public Mono<Report> saveReportFromLambda(Report report) {
        return reportRepository.saveReportFromLambda(report)
                .switchIfEmpty(Mono.error(new ApprovedReportNotSavedException("Report not saved")));

    }
}
