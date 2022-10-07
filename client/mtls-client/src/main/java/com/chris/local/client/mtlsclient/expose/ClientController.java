package com.chris.local.client.mtlsclient.expose;

import com.chris.local.client.mtlsclient.service.ServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {

    private final ServiceClient serviceClient;

    public ClientController(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }


    @GetMapping("verify")
    public Mono<String> verify() {
        return this.serviceClient.getClient();
    }

}
