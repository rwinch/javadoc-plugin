properties([
		buildDiscarder(logRotator(numToKeepStr: '10')),
		pipelineTriggers([
				cron('@daily')
		]),
])

def GRADLE_ENTERPRISE_CACHE_USER = usernamePassword(credentialsId: 'gradle_enterprise_cache_user',
		passwordVariable: 'GRADLE_ENTERPRISE_CACHE_PASSWORD',
		usernameVariable: 'GRADLE_ENTERPRISE_CACHE_USERNAME')
def GRADLE_ENTERPRISE_SECRET_ACCESS_KEY = string(credentialsId: 'gradle_enterprise_secret_access_key',
		variable: 'GRADLE_ENTERPRISE_ACCESS_KEY')
def ARTIFACTORY_CREDENTIALS = usernamePassword(credentialsId: '02bd1690-b54f-4c9f-819d-a77cb7a9822c', usernameVariable: 'ARTIFACTORY_USERNAME', passwordVariable: 'ARTIFACTORY_PASSWORD')

def jdkEnv(String jdk = 'jdk8') {
	def jdkTool = tool(jdk)
	return "JAVA_HOME=${ jdkTool }"
}

stage('Artifactory Deploy') {
	node('linux') {
		checkout scm
		sh 'git clean -dfx'
		withCredentials([ARTIFACTORY_CREDENTIALS,
		 GRADLE_ENTERPRISE_CACHE_USER,
		 GRADLE_ENTERPRISE_SECRET_ACCESS_KEY]) {
			withEnv([jdkEnv(),
				 "GRADLE_ENTERPRISE_CACHE_USERNAME=${GRADLE_ENTERPRISE_CACHE_USERNAME}",
				 "GRADLE_ENTERPRISE_CACHE_PASSWORD=${GRADLE_ENTERPRISE_CACHE_PASSWORD}",
				 "GRADLE_ENTERPRISE_ACCESS_KEY=${GRADLE_ENTERPRISE_ACCESS_KEY}"]) {
				sh './gradlew check artifactoryPublish --stacktrace -PartifactoryUsername=$ARTIFACTORY_USERNAME -PartifactoryPassword=$ARTIFACTORY_PASSWORD'
			}
		}
	}
}
