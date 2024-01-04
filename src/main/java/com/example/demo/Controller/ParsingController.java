package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ParserService;

@RestController
@RequestMapping("/api")
public class ParsingController {

    private final ParserService parserService;

    @Autowired
    public ParsingController(ParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping("/par")
    public String parseProperties() {
        parserService.parseFile();
        return "Check the console for parsed properties";
    }
}
