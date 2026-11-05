package br.com.navdata.flowcash.filter;

import br.com.navdata.flowcash.service.AuthApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthTokenValidationFilterTest {

    private AuthApiClient authApiClient;
    private AuthTokenValidationFilter filter;

    @BeforeEach
    void setUp() {
        authApiClient = mock(AuthApiClient.class);
        filter = new AuthTokenValidationFilter(authApiClient, new ObjectMapper());
    }

    @Test
    void validTokenContinuesRequest() throws Exception {
        MockHttpServletRequest request = authorizedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        when(authApiClient.validateToken("token-value", "FlowCash"))
                .thenReturn(ResponseEntity.ok("{\"valid\":true,\"email\":\"user@example.com\"}"));

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void invalidTokenReturnsAuthResponseAndStopsRequest() throws Exception {
        MockHttpServletRequest request = authorizedRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        String authError = "{\"valid\":false,\"email\":null}";
        when(authApiClient.validateToken("token-value", "FlowCash"))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authError));

        filter.doFilter(request, response, chain);

        verifyNoInteractions(chain);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(authError, response.getContentAsString());
    }

    @Test
    void loginDoesNotRequireExistingToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verifyNoInteractions(authApiClient);
    }

    private MockHttpServletRequest authorizedRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/accounts");
        request.addHeader("Authorization", "Bearer token-value");
        request.addHeader("X-System-Name", "FlowCash");
        return request;
    }
}
