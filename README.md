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




