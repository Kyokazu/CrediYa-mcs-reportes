package co.com.crediya.api;

import co.com.crediya.api.dto.ReportDTO;
import co.com.crediya.api.exception.MissingInvalidAuthHeaderException;
import co.com.crediya.model.report.ReportSummary;
import co.com.crediya.usecase.reportapprovedloan.ReportApprovedLoanUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {


    private final ReportApprovedLoanUseCase reportApprovedLoanUseCase;

    public Mono<ServerResponse> getReport(ServerRequest request) {
        log.info("Generating report with borrowed capital and the quantity of approved loans");
        return extractToken(request)
                .flatMap(reportApprovedLoanUseCase::getNumberOfApprovedLoans)
                .flatMap(this::buildSuccessResponse);

    }


    private Mono<String> extractToken(ServerRequest request) {
        return Mono.justOrEmpty(request.headers().firstHeader(HttpHeaders.AUTHORIZATION))
                .doOnNext(header -> log.debug("🔐 Authorization header found: {}", header))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> {
                    String token = authHeader.substring(7);
                    log.info("🔑 Extracted JWT token (first 10 chars): {}", token.substring(0, Math.min(10, token.length())));
                    return token;
                })
                .switchIfEmpty(Mono.error(new MissingInvalidAuthHeaderException("Missing or invalid Authorization header")));
    }

    private Mono<ServerResponse> buildSuccessResponse(ReportSummary report) {
        log.info("Generated report ");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapToDTO(report));
    }

    private ReportDTO mapToDTO(ReportSummary report) {
        return ReportDTO.builder()
                .quantity(report.getQuantity())
                .amountBorrowed(report.getAmountBorrowed())
                .build();
    }
}
