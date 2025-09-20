package com.notwork.notwork_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hi")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", "200");
        result.put("msg", "success");
        result.put("data", "Hello world");
        return result;
    }
}
