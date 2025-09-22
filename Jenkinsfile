pipeline {
    agent any

    environment {
        TOMCAT_HOME = "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1"
        FRONTEND_DIR = "frontend"
        BACKEND_DIR = "backend"
        CONTEXT_PATH = "onlinebanking"
        BACKEND_WAR_NAME = "onlinebankingbackend.war"
    }

    stages {

        // ===== FRONTEND BUILD =====
        stage('Build Frontend') {
            steps {
                dir(FRONTEND_DIR) {
                    echo "Installing npm packages..."
                    bat 'npm install'

                    echo "Setting Vite base path..."
                    // Update vite.config.js to set base to /onlinebanking/
                    bat """
                    powershell -Command "(Get-Content vite.config.js) -replace 'base: \\'/\\'', 'base: \\'/${CONTEXT_PATH}/\\'' | Set-Content vite.config.js"
                    """

                    echo "Building frontend..."
                    bat 'npm run build'
                }
            }
        }

        // ===== FRONTEND DEPLOY =====
        stage('Deploy Frontend to Tomcat') {
            steps {
                echo "Deploying frontend to Tomcat..."
                bat """
                if exist "${TOMCAT_HOME}\\webapps\\${CONTEXT_PATH}" (
                    rmdir /S /Q "${TOMCAT_HOME}\\webapps\\${CONTEXT_PATH}"
                )
                mkdir "${TOMCAT_HOME}\\webapps\\${CONTEXT_PATH}"
                xcopy /E /I /Y ${FRONTEND_DIR}\\dist\\* "${TOMCAT_HOME}\\webapps\\${CONTEXT_PATH}"
                """
            }
        }

        // ===== BACKEND BUILD =====
        stage('Build Backend') {
            steps {
                dir(BACKEND_DIR) {
                    echo "Building backend WAR..."
                    bat 'mvn clean package'
                }
            }
        }

        // ===== BACKEND DEPLOY =====
        stage('Deploy Backend to Tomcat') {
            steps {
                script {
                    // Compute backend folder name without .war
                    def backendFolder = BACKEND_WAR_NAME.replace('.war','')

                    echo "Deploying backend to Tomcat..."
                    bat """
                    if exist "${TOMCAT_HOME}\\webapps\\${BACKEND_WAR_NAME}" (
                        del /Q "${TOMCAT_HOME}\\webapps\\${BACKEND_WAR_NAME}"
                    )
                    if exist "${TOMCAT_HOME}\\webapps\\${backendFolder}" (
                        rmdir /S /Q "${TOMCAT_HOME}\\webapps\\${backendFolder}"
                    )
                    copy "${BACKEND_DIR}\\target\\*.war" "${TOMCAT_HOME}\\webapps\\"
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Frontend and Backend Deployment Successful!'
        }
        failure {
            echo 'Pipeline Failed. Check logs for details.'
        }
    }
}
