#PULL Base image	
From tomcat:8-jre8-slim

# Maintainer
MAINTAINER "Salil <salil.learner@gmail.com">

# Copy to images tomcat path
ADD *.war /usr/local/tomcat/webapps/

