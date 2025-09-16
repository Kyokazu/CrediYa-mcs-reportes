package co.com.crediya.usecase.reportapprovedloan;

import co.com.crediya.model.exception.InvalidTokenException;
import co.com.crediya.model.exception.NoReportsFoundException;
import co.com.crediya.model.exception.NotAdminRoleException;
import co.com.crediya.model.report.ReportSummary;
import co.com.crediya.model.report.UserTokenInfo;
import co.com.crediya.model.report.gateways.JwtGateway;
import co.com.crediya.model.report.gateways.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportApprovedLoanUseCaseTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private JwtGateway jwtGateway;

    @InjectMocks
    private ReportApprovedLoanUseCase reportApprovedLoanUseCase;

    private static final String VALID_ADMIN_TOKEN = "admin-token";
    private static final String INVALID_TOKEN = "invalid-token";
    private static final String NON_ADMIN_TOKEN = "non-admin-token";

    @Test
    void shouldReturnReportSummaryForAdminUser() {
        UserTokenInfo adminUser = new UserTokenInfo(VALID_ADMIN_TOKEN, "admin@example.com", "ADMIN");
        ReportSummary expectedSummary = new ReportSummary(5L, new BigDecimal("10000.00"));

        doReturn(Mono.just(adminUser))
                .when(jwtGateway)
                .validateToken(VALID_ADMIN_TOKEN);
        doReturn(Mono.just(expectedSummary))
                .when(reportRepository)
                .getReportsInfo();

        StepVerifier.create(reportApprovedLoanUseCase.getNumberOfApprovedLoans(VALID_ADMIN_TOKEN))
                .expectNext(expectedSummary)
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenNoReportsAreFound() {
        UserTokenInfo adminUser = new UserTokenInfo(VALID_ADMIN_TOKEN, "admin@example.com", "ADMIN");

        doReturn(Mono.just(adminUser))
                .when(jwtGateway)
                .validateToken(VALID_ADMIN_TOKEN);
        doReturn(Mono.empty())
                .when(reportRepository)
                .getReportsInfo();

        StepVerifier.create(reportApprovedLoanUseCase.getNumberOfApprovedLoans(VALID_ADMIN_TOKEN))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoReportsFoundException &&
                                throwable.getMessage().equals("Not a single loan was found as approved"))
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        // Arrange: Simula que el gateway lanza un error directamente
        doReturn(Mono.error(new InvalidTokenException("Invalid token")))
                .when(jwtGateway)
                .validateToken(INVALID_TOKEN);
        doReturn(Mono.empty())
                .when(reportRepository)
                .getReportsInfo();

        // Act & Assert
        StepVerifier.create(reportApprovedLoanUseCase.getNumberOfApprovedLoans(INVALID_TOKEN))
                .expectErrorMatches(throwable ->
                        throwable instanceof InvalidTokenException &&
                                throwable.getMessage().equals("Invalid token"))
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotAdmin() {
        doReturn(Mono.error(new NotAdminRoleException("Access denied: User does not have Admin role")))
                .when(jwtGateway)
                .validateToken(NON_ADMIN_TOKEN);
        doReturn(Mono.empty())
                .when(reportRepository)
                .getReportsInfo();

        StepVerifier.create(reportApprovedLoanUseCase.getNumberOfApprovedLoans(NON_ADMIN_TOKEN))
                .expectErrorMatches(throwable ->
                        throwable instanceof NotAdminRoleException &&
                                throwable.getMessage().equals("Access denied: User does not have Admin role"))
                .verify();
    }
}
