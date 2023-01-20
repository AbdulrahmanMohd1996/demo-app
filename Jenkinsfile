#!/usr/bin/env groovy
//@ Library('jenkins-shared-library')

import groovy.json.JsonSlurper

library identifier: 'jenkins-shared-lib@main', retriever: modernSCM(
    [$class: 'GitSCMSource',
     remote: 'https://gitlab.com/abdulrahman.mohd1996/jenkins-shared-library.git',
     credentialsId: '62495e77-beb8-495a-83ab-accf341b3c25'
    ]
     
)
     
//def gv

pipeline 
{
  
    agent any

    stages
    {       
        stage('init')
        {           
            steps
            {

                script
                {  
                    //gv = load "script.groovy"
                    sh "npm --prefix ./api/. version patch"
                    def packageN = readFile("./api/package.json")
                    def jsonPackage = new JsonSlurper().parseText(packageN)
                    env.PKG_NAME = jsonPackage.name
                    env.VER = jsonPackage.version
                    env.IMAGE_NAME="${PKG_NAME}"


                }
            }
        }
    


        stage('build') 
        {
            steps
            {
                script
                {
                    sh "docker build -t abdolee/${IMAGE_NAME}:${VER}-${BUILD_NUMBER} ." 
                }
            }

        }
        
        stage('push to docekr HUB')
         {
            steps
            {
                script 
                {
                    sh "docker push abdolee/${PKG_NAME}:${VER}-${BUILD_NUMBER}"
                }
            }
        }
        stage('deploy') 
        {
            steps 
            {
                script 
                {
                        def dockerCommand= "docker run -d -p 3080:3080 abdolee/${PKG_NAME}:${VER}-${BUILD_NUMBER}"

                        sshagent(['EC2-SERVER-KEY']) 
                        {
                            sh "ssh -o StrictHostKeyChecking=no -i /tmp/docker-server.pem ec2-user@54.194.193.122 '${dockerCommand}' "
                        }
                }
            }
        }

        stage('commit version update')
        {
            steps
            {
                script
                {
                    withCredentials([usernamePassword(credentialsId: 'GITHUB-CRED', passwordVariable:'PASSWORD', usernameVariable:'USERNAME')])
                    {
                        sh "git config --global user.email 'jenkins@example.com' "
                        sh "git config --global user.name 'jenkins' "

                        sh "git status"
                        sh "git branch"
                        sh "git config --list"

                        sh "git remote set-url origin 'https://${USERNAME}:${PASSWORD}@github.com/AbdulrahmanMohd1996/demo-app.git'"
                        sh "git add ."
                        sh "git commit -m 'from jenkins version json.xml'"
                        sh "git add"
                        sh "git push origin HEAD:master"

                    }
                }
                
            }
        }

    }

}
