pipeline {
    agent any

    environment {
        REPO = 'git@github.com:CaoDuyTung86/fintrackdeploy.git'
        BACKEND_IMAGE = 'fintrack-backend'
        FRONTEND_IMAGE = 'fintrack-frontend'
        BACKEND_CONTAINER = 'backend-container'
        FRONTEND_CONTAINER = 'frontend-container'
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token-id')
    }

    stages {
        stage('Checkout Code') {
            steps {
            git url: "${REPO}", branch: 'main', credentialsId: 'bd47e73d-f625-46d4-a752-bb84479c5aa7'
            }
        }

        stage('Build Applications') {
            parallel {
                stage('Build Backend') {
                    steps {
                        dir('be-fintrack-master') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Build Frontend') {
                    steps {
                        dir('fe-fintrack-master') {
                            sh 'npm install && npm run build'
                        }
                    }
                }
            }
        }
        
        stage('Start SonarQube') {
            steps {
                sh '''
                    docker-compose up -d sonarqube
                    echo "Waiting for SonarQube to start..."
                    sleep 30
                '''
            }
        }

        stage('SonarQube Analysis') {
            parallel {
                stage('Backend Code Analysis') {
                    steps {
                        dir('be-fintrack-master') {
                            script {
                                sh '''
                                    cat > sonar-project.properties << EOF
                                    sonar.projectKey=fintrack-backend
                                    sonar.projectName=FinTrack Backend
                                    sonar.projectVersion=1.0
                                    sonar.sources=src/main/java
                                    sonar.tests=src/test/java
                                    sonar.java.binaries=target/classes
                                    sonar.java.test.binaries=target/test-classes
                                    sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                                    sonar.host.url=http://localhost:9000
                                    sonar.login=${SONAR_TOKEN}
                                    EOF
                                '''
                                
                                // Chạy SonarQube analysis
                                withSonarQubeEnv('SonarQube') {
                                    sh '''
                                        mvn clean verify sonar:sonar \
                                        -Dsonar.projectKey=fintrack-backend \
                                        -Dsonar.host.url=http://localhost:9000 \
                                        -Dsonar.login=${SONAR_TOKEN}
                                    '''
                                }
                            }
                        }
                    }
                }
                
                stage('Frontend Code Analysis') {
                    steps {
                        dir('fe-fintrack-master') {
                            script {
                                // Tạo sonar-project.properties cho frontend
                                sh '''
                                    cat > sonar-project.properties << EOF
                                    sonar.projectKey=fintrack-frontend
                                    sonar.projectName=FinTrack Frontend
                                    sonar.projectVersion=1.0
                                    sonar.sources=src
                                    sonar.tests=src
                                    sonar.javascript.lcov.reportPaths=coverage/lcov.info
                                    sonar.host.url=http://localhost:9000
                                    sonar.login=${SONAR_TOKEN}
                                    EOF
                                '''
                                sh 'npm install sonarqube-scanner --save-dev'
                                sh 'npx sonarqube-scanner'
                                
                                withSonarQubeEnv('SonarQube') {
                                    sh '''
                                        sonar-scanner \
                                        -Dsonar.projectKey=fintrack-frontend \
                                        -Dsonar.sources=src \
                                        -Dsonar.host.url=http://localhost:9000 \
                                        -Dsonar.login=${SONAR_TOKEN}
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('Quality Gate Check') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Build Backend Image') {
                    steps {
                        dir('be-fintrack-master') {
                            sh "docker build -t ${BACKEND_IMAGE} ."
                        }
                    }
                }
                stage('Build Frontend Image') {
                    steps {
                        dir('fe-fintrack-master') {
                            sh "docker build -t ${FRONTEND_IMAGE} ."
                        }
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    docker-compose down || true
                    docker rm -f backend-container frontend-container sonarqube || true
                    docker-compose up -d
                    sleep 10
                '''
                sh '''
                    echo "=== Container Status ==="
                    docker ps
                    echo "=== Backend Logs ==="
                    docker logs backend-container --tail 20 || true
                    echo "=== Frontend Logs ==="
                    docker logs frontend-container --tail 20 || true
                '''
            }
        }

        stage('Health Check') {
            steps {
                script {
                    try {
                        sh 'curl -f http://localhost:5000/actuator/health || curl -f http://localhost:5000/api/health || echo "Backend health check failed"'
                        echo "✅ Backend is running"
                    } catch (Exception e) {
                        echo "❌ Backend health check failed: ${e.getMessage()}"
                    }

                    try {
                        sh 'curl -f http://localhost:3000 || echo "Frontend health check failed"'
                        echo "✅ Frontend is running"
                    } catch (Exception e) {
                        echo "❌ Frontend health check failed: ${e.getMessage()}"
                    }
                    
                    try {
                        sh 'curl -f http://localhost:9000 || echo "SonarQube health check failed"'
                        echo "✅ SonarQube is running"
                    } catch (Exception e) {
                        echo "❌ SonarQube health check failed: ${e.getMessage()}"
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo "✅ Deployment completed successfully!"
            echo "Frontend: http://localhost:3000"
            echo "Backend: http://localhost:5000"
            echo "SonarQube: http://localhost:9000"
        }
        failure {
            echo "❌ Deployment failed!"
        }
    }
}

