FROM tomcat:8.5-slim

ENV UAA_CONFIG_PATH /usr/local/uaa/

ADD https://repo1.maven.org/maven2/org/cloudfoundry/identity/cloudfoundry-identity-uaa/4.30.0/cloudfoundry-identity-uaa-4.30.0.war /usr/local/tomcat/webapps/uaa.war
COPY ./uaa.yml /usr/local/uaa/
