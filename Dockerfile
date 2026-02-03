# Use official Tomcat image with:
# - Tomcat version 9.0
# - JDK 11 installed
# - Slim Linux base image (lighter & faster)
FROM tomcat:9.0-jdk11-slim

# Remove all default Tomcat web applications
# (like docs, examples, manager, host-manager)
# This keeps the container clean and secure
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your built WAR file from the local 'target' folder
# mysqlinto Tomcat's webapps directory
# Rename it to ROOT.war so it runs at:
# http://localhost:8080
# COPY target/OSRTC-BusTrackingSystem.war /usr/local/tomcat/webapps/ROOT.war
COPY target/OSRTC-BusTrackingSystem.war /usr/local/tomcat/webapps/OSRTC-BusTrackingSystem.war

# Inform Docker that the container will listen on port 8080
# (Tomcat's default port)
EXPOSE 8080

# Start Tomcat server in foreground mode
# 'run' keeps the container alive
CMD ["catalina.sh", "run"]

