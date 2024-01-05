package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileWriter;
import java.io.PrintWriter;

@Service
public class ParserService {
	private String portValue;
    private String urlValue;
    private String usernameValue;
    private String passwordValue;
    private List<String> images;
    private List<String> entrypoints;


    public String getPortValue() {
        return portValue;
    }

    public void setPortValue(String portValue) {
        this.portValue = portValue;
    }

    public String getUrlValue() {
        return urlValue;
    }

    public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
    }

    public String getUsernameValue() {
        return usernameValue;
    }

    public void setUsernameValue(String usernameValue) {
        this.usernameValue = usernameValue;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getEntryPoints() {
        return entrypoints;
    }

    public void setEntryPoints(List<String> entrypoints) {
        this.entrypoints = entrypoints;
    }

    public void parsePropretiesFile() {
    	
    	String filePath = "src/main/resources/static/uploads/application.properties";

    	Properties properties = new Properties();

    	try (FileInputStream input = new FileInputStream(filePath)) {
    	    properties.load(input);
    	    // Récupération de la valeur associée à la propriété "server.port" avec une valeur par défaut de 8080
    	    String portValue = properties.getProperty("server.port");
    	    if (portValue==null) {
    	    	portValue= "8080";
    	    }
    	    String urlValue = properties.getProperty("spring.datasource.url");
    	    String usernameValue = properties.getProperty("spring.datasource.username");
    	    String passwordValue = properties.getProperty("spring.datasource.password");

    	    System.out.println("Port value: " + portValue);
    	    System.out.println("URL value: " + urlValue);
    	    System.out.println("Username value: " + usernameValue);
    	    System.out.println("Password value: " + passwordValue);

    	    // Extraction d'informations spécifiques de l'URL JDBC
    	    if (urlValue != null) {
    	        if (urlValue.startsWith("jdbc:mysql")) {
    	            String[] parts = urlValue.split(":");
    	            String host = parts[2].substring(2);
    	            String[] subParts = parts[3].split("/");
    	            String port_db = subParts[0];
    	            String type_db = "mysql";

    	            // Affichage des valeurs extraites
    	            System.out.println("Host: " + host);
    	            System.out.println("Port_db: " + port_db);
    	            System.out.println("Type_db: " + type_db);
    	        } else if (urlValue.startsWith("jdbc:oracle")) {
    	            String[] parts = urlValue.split(":");
    	            String host = parts[3].substring(1);
    	            String port_db = parts[4];
    	            String type_db = "oracle";

    	            // Affichage des valeurs extraites
    	            System.out.println("Host : " + host);
    	            System.out.println("Port_db: " + port_db);
    	            System.out.println("Type_db: " + type_db);
    	        } else {
    	            System.out.println("Unsupported database URL.");
    	        }
    	    } else {
    	        System.out.println("Database URL not found in the properties.");
    	    }

    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	



    }
    public void parseDockerFile(int numFiles) {
        List<String> images = new ArrayList<>();
        List<String> entrypoints = new ArrayList<>();

        for (int i = 0; i < numFiles-1; i++) {
            String filePathDocker;

            if (i == 0) {
                filePathDocker = "src/main/resources/static/uploads/DockerFile";
            } else {
                filePathDocker = "src/main/resources/static/uploads/DockerFile" + i;
            }

            try (FileInputStream inputDockerFile = new FileInputStream(filePathDocker)) {
                int content;
                StringBuilder dockerFileContent = new StringBuilder();

                while ((content = inputDockerFile.read()) != -1) {
                    dockerFileContent.append((char) content);
                }

                String[] lines = dockerFileContent.toString().split("\\r?\\n");

                String image = null;
                String entrypoint = null;

                for (String line : lines) {
                    if (line.startsWith("FROM")) {
                        // Extract image from FROM instruction
                        image = line.substring(line.indexOf(" ") + 1).trim();
                    } else if (line.startsWith("ENTRYPOINT") || line.startsWith("CMD")) {
                        entrypoint = line.substring(line.indexOf("[")).replaceAll("\\s+", "");
                    }
                }

                // Check if entrypoint is null and set it to an empty string if needed
                entrypoint = (entrypoint == null) ? "" : entrypoint;

                images.add(image);
                entrypoints.add(entrypoint);

                System.out.println("Image for file " + i + ": " + images.get(i));
                System.out.println("Entrypoint for file " + i + ": " + entrypoints.get(i));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void confFile() {
        
        String filePath = "src/main/resources/static/uploads/config.txt";
        
        try {
            // Create FileWriter and PrintWriter objects
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Maintenant, vous pouvez accéder en toute sécurité aux listes
            printWriter.println("?nsuri: maven");
            printWriter.println();
            printWriter.println("configFile:");
            printWriter.println("  Docker_values:");
            printWriter.println("    entrypoint: ");
            printWriter.println("    image_backend: ");
            printWriter.println("    image_db: \"mysql:8.0\"");
            printWriter.println("  Properties:");
            printWriter.println("    port: 8087");
            printWriter.println("    Datasource:");
            printWriter.println("      type: \"mysql\"");
            printWriter.println("      url: \"jgrekgjvrekjgvvfdvfdbgfnjht\"");
            printWriter.println("      port: \"4040\"");
            printWriter.println("      database: \"db\"");
            printWriter.println("      username: \"amila_one\"");
            printWriter.println("      password: \"Amila_pw\"");

            // Fermez les ressources
            printWriter.close();
            fileWriter.close();

            System.out.println("File 'config.txt' created successfully at: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating 'config.txt' file.");
        }
    }


}
