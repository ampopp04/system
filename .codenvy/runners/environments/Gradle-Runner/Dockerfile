FROM codenvy/jdk8_maven3_tomcat8

RUN mkdir /home/user/gradle
# download Gradle distribution, unzip it
RUN wget -P /home/user/ --quiet https://services.gradle.org/distributions/gradle-2.13-bin.zip
RUN cd /home/user/ && unzip gradle-2.13-bin.zip

# set environment variables
ENV GRADLE_HOME /home/user/gradle-2.13
RUN echo 'export GRADLE_HOME=$GRADLE_HOME' >> /home/user/.bashrc
ENV PATH $GRADLE_HOME/bin:$PATH
RUN echo "export PATH=$PATH" >> /home/user/.bashrc

RUN mkdir /home/user/app
ADD $src$ /home/user/app/

WORKDIR /home/user/app
ADD $app$ /home/user/$app$
RUN unzip -q /home/user/$app$ -d /home/user/app
