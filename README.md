System Project

This project is configured to support Maven and Gradle.

Maven Configuration
    To run the main within Runner simply add a Maven command. 
    Title it Run and set command line to 
        clean install && java -jar ${current.project.path}/target/*jar-with-dependencies.jar
        
    To build the project with Maven simply use the standard maven command and title it Build
    
Gradlew
    To run the main within Runner simply add a Custom command.
    Name it Gradle Run with command line 
        cd ${current.project.path} && ./gradlew run
    
    To build the project with Gradle simply use the custom command and name it Gradle Build with command line
        cd ${current.project.path} && ./gradlew build