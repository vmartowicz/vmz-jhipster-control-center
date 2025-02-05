package tech.jhipster.controlcenter.config.apidoc;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springdoc.core.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.swagger.web.SwaggerResource;
//import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import tech.jhipster.config.JHipsterConstants;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL;

/**
 * jhcc-custom
 * Retrieves all registered microservices Swagger resources.
 */
@Component
@Primary
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
@Configuration
//@EnableOpenApi
@OpenAPIDefinition
public class SwaggerConfiguration /*implements SwaggerResourcesProvider*/ {

    private final RouteLocator routeLocator;

    public SwaggerConfiguration(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Bean
    @Lazy(false)
    public Set<SwaggerUrl> apis(RouteDefinitionLocator locator/*, SwaggerUiConfigProperties swaggerUiConfigProperties*/) {

        Set<SwaggerUrl> urls = new HashSet<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions
            .stream()
            //.filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
            .forEach(routeDefinition -> {
                String service = routeDefinition.getId().split("/")[0];
                SwaggerUrl swaggerDefaultUrl = new SwaggerUrl(service + " (default)", "/gateway/" + service + DEFAULT_API_DOCS_URL + "/springdocDefault", null);
                SwaggerUrl swaggerManagementUrl = new SwaggerUrl(service + " (management)", "/gateway/" + service + DEFAULT_API_DOCS_URL + "/management", null);
                urls.addAll(List.of(swaggerDefaultUrl, swaggerManagementUrl));
            });
        //swaggerUiConfigProperties.setUrls(urls);
        return urls;
    }


/*
    @Qualifier("swaggerResources")
    private final SwaggerResourcesProvider controlCenterSwaggerResources;

    public SwaggerConfiguration(RouteLocator routeLocator, SwaggerResourcesProvider swaggerResources) {
        this.routeLocator = routeLocator;
        this.controlCenterSwaggerResources = swaggerResources;
    }

    @Override
    public List<SwaggerResource> get() {
        // Get control center swagger resources and make their names more explicit
        List<SwaggerResource> allSwaggerResources = controlCenterSwaggerResources.get();
        allSwaggerResources.get(0).setName(String.format("jhipster-control-center (%s)", allSwaggerResources.get(0).getName()));
        allSwaggerResources.get(1).setName(String.format("jhipster-control-center (%s)", allSwaggerResources.get(1).getName()));

        List<Tuple2<Route, List<SwaggerResource>>> servicesRouteSwaggerResources = routeLocator
            .getRoutes()
            .filterWhen(
                route -> {
                    String routePredicate = route.getPredicate().toString();
                    // Ignore the Consul server from the list as it doesn't expose a /swagger-resources endpoint
                    return Mono.just(!routePredicate.contains("consul/consul"));
                }
            )
            .flatMap(
                route -> {
                    // Retrieve the list of available OpenAPI resources for each service from their /swagger-resources endpoint
                    WebClient serviceClient = WebClient.builder().baseUrl(route.getUri().toString()).build();
                    Mono<List<SwaggerResource>> swaggerResources = serviceClient
                        .get()
                        .uri("/swagger-resources")
                        .retrieve()
                        .bodyToFlux(SwaggerResource.class)
                        .onErrorResume(exception -> Mono.empty())
                        .collectList()
                        .defaultIfEmpty(Collections.emptyList());
                    return Mono.just(route).zipWith(swaggerResources);
                }
            )
            .collectList()
            .defaultIfEmpty(Collections.emptyList())
            .subscribeOn(Schedulers.boundedElastic())
            .toFuture()
            .orTimeout(10, TimeUnit.SECONDS)
            .join();

        //Add the registered microservices swagger docs as additional swagger resources
        List<SwaggerResource> servicesSwaggerResources = servicesRouteSwaggerResources
            .stream()
            .filter(tuple -> !tuple.getT2().isEmpty())
            .map(
                tuple -> {
                    Route route = tuple.getT1();
                    String routePredicate = route.getPredicate().toString();
                    List<SwaggerResource> swaggerResources = tuple.getT2();
                    List<SwaggerResource> swaggerResourcesFinal = new ArrayList<>();
                    for (SwaggerResource swaggerResource : swaggerResources) {
                        String patchedSwaggerPath = routePredicate
                            .substring(routePredicate.indexOf("[") + 1, routePredicate.indexOf("]"))
                            .replace("/**", swaggerResource.getUrl());
                        swaggerResource.setName(route.getId() + " (" + swaggerResource.getName() + ")");
                        swaggerResource.setUrl(patchedSwaggerPath);
                        swaggerResourcesFinal.add(swaggerResource);
                    }
                    return swaggerResourcesFinal;
                }
            )
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        allSwaggerResources.addAll(servicesSwaggerResources);
        return allSwaggerResources;
    }*/
}
