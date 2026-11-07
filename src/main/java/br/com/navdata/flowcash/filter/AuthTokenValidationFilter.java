package br.com.navdata.flowcash.filter;

import br.com.navdata.flowcash.service.AuthApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Component
public class AuthTokenValidationFilter extends OncePerRequestFilter {

    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final String REGISTER_COMPANY_PATH = "/api/v1/auth/register-company";
    private static final String SYSTEM_NAME_HEADER = "X-System-Name";

    private final AuthApiClient authApiClient;
    private final ObjectMapper objectMapper;

    public AuthTokenValidationFilter(AuthApiClient authApiClient, ObjectMapper objectMapper) {
        this.authApiClient = authApiClient;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return request.getDispatcherType() == DispatcherType.ERROR
                || "OPTIONS".equalsIgnoreCase(request.getMethod())
                || ("POST".equalsIgnoreCase(request.getMethod())
                    && (LOGIN_PATH.equals(path) || REGISTER_COMPANY_PATH.equals(path)));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")
                || authorization.substring(7).isBlank()) {
            writeError(response, HttpStatus.UNAUTHORIZED, "Token de autenticação ausente ou inválido");
            return;
        }

        String systemName = request.getHeader(SYSTEM_NAME_HEADER);
        if (systemName == null || systemName.isBlank()) {
            writeError(response, HttpStatus.BAD_REQUEST, "Header X-System-Name não informado");
            return;
        }

        String token = authorization.substring(7).trim();
        try {
            ResponseEntity<String> validation = authApiClient.validateToken(token, systemName);
            if (!validation.getStatusCode().is2xxSuccessful()) {
                writeResponse(response, validation);
                return;
            }

            if (validation.getBody() == null || validation.getBody().isBlank()) {
                writeError(response, HttpStatus.BAD_GATEWAY, "Resposta inválida do serviço NavData Auth");
                return;
            }
            JsonNode body = objectMapper.readTree(validation.getBody());
            if (!body.path("valid").asBoolean(false)) {
                writeResponse(response, validation, HttpStatus.UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);
        } catch (ResponseStatusException exception) {
            String message = exception.getReason() != null ? exception.getReason() : "Falha ao validar token no NavData Auth";
            writeError(response, HttpStatus.valueOf(exception.getStatusCode().value()), message);
        } catch (JsonProcessingException exception) {
            writeError(response, HttpStatus.BAD_GATEWAY, "Resposta inválida do serviço NavData Auth");
        }
    }

    private void writeResponse(HttpServletResponse response, ResponseEntity<String> validation) throws IOException {
        writeResponse(response, validation, HttpStatus.valueOf(validation.getStatusCode().value()));
    }

    private void writeResponse(
            HttpServletResponse response,
            ResponseEntity<String> validation,
            HttpStatus status
    ) throws IOException {
        response.setStatus(status.value());
        MediaType contentType = validation.getHeaders().getContentType();
        response.setContentType((contentType != null ? contentType : MediaType.APPLICATION_JSON).toString());
        if (validation.getBody() != null) {
            response.getWriter().write(validation.getBody());
        }
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("message", message)));
    }
}
