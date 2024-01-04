package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class ParserService {

    public void parseFile() {
        String filePath = "src/main/resources/static/uploads/application.properties";

        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);

            // Récupération de la valeur associée à la propriété "server.port" avec une valeur par défaut de 8080
            String portValue = properties.getProperty("server.port", "8080");
            String urlValue = properties.getProperty("spring.datasource.url");
            String usernameValue = properties.getProperty("spring.datasource.username");
            String passwordValue = properties.getProperty("spring.datasource.password");

            System.out.println("Port value: " + portValue);
            System.out.println("URL value: " + urlValue);
            System.out.println("Username value: " + usernameValue);
            System.out.println("Password value: " + passwordValue);

            // Vous pouvez stocker ces valeurs dans des variables si nécessaire.
            
            // Vous pouvez également parcourir toutes les propriétés
            properties.forEach((key, value) -> System.out.println(key + ": " + value));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
