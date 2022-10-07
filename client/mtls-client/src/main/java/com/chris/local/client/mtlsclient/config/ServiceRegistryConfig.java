package com.chris.local.client.mtlsclient.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class ServiceRegistryConfig {

    @Value("${server.base-url}")
    private String baseUrl;

    @Value("${server.endpoint}")
    private String endpoint;

    @Bean
    public WebClient configureWebclient (@Value("${server.ssl.trust-store}") String trustStorePath,
                                         @Value("${server.ssl.trust-store-password}") String trustStorePass,
                                         @Value("${server.ssl.key-store}") String keyStorePath,
                                         @Value("${server.ssl.key-store-password}") String keyStorePass,
                                         @Value("${server.ssl.key-alias}") String keyAlias) {
        SslContext sslContext;
        final PrivateKey privateKey;
        final X509Certificate[] certificates;

        try {
            final KeyStore trustStore;
            final KeyStore keyStore;

            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(ResourceUtils.getFile(keyStorePath)), keyStorePass.toCharArray());

            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(ResourceUtils.getFile(trustStorePath)), trustStorePass.toCharArray());

            List<Certificate> certificateList =
                    Collections.list(trustStore.aliases()).stream()
                            .filter(
                                    t -> {
                                        try {
                                            return trustStore.isCertificateEntry(t);
                                        } catch (KeyStoreException exception) {
                                            throw new RuntimeException("Error reading truststore", exception);
                                        }
                                    })
                            .map(
                                    t -> {
                                        try {
                                            return trustStore.getCertificate(t);
                                        } catch (KeyStoreException exception) {
                                            throw new RuntimeException("Error reading truststore", exception);
                                        }
                                    })
                            .collect(Collectors.toList());

            certificates = certificateList.toArray(new X509Certificate[certificateList.size()]);
            privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyStorePass.toCharArray());

            Certificate[] certChain = keyStore.getCertificateChain(keyAlias);
            X509Certificate[] x509CertificatesChain =
                    Arrays.stream(certChain)
                            .map(certificate -> (X509Certificate) certificate)
                            .collect(Collectors.toList())
                            .toArray(new X509Certificate[certChain.length]);

            X509Certificate certificate = x509CertificatesChain[0];

            validateCertificate(certificate);

            sslContext = SslContextBuilder.forClient()
                    .keyManager(privateKey, keyStorePass, x509CertificatesChain)
                    .trustManager(certificates)
                    .build();

            HttpClient httpClient =
                    HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

            return webClientConfiguration(httpClient);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateCertificate(X509Certificate certificate) {
        var certificateExpirationsDate =
                certificate.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var certificateStartDate =
                certificate.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (LocalDate.now().isAfter(certificateExpirationsDate)) {
            throw new RuntimeException("Service date expiration");
        }

        if (LocalDate.now().isBefore(certificateStartDate)) {
            throw new RuntimeException("Service cannot be used until " + certificateStartDate.toString());
        }

        var subject = Arrays.stream(certificate.getSubjectDN().getName().split(","))
                .map(i -> i.split("="))
                .collect(Collectors.toMap(element -> element[0].trim(), element -> element[1].trim()));
        if (!subject.get("O").equalsIgnoreCase("Chris")) {
            throw new RuntimeException("Organization is not correct");
        }
        return true;
    }

    private WebClient webClientConfiguration(HttpClient httpClient) {
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        var webClient =
                WebClient.builder()
                        .clientConnector(connector)
                        .baseUrl(baseUrl)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();

//        var response =
//                webClient.get().uri(endpoint).retrieve()
//                        .bodyToMono(String.class)
//                        .block();
//
//        assert Objects.requireNonNull(response).equalsIgnoreCase("verified");

        return webClient;
    }

}
