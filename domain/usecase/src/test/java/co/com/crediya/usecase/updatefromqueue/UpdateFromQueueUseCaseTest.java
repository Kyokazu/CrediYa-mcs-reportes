package co.com.crediya.usecase.updatefromqueue;

import co.com.crediya.model.exception.ApprovedReportNotSavedException;
import co.com.crediya.model.report.Report;
import co.com.crediya.model.report.gateways.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UpdateFromQueueUseCaseTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private UpdateFromQueueUseCase updateFromQueueUseCase;

    // --- Tests ---

    @Test
    void shouldSaveReportSuccessfully() {
        Report dummyReport = createDummyReport();
        doReturn(Mono.just(dummyReport))
                .when(reportRepository)
                .saveReportFromLambda(any(Report.class));

        StepVerifier.create(updateFromQueueUseCase.saveReportFromLambda(dummyReport))
                .expectNext(dummyReport)
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenReportIsNotSaved() {
        // Arrange
        Report dummyReport = createDummyReport();
        doReturn(Mono.empty())
                .when(reportRepository)
                .saveReportFromLambda(any(Report.class));

        // Act & Assert
        StepVerifier.create(updateFromQueueUseCase.saveReportFromLambda(dummyReport))
                .expectErrorMatches(throwable ->
                        throwable instanceof ApprovedReportNotSavedException &&
                                throwable.getMessage().equals("Report not saved"))
                .verify();
    }


    private Report createDummyReport() {
        return Report.builder()
                .id("123")
                .email("test@example.com")
                .amount(BigDecimal.valueOf(10000))
                .monthlyPayment(BigDecimal.valueOf(500))
                .duration(24L)
                .interestRate(5.5)
                .build();
    }
}
