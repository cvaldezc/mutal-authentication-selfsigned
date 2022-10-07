package com.chris.local.client.mtlsclient.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ServiceClient {

    @Value("${server.endpoint}")
    private String endpoint;

    private final WebClient webClient;

    public ServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getClient() {
        return webClient.get().uri(endpoint).retrieve()
                .bodyToMono(String.class);
        //return webClient.get().retrieve().bodyToMono(String.class);
    }

}
