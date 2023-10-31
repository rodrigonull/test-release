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
                    response = githubUtils.createRelease(
                        "rodrigonull/test-release",
                        "${env.TAG} @ (alpha)",
                        "${env.TAG}",
                        "${GIT_COMMIT}",
                        false,
                        true,
                        "gh-token"
                    )

                    respJSON = readJSON text: response

                    env.RELEASE_ID = respJSON.id
                    env.RELEASE_HTML_URL = respJSON.html_url
                    env.RELEASE_UPLOAD_ASSET_URL = respJSON.upload_url
                }
            }
        }

        stage('Upload Assets to Release') {
            steps {
                script {
                    assetFile = "${WORKSPACE}/test.zip"

                    sh """
                    output_empty_zip () { echo UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA== | base64 -d; }
                    output_empty_zip > ${assetFile}
                    """

                    response = githubUtils.uploadReleaseAsset(
                        "${env.RELEASE_UPLOAD_ASSET_URL}",
                        "${assetFile}",
                        "Test.zip",
                        "application/zip",
                        "gh-token"
                    )
                }
            }
        }
    }
}
