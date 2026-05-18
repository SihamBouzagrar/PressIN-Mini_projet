pipeline {
    agent any

    tools {
        jdk 'JDK17'      // Doit correspondre EXACTEMENT au nom dans Jenkins Tools
        maven 'Maven3'   // Doit correspondre EXACTEMENT au nom dans Jenkins Tools
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Kill old app') {
            steps {
                // Ne plante pas si aucun process sur ce port
                bat '''
                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8083 2^>nul') do (
                        taskkill /F /PID %%a 2>nul
                    )
                    exit /b 0
                '''
            }
        }

        stage('Run App') {
            steps {
                bat '''
                    start /B cmd /c "java -jar target\\*.jar --server.port=8083 > app.log 2>&1"
                    timeout /t 10 /nobreak
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