# Java Enterprise System Framework

The Java Enterprise System Framework is the backbone for rapidly creating, testing, and deploying enterprise quality full-stack Java 9 applications. It is very generic and flexible while also being designed for rigorous enterprise application development. It is best suited for internally consumed applications but this is by no means a strict requirement.

## NOTE

This is a framework, please use the job-tracker project for running and deploying an enterprise example instance.

[System Framework Example - Basic Project Management](https://github.com/ampopp04/job-tracker)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites
The following frameworks and tools are required to deploy a development instance of the System Framework.

#### Frameworks and Tools
The only major framework you will need is ExtJS. We are using an opensource license for this project to demonstrate
a possible UI solution that is fully dynamically generated. It is dynamically generated based on a UI database schema populated
via DTO analysis.

##### Java 9

This project requires Java 9.

##### ExtJs
The frontend UI is created using Javascript and a framework called ExtJS. To
compile the frontend UI code you will need the Sencha cmd tool installed. This
allows the build process to use this tool to build the UI code for you automatically.
```
https://www.sencha.com/products/extjs/cmd-download/
Click Download with JRE Included and install the tool.
```

##### Gradle
Gradle is the actual build tool. It does many tasks related to deployment. I have
created a single simple task for full deployments called “fullDeployableBuild” this
will build the UI code and compile all of the java code into a single runnable zip
file.

It also integrated with the next tool “IntelliJ IDEA” and allows you to click the
“bootRun” task which will run the entire application.

I have included the Gradle tool within the source code itself so you do not need to
download it independently.

##### IntelliJ IDEA
IntelliJ IDEA is an integrated development environment (IDE). This is like Visual
Studio for Visual Basic. It is a tool that allows you to easily code and create
applications written primarily in Java/Javascript while being fully integrated with
Gradle.

Download and install this IDE, you can use the free community edition or ultimate if you like.
```

https://www.jetbrains.com/idea/download/
Download and install, configure to have Gradle build
```

##### MySQL
MySQL database is used to store all of the database related tables and data.
This is not required to be installed on a development instance and is primarily
used for production.

I have built the system to be intelligent enough to detect whether you have
MySQL and if it is not setup it will run its own embedded database within the
application itself called H2. This embedded database is a lightweight version of
MySQL.

You do not have to do anything here because you can simply use the provided
embedded database that is managed for you automatically.

### Deployment
There are three main ways to run a new instance. 

* You can simply run the instance quickly via the gradle bootRun task.
* You can use Docker images
* You can use standard bat/sh zip distribution deployments with embedded tomcat

Below details these options in detail.

#### Running Locally

Install applications required from the Frameworks and Tools section.
2. Open IntelliJ IDEA
3. Click File -> New -> Project from Existing Sources…
4. Find the extracted root folder of the source code. Click Ok
5. Go to Gradle projects tab once it initializes everything.
6. Click the Gradle refresh icon
7. Click the runner module -> build folder -> build task, once complete click fullDeployableBuild and
bootRun.
8. You should have an instance of the runner running on
http://localhost:8080 - username: testuser password: password

#### Docker
Under the runner module you can see a Gradle tasks folder labeled docker. This docker folder contains tasks that allow you to build and push docker images. The default valuse are usually fine but you may want to refer to the docker plugin specification on how to configure the url you wish to push to. 

Below shows a simplified non-docker deployment approach.

#### Standard bat/sh Deployments

To run the application in your development environment go to the Gradle Projects tab
and click runner:bootRun under applications.
 
 If you have made UI changes you will want to run fullDeployableBuild task first to ensure your UI code is ‘compiled’.
 
If you want to deploy a new production instance then run fullDeployableBuild followed by
bootDistZip under application and distribution folders, respectively.
This will create a build directory at the root of the project. Within it there is a distributions
folder that contains a zip file of the new code.
Take it and unzip it on the server. 

Using a text editor open up the bin folder file
runner.bat (on Windows) and set
```
DEFAULT_JVM_OPTS=--add-modules java.xml.bind -Duser.timezone=UTC -d64 -server -Xmx4g -Xms4G -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=20 -XX:ConcGCThreads=5 -XX:InitiatingHeapOccupancyPercent=70
```

Replace %CMD_LINE_ARGS% with
```
cuba.reporting.openoffice.path=/Applications/LibreOffice.app/Contents/MacOS
```
Where the openoffice path points to your openoffice installation folder

The above -Xmx4g -Xms4G defines the Xmx max RAM used and Xms min application
RAM used.

An improvement would be to have the build process simply copy these properties over to
this file but that is slated for a later release.

Add the runner.bat file to your windows Task Scheduler to run on restart. You
can also double click this bat file to run the application.


## Frameworks and Dependencies Used

* Spring Boot
* Java 9
* JavaScript
* ExtJS 6
* Gradle
* Spring Suite
* MySQL/H2
* Flyway
* Docker (Not Required)
* Hibernate/JPA
* ByteBuddy
* logback
* lombok
* Jackson
* Embedded Tomcat 8
* JUnit
* EhCache
* HikariCP
* HATEOAS
* FreeMarker/SpEL
* yarg
* docx4j
* jasper

## Authors

* **Andrew Popp** - *Complete Work* - [CSExamples Blog](https://csexamples.com)

If you would like to contribute to this project please let me know! (ampopp04 at gmail.com)

## Upcoming Enhancements

* Core NoSQL/Cassandra/Mongo Module
* Enhance export module to execute FAX/EMAIL/FTP exports using Spring Batch with Spring Retry
* Add Angular Support with easy exclusion of ExtJS
* Clean up SchemaTableColumn class to split out UI specific logic
* Database Migration version number is universal to project, make module specific sub-numbering
* Always more tests!

## Brief Module Descriptions
This section are one liners that describe what each module encapsulates.

* core-util - All universal utility classes
* core-logging - Generic Exception handlers, logging configuration, and support
* core-manipulator - ByteBuddy wrapper for manipulating and generating bytecode for new or modified classes/objects
* core-inversion - Wrapper for Spring inversion control with support classes and class path interceptors
* core-db - The core DTO entity abstract schema and repository layouts
* core-db-migration - Flyway wrapper for handling automatic DTO table generation and special spring based Java migrations
* core-db-schema - Module for handling inversion of control of the database itself. Think Spring inversion control philosophy but for your database relationships.
* system-security - LDAP and Spring security module.
* system-bean - Exposes powerful generic class access through database definitions
* core-ws - Generic spring rest repository configuration, resolvers, and projectors
* system-expression - Generic database schema for saving, managing, and executing generic expressions against DTOs and SystemBeans.
* system-note - Note database schema for attaching notes to any entity within the system.
* system-export - Generic and flexible export module for executing any sort of task across any sort of transport and managing it
* core-ui - The UI database schema that is used to dynamically generate the UI based on metadata from the database
* runner - A basic runner that is not required but can be used to show the UI of the administrative management sections

## Screenshots


Login view of the application

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2011.53.26%20AM.png?raw=true?raw=true)

Main backend admin dashboard

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2011.57.46%20AM.png?raw=true?raw=true)


Display of tools menu options

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2011.58.03%20AM.png?raw=true?raw=true)


Display of the inversion of control schema architecture entity data along with the grid tools related to exporting and importing entities along with dynamic reporting

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2011.58.55%20AM.png?raw=true?raw=true)


View of the backend security user and it’s detail page

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.00.19%20PM.png?raw=true?raw=true)


Display of a more complex detail page and creating a new entity that is an export template for generating reports

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.02.18%20PM.png?raw=true?raw=true)


Show of the combo box, supports autosearch/autocomplete, paging, drill into to see the detail window of the associated entity (arrow in box), the magnifying glass brings up an advanced search window to find and select the entity you want to populate in this combo box

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.02.25%20PM.png?raw=true?raw=true)


File type detail drill in

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.02.48%20PM.png?raw=true?raw=true)


Advanced search showing a search highlighting example. You find what you want, then click the arrow towards the door and it auto populates the previous windows combo box

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.03.20%20PM.png?raw=true?raw=true)


Generic and flexible expression management section

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.05.01%20PM.png?raw=true?raw=true)


An example of a freemarker or spEL expression along with it’s details. This example shows an expression that has a child expression resolution path. The child and parent expressions are used for complex chaining. System beans and other entities in the system may evaluate these expressions against various entities in the system

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.06.26%20PM.png?raw=true?raw=true)


A view of an expression assigned to a specific database entity within the system. This is where the power of the database inversion of control data shines through. This allows assignment of expressions to specific entities and the type of operation that gets performed under specific conditions.

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.07.21%20PM.png?raw=true?raw=true)


Another view of the advanced search for combo boxes

![Alt text](https://github.com/ampopp04/system/blob/master/runner/src/main/resources/screenshots/Screen%20Shot%202018-04-11%20at%2012.08.02%20PM.png?raw=true?raw=true)
