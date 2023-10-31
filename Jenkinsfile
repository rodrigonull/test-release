@Library('kie-tools-pipeline-lib')_

pipeline {
    agent {
        docker {
            image 'quay.io/kie-tools/kie-tools-ci-build:ubuntu'
        }
    }
    
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
                    env.TAG = sh(returnStdout: true, script: "#!/bin/bash -el \n node -p \"'\${GIT_BRANCH}'.match(/(.+)-prerelease/)[1]\"").trim()
                    env.COMMIT_SHA = sh(returnStdout: true, script: "git rev-parse HEAD").trim()
                    sh "echo ${env.TAG}"
                    sh "echo ${env.COMMIT_SHA}"
                }
            }
        }

        stage('Create Release') {
            steps {
                script {
                    response = createGHRelease(
                        "rodrigonull/test-release",
                        "${env.TAG} @ (alpha)",
                        "${env.TAG}",
                        "${GIT_COMMIT}",
                        true,
                        true,
                        "gh-token"
                    )
                }
            }
        }
    }
}
