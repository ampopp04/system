System Project

This project is configure to support Maven and Gradle.

To run the main within Runner simply add a Maven command. 
Title it Run and set command line to 
    clean install && java -jar ${current.project.path}/target/*jar-with-dependencies.jar
    
To build the project with Maven simply use the standard maven command and title it Build