package co.com.crediya.api;

import co.com.crediya.api.config.ReportPath;
import co.com.crediya.api.openapi.ReportApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RouterRest {

    private final ReportPath reportPath;
    private final Handler handler;


    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route()
                .GET(reportPath.getReport(), handler::getReport, ReportApiDoc::getReportDoc)
                .build();
    }
}
