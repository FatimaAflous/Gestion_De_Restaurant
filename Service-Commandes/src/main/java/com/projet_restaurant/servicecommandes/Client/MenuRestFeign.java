package com.projet_restaurant.servicecommandes.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "MENU-SERVICE", url = "http://localhost:8888/MENU-SERVICE", configuration = FeignConfig.class)
public interface MenuRestFeign {
    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> executeGraphQLQuery(@RequestBody Map<String, Object> request);
}
