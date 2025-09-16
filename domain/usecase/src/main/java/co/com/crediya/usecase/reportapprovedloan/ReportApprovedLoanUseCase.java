package co.com.crediya.usecase.reportapprovedloan;

import co.com.crediya.model.exception.InvalidTokenException;
import co.com.crediya.model.exception.NoReportsFoundException;
import co.com.crediya.model.exception.NotAdminRoleException;
import co.com.crediya.model.report.ReportSummary;
import co.com.crediya.model.report.gateways.JwtGateway;
import co.com.crediya.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportApprovedLoanUseCase {

    private final ReportRepository reportRepository;
    private final JwtGateway jwtGateway;

    public Mono<ReportSummary> getNumberOfApprovedLoans(String token) {
        return validateUserWithToken(token)
                .then(reportRepository.getReportsInfo())
                .switchIfEmpty(Mono.error(new NoReportsFoundException("Not a single loan was found as approved")));
    }

    private Mono<Void> validateUserWithToken(String token) {
        return jwtGateway.validateToken(token)
                .switchIfEmpty(Mono.error(new InvalidTokenException("Invalid token")))
                .filter(userTokenInfo -> "ADMIN".equalsIgnoreCase(userTokenInfo.getRole()))
                .switchIfEmpty(Mono.error(new NotAdminRoleException("Access denied: User does not have Admin role")))
                .then();
    }
}

