FROM tomcat:8.0-jre8
COPY ./build/libs /usr/local/tomcat/webapps
