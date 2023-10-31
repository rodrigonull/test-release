import groovy.json.JsonSlurper

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
                    print(response.html_url)
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


def createGHRelease(String repository, String name, String tag, String commit, Boolean draft, Boolean preRelease, String credentialsId) {
    withCredentials([string(credentialsId: credentialsId, variable: 'GITHUB_TOKEN')]) {
        response = sh returnStdout: true, script: """
        curl -L \
        -X POST \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        -H "X-GitHub-Api-Version: 2022-11-28" \
        https://api.github.com/repos/rodrigonull/test-release/releases \
        -d '{"tag_name": "${tag}", "target_commitish": "0c9760b8cb74c98f24cf4f0dcb64c44c7f184ccb", "name": "0.1.0 (alpha)", "draft": true, "prerelease": true}'
        """.trim()
        print(response)
        respObj = new JsonSlurper().parseText(response as String)
        print(respObj)
        return respObj
    }
}
