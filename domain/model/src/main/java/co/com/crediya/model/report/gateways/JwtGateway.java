package co.com.crediya.model.report.gateways;

import co.com.crediya.model.report.UserTokenInfo;
import reactor.core.publisher.Mono;

public interface JwtGateway {
    Mono<UserTokenInfo> validateToken(String token);
}
