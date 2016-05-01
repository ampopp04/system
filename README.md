System Project

This project uses Gradle. The pom.xml is only for Maven that is required by Codenvy

# Commands

| #       | Command           | 
| :------------- |:------------- |
| 1      | `cd ${current.project.path} && gradle build && java -jar build/libs/*.jar` |

# App output

App output is streamed into a console. Note that if your app expects user input, do not use command but execute jars in the terminal directly.
