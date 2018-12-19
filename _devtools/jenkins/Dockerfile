FROM jenkins/jenkins:2.149

USER root

RUN curl -fsSL get.docker.com -o get-docker.sh && sh get-docker.sh
RUN usermod -aG docker jenkins

USER jenkins

ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/jenkins.sh"]
