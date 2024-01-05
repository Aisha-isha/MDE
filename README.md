# Config2Compose

<img src="imgs/config2compose.jpg" width="600">


# Introduction

Welcome to Config2Compose; a Model-Driven Engineering approach to simplify the containerization & orchestration process for Spring Boot + database applications. 

# Problem

Tired of writing your docker compose files by hand? Tired of having to look up the docker compose file structure each time you have to write it? Fret not! For Config2Compose is here to simplify and avoid wasting time where it doesn't need to be!

# Our Solution

Config2Compose, a fairly easy to use web interface where the user can upload their maven config files (application.properties + dockerfiles), and the system; a model-driven approach, transforms these files into their corresponding Docker compose file to simplify the orchestration process. \(^o^)/**

# Architecture of our Solution

The application architecture revolves around a user-friendly single-page web interface and a robust backend built with Java and the Spring Boot framework. The front-end, developed using HTML and CSS, offers a seamless experience for users to upload their Maven configuration files, including application.properties and Dockerfiles. On the backend, a Spring Boot application manages user requests and file uploads, employing a model-driven approach to intelligently transform Maven configuration files into corresponding Docker Compose files.


<img src="imgs/index.png" width="600">

# Metamodels
## 🎆Configuration File Metamodel
<img src="imgs/configMetamodel.png" width="600">

## 🎆Docker compose Metamodel
<img src="imgs/composeMetamodel.png" width="600">

# Transformation
In order to achieve our desired outcomes using the model-driven approach, we need to make configure the Model-to-Model and Model-to-Text transformations that will transfrom our metamodels from config files to Docker compose file.

## Model-to-Model Transformation

It is the transformation that occurs from the config file metamodel to the docker compose metamodel, done using ETL (Epsilon Transformation Language), it takes as output the metamodels, the ETL transformation rules and the config file model instance. 
Using Epsilon Playground, we got the following Target Model:
<img src="imgs/m2m.jpg" width="900">

## Model-to-Text Transformation

The transformation that takes us from the Docker compose file to the Docker compose file as in text. It was done using EGL (Epsilon Generation Language). Its output is the final Docker compose file as shown below:
<img src="imgs/m2t.jpg" width="700">

## Conclusion
In summary, this application seamlessly integrates an easy-to-use web interface with a robust backend, providing users with an easy solution to generate Docker Compose files from Maven configuration files.
This application utilizes a model-driven engineering (MDE) process that includes the creation of a Compose metamodel and a configuration file metamodel, and utilizes Epsilon for model-to-model and model-to-text translation.
The application automates the generation of the docker-compose.yml file.
This approach, based on MDE principles, ensures efficiency, accuracy, and adaptability in orchestrating  Docker environments based on user-provided Maven configurations and Docker files.




