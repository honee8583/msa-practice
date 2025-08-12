package com.farmdora.farmdoraproductservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/product-service")  // http://localhost:8081/product-service/welcome
@RequiredArgsConstructor
public class ProductController {

    private final Environment env;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Product Service!";
    }
}
