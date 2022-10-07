package com.chris.local.server.mtlsserver.expose;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceVerificationResource {

    @GetMapping("certificate-verify")
    public ResponseEntity<String> tlsServiceVerification() {
        return ResponseEntity.ok("verified");
    }


}
