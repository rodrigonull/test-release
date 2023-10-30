pipeline {
    agent any
    
    options {
        timeout(time: 2, unit: 'MINUTES')
    }

    stages {
        stage('test') {
            steps {
                sh "env && echo TEST"
            }
        }

        stage('Parse tag') {
            steps {
                script {
                    env.TAG = sh(returnStdout: true, script: "#!/bin/bash -el \n node -p \"'\${GIT_BRANCH}'.match(/(.+)-prerelease/)[1]\"")
                    env.COMMIT_SHA = sh(returnStdout: true, script: "git rev-parse HEAD")
                    sh "echo ${env.TAG}"
                    sh "echo ${env.COMMIT_SHA}"
                }
            }
        }
    }



    post {
        always {
            // Clean workspace
            cleanWs(deleteDirs: true, disableDeferredWipeout: true)
        }
    }
}

