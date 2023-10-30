pipeline {
    agent any
    
    options {
        timeout(time: 2, unit: 'MINUTES')
    }

    stages {
        stage('test') {
            steps {
                sh "echo TEST"
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

