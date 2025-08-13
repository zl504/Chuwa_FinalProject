package com.example.orderservice.client;


import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountClient {
    @GetMapping("/account/me")
    MeResponse me(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);

    class MeResponse {
        private Long id;
        private String username;
        // getters/setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String u) { this.username = u; }
    }
}
