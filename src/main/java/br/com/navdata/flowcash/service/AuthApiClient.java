package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.request.LoginRequest;
import br.com.navdata.flowcash.dto.request.PaymentTransactionRequest;
import br.com.navdata.flowcash.dto.request.RegisterCompanyRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class AuthApiClient {

    private static final String PAYMENTS_PATH = "/api/v1/payments";
    private static final String AUTH_LOGIN_PATH = "/api/v1/auth/login";
    private static final String AUTH_REGISTER_COMPANY_PATH = "/api/v1/auth/register-company";
    private static final String AUTH_ME_PATH = "/api/v1/auth/me";
    private static final String AUTH_VALIDATE_PATH = "/api/v1/auth/validate";

    private final RestTemplate restTemplate;
    private final String authApiBaseUrl;

    public AuthApiClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${auth.api.base-url}") String authApiBaseUrl
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.authApiBaseUrl = authApiBaseUrl.replaceAll("/+$", "");
    }

    public ResponseEntity<String> login(LoginRequest login, String systemName) {
        URI uri = UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(AUTH_LOGIN_PATH)
                .build()
                .toUri();
        return forward(
                HttpMethod.POST,
                uri,
                java.util.Map.of("email", login.email(), "password", login.password()),
                null,
                systemName
        );
    }

    public ResponseEntity<String> registerCompany(RegisterCompanyRequest request, String systemName) {
        URI uri = UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(AUTH_REGISTER_COMPANY_PATH)
                .build()
                .toUri();
        return forward(HttpMethod.POST, uri, request, null, systemName);
    }

    public ResponseEntity<String> validateToken(String token, String systemName) {
        URI uri = UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(AUTH_VALIDATE_PATH)
                .build()
                .toUri();
        return forward(
                HttpMethod.POST,
                uri,
                java.util.Map.of("token", token),
                null,
                systemName
        );
    }

    public ResponseEntity<String> getAuthenticatedUser(String authorization, String systemName) {
        URI uri = UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(AUTH_ME_PATH)
                .build()
                .toUri();
        return forward(HttpMethod.GET, uri, null, authorization, systemName);
    }

    public ResponseEntity<String> createPayment(
            PaymentTransactionRequest payment,
            String authorization,
            String systemName
    ) {
        return forward(HttpMethod.POST, paymentsUri(), payment, authorization, systemName);
    }

    public ResponseEntity<String> listByCompany(
            Integer systemUnitId,
            String authorization,
            String systemName
    ) {
        URI uri = UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(PAYMENTS_PATH)
                .pathSegment("company", systemUnitId.toString())
                .build()
                .encode()
                .toUri();
        return forward(HttpMethod.GET, uri, null, authorization, systemName);
    }

    public ResponseEntity<String> listBySystem(String systemName, String authorization) {
        URI uri = UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(PAYMENTS_PATH)
                .pathSegment("system", systemName)
                .build()
                .encode()
                .toUri();
        return forward(HttpMethod.GET, uri, null, authorization, systemName);
    }

    private URI paymentsUri() {
        return UriComponentsBuilder.fromUriString(authApiBaseUrl)
                .path(PAYMENTS_PATH)
                .build()
                .toUri();
    }

    private ResponseEntity<String> forward(
            HttpMethod method,
            URI uri,
            Object body,
            String authorization,
            String systemName
    ) {
        HttpHeaders headers = new HttpHeaders();
        if (authorization != null && !authorization.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        headers.set("X-System-Name", systemName);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        if (body != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    method,
                    new HttpEntity<>(body, headers),
                    String.class
            );
            return proxyResponse(response.getStatusCode(), response.getHeaders(), response.getBody());
        } catch (HttpStatusCodeException exception) {
            return proxyResponse(
                    exception.getStatusCode(),
                    exception.getResponseHeaders(),
                    exception.getResponseBodyAsString()
            );
        } catch (ResourceAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Não foi possível conectar ao serviço NavData Auth",
                    exception
            );
        }
    }

    private ResponseEntity<String> proxyResponse(
            HttpStatusCode status,
            HttpHeaders headers,
            String body
    ) {
        MediaType contentType = headers != null ? headers.getContentType() : null;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(contentType != null ? contentType : MediaType.APPLICATION_JSON);
        if (headers != null && headers.containsKey(HttpHeaders.SET_COOKIE)) {
            responseHeaders.put(HttpHeaders.SET_COOKIE, headers.get(HttpHeaders.SET_COOKIE));
        }
        return new ResponseEntity<>(body, responseHeaders, status);
    }
}
