pipeline {
    agent any
tools {
    maven 'Maven'
    jdk 'JDK17'
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

        stage('Run App') {
            steps {
                sh '''
                nohup java -jar target/*.jar --server.port=8083 > app.log 2>&1 &
                sleep 10
                echo "App started on port 8083"
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}