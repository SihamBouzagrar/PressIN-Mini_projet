pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Kill old app') {
            steps {
                sh '''
                    fuser -k 8083/tcp || true
                '''
            }
        }

        stage('Run App') {
            steps {
                sh '''
                    nohup java -jar target/*.jar --server.port=8083 > app.log 2>&1 &
                    sleep 10
                '''
            }
        }
    }

    post {
        success {
            echo 'CI/CD Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}