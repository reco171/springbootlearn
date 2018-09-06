package com.reco.security.method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by root on 9/6/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleMethodSecurityApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHome() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        ResponseEntity<String> entity = this.restTemplate.exchange("/", HttpMethod.GET,
                new HttpEntity<Void>(headers), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).contains("<title>Login");
    }

    @Test
    public void testLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("username", "admin");
        form.set("password", "admin");
        getCsrf(form, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange("/login",
                HttpMethod.POST, new HttpEntity<>(form, headers), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(entity.getHeaders().getLocation().toString())
                .isEqualTo("http://localhost:" + this.port + "/");
    }

    @Test
    public void testDenied() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("username", "user");
        form.set("password", "user");
        getCsrf(form, headers);
        ResponseEntity<String> entity = this.restTemplate.exchange("/login",
                HttpMethod.POST, new HttpEntity<>(form, headers), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        String cookie = entity.getHeaders().getFirst("Set-Cookie");
        headers.set("Cookie", cookie);
        ResponseEntity<String> page = this.restTemplate.exchange(
                entity.getHeaders().getLocation(), HttpMethod.GET,
                new HttpEntity<Void>(headers), String.class);
        assertThat(page.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(page.getBody()).contains("Access denied");
    }

    @Test
    public void testManagementProtected() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        ResponseEntity<String> entity = this.restTemplate.exchange("/actuator/beans",
                HttpMethod.GET, new HttpEntity<Void>(headers), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testManagementAuthorizedAccess() {
        BasicAuthorizationInterceptor basicAuthInterceptor = new BasicAuthorizationInterceptor(
                "admin", "admin");
        this.restTemplate.getRestTemplate().getInterceptors().add(basicAuthInterceptor);
        try {
            ResponseEntity<String> entity = this.restTemplate
                    .getForEntity("/actuator/beans", String.class);
            assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
        finally {
            this.restTemplate.getRestTemplate().getInterceptors()
                    .remove(basicAuthInterceptor);
        }
    }

    private void getCsrf(MultiValueMap<String, String> form, HttpHeaders headers) {
        ResponseEntity<String> page = this.restTemplate.getForEntity("/login",
                String.class);
        String cookie = page.getHeaders().getFirst("Set-Cookie");
        headers.set("Cookie", cookie);
        String body = page.getBody();
        Matcher matcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*")
                .matcher(body);
        matcher.find();
        form.set("_csrf", matcher.group(1));
    }

}