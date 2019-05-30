FROM maven:3.6.1-jdk-8

#https://unix.stackexchange.com/questions/239757/package-firefox-is-not-available/537147
RUN apt-get update && apt-get install iceweasel -y

#TODO verify key with finger print in https://docs.docker.com/v17.12/install/linux/docker-ce/debian/#install-docker-ce-1
RUN apt-get install apt-transport-https ca-certificates curl gnupg2 software-properties-common -y && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get install docker-ce=5:18.09.6~3-0~debian-stretch -y
