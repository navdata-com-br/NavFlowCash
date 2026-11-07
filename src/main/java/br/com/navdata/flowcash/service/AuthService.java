package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.request.LoginRequest;
import br.com.navdata.flowcash.dto.request.RegisterCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthApiClient authApiClient;

    public ResponseEntity<String> login(LoginRequest request, String systemName) {
        return authApiClient.login(request, systemName);
    }

    public ResponseEntity<String> registerCompany(RegisterCompanyRequest request, String systemName) {
        return authApiClient.registerCompany(request, systemName);
    }

    public ResponseEntity<String> getAuthenticatedUser(String authorization, String systemName) {
        return authApiClient.getAuthenticatedUser(authorization, systemName);
    }
}
