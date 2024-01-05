package com.example.demo1.service;

import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ParserService {
	private String portValue;
    private String urlValue;
    private String usernameValue;
    private String passwordValue;
    public String host;
    public String port_db;
    public String type_db;
    public String db;
    private List<String> images;
    private List<String> entrypoints;


    

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
    	    this.portValue = (portValue != null) ? portValue : "8080";
            this.urlValue = urlValue;
            this.usernameValue = usernameValue;
            this.passwordValue = passwordValue;



    	    // Extraction d'informations spécifiques de l'URL JDBC
    	    if (urlValue != null) {
    	        if (urlValue.startsWith("jdbc:mysql")) {
    	            String[] parts = urlValue.split(":");
    	            String host = parts[2].substring(2);
    	            String[] subParts = parts[3].split("/");
    	            String port_db = subParts[0];
    	            String type_db = "mysql";
    	            String db= subParts[1];
    	            this.type_db = type_db;
    	            this.port_db = port_db;
    	            this.db=db;

    	            // Affichage des valeurs extraites


    	        } else if (urlValue.startsWith("jdbc:oracle")) {
    	            String[] parts = urlValue.split(":");
    	            String host = parts[3].substring(1);
    	            String port_db = parts[4];
    	            String type_db = "oracle";
    	            String db=parts[4];
    	            
    	            this.db = db;
    	            this.type_db = type_db;
    	            this.port_db = port_db;
    	            

    	            // Affichage des valeurs extraites

    	        } else {
    	            System.out.println("Unsupported database URL.");
    	        }
    	    } else {
    	        System.out.println("Database URL not found in the properties.");
    	    }

    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	deleteFile(filePath);
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
                this.images = images;
                this.entrypoints = entrypoints;



            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteFile(filePathDocker);
        }
    }
    public void confFile() {
        
        String filePath = "src/main/resources/static/uploads/config.flexmi";
        
        try {
            // Create FileWriter and PrintWriter objects
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Maintenant, vous pouvez accéder en toute sécurité aux listes
            printWriter.println("?nsuri: maven");
            printWriter.println();
            printWriter.println("configFile:");
            printWriter.println("  Docker_values:");
            String originalValue = "[\"java\",\"-jar\",\"/app.jar\"]";
            String transformedValue = originalValue.replace("\"", "'");
            printWriter.println("    entrypoint: " +"\"" + transformedValue+"\"");
            printWriter.println("    image_backend: " +"\"" + images.get(0) + "\"");
            printWriter.println("    image_db: \"mysql:8.0\"");
            printWriter.println("  Properties:");
            printWriter.println("    port: " + portValue);
            printWriter.println("    Datasource:");
            printWriter.println("      type: " + "\"" + type_db + "\"");
            printWriter.println("      url: " +"\"" + urlValue+ "\"");
            printWriter.println("      port: " + "\""+port_db+"\"");
            printWriter.println("      database: " +"\""+ db + "\"");
            printWriter.println("      username: " + "\""+ usernameValue+ "\"");
            printWriter.println("      password: " + "\""+ passwordValue+"\"");

            // Fermez les ressources
            printWriter.close();
            fileWriter.close();

            System.out.println("File 'config.txt' created successfully at: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error creating 'config.flexmi' file.");
        }
    }
    private void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
            System.out.println("File deleted successfully: " + filePath);
        } catch (IOException e) {
            System.out.println("Error deleting the file: " + filePath);
            e.printStackTrace();
        }
    }


}
