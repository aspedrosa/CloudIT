# How to set up our CI/CD environment
1. Start docker containers using docker-compose

2. Install Extra Plugins
   - SonarQube Scanner
   - Slack Notification Plugin

3. Setup Sonnar + Jenkins
   - On Sonarqube go to My Account -> Secutiry and generate a new token
   - On Jenkins go to Manage Jenkins -> Configure System -> SonarQube servers
   and add a new server. On the name insert the same as in the analysis stage of
   the Jenkinsfile. On the token field insert the token generated on the Sonarqube

4. Setup Slack + Jenking
   - Install the Jenkins app on to your workspace
   - Choose a channel to the app send messages
   - On Jenkings got o Manage Jenkins -> Configure System -> Global Slack Notifier
   Settings and fill the fields Base URL, Integration Token and Channel with the
   information provided on the Jenkins app page on Slack.
   - Still in Configure System go to Global Pipeline Libraries and add a new entry.
   Give it a name and choose the Retrieval method -> Modern SCM -> GitHub. On owner
   insert sorah and the repository choose the repository jenkinsfile-slack.
   - Insert `@Library('github.com/sorah/jenkinsfile-slack@master') _` to the top
   of your Jenkinsfile
   - Add the follwing to the pipeline on your Jenkinsfile
   ```
   post {
    always {
      postNotifySlack currentBuild.result
    }
   }
   ```
