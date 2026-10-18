package com.example.firstspringprjct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/city")
public class UserService {

    public String city = "Bengaluru";

    @GetMapping
    public String getCity()
    {
        return city;
    }

    @PostMapping("/newcity")
    public String newcity(String newcity)
    {
        city = newcity;
        return city;
    }

}
