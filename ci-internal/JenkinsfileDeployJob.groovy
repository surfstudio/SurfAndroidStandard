// =======================================  CONSTANTS AND VARS ==========================================
//execute results
SUCCESS = 'SUCCESS'
UNSTABLE = 'UNSTABLE'
FAILURE = 'FAILURE'

//stage strategy types
SKIP_STAGE = "SKIP_STAGE"
FAIL_WHEN_STAGE_ERROR = "FAIL_WHEN_STAGE_ERROR"
UNSTABLE_WHEN_STAGE_ERROR = "UNSTABLE_WHEN_STAGE_ERROR"
SUCCESS_WHEN_STAGE_ERROR = "SUCCESS_WHEN_STAGE_ERROR"

branchName = ""

//default stage strategies
checkoutStageStrategy = FAIL_WHEN_STAGE_ERROR
buildStageStrategy = FAIL_WHEN_STAGE_ERROR
unitTestStageStrategy = FAIL_WHEN_STAGE_ERROR
instrumentationTestStageStrategy = FAIL_WHEN_STAGE_ERROR
staticCodeAnalysisStageStrategy = SKIP_STAGE//FAIL_WHEN_STAGE_ERROR
deployStageStrategy = FAIL_WHEN_STAGE_ERROR

//URLS
JARVIS_URL = "http://jarvis.surfstudio.ru/api/v1/"

jobResult = SUCCESS
stagesResult = [:] //stageName : execute result

// =========================== MAIN ===============================================

node("android.node") {
    try {
        stage('Init') {
            initStageBody()
        }
        stageWithStrategy('Checkout', checkoutStageStrategy) {
            checkoutStageBody()
        }
        stageWithStrategy('Build', buildStageStrategy) {
            buildStageBody()
        }
        stageWithStrategy('Unit Test', unitTestStageStrategy) {
            unitTestStageBody()
        }
        stageWithStrategy('Small Instrumentation Test', instrumentationTestStageStrategy) {
            instrumentationTestStageBody()
        }
        stageWithStrategy('Static Code Analysis', staticCodeAnalysisStageStrategy) {
            staticCodeAnalysisStageBody()
        }
        stageWithStrategy('Deploy', deployStageStrategy) {
            deployStageBody()
        }
    } finally {
        finalizeBody()
    }
}

// =================================== STAGE BODIES =======================================
void initStageBody() {
    echo '\n\nInit Started'


    printDefaultVar('checkoutStageStrategy', checkoutStageStrategy)
    printDefaultVar('buildStageStrategy', buildStageStrategy)
    printDefaultVar('unitTestStageStrategy', unitTestStageStrategy)
    printDefaultVar('smallInstrumentationTestStageStrategy', instrumentationTestStageStrategy)
    printDefaultVar('staticCodeAnalysisStageStrategy', staticCodeAnalysisStageStrategy)
    printDefaultVar('deployStageStrategy', betaUploadStageStrategy)



    //Выбираем значения веток и автора из параметров, Установка их в параметры происходит
    // если триггером был webhook или если стартанули Job вручную
    //Используется имя branchName_0 из за особенностей jsonPath в GenericWebhook plugin
    applyParameterIfNotEmpty('branchName', params.branchName_0, {
        value -> branchName = value
    })

    if(branchName.contains("project-snapshot")){
        echo "Apply lightweight strategies for project-snapshot branch"
        unitTestStageStrategy = SKIP_STAGE
        instrumentationTestStageStrategy = SKIP_STAGE
        staticCodeAnalysisStageStrategy = SKIP_STAGE
    }
}

void checkoutStageBody() {
    echo '\n\nCheckout Started'
    checkout([
            $class                           : 'GitSCM',
            branches                         : [[name: branchName]],
            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
            userRemoteConfigs                : scm.userRemoteConfigs,
    ])
    echo 'Checkout Success'
}

void buildStageBody() {
    echo '\n\nBuild Started'
    sh "./gradlew clean assemble"
    step([$class: 'ArtifactArchiver', artifacts: '**/*.apk'])
}

void unitTestStageBody() {
    echo '\n\nUnit Test Started'
    try {
        sh "./gradlew testQaUnitTest"
    } finally {
        junit allowEmptyResults: true, testResults: '**/test-results/testQaUnitTest/*.xml'
        publishHTML(target: [
                allowMissing         : true,
                alwaysLinkToLastBuild: false,
                keepAll              : true,
                reportDir            : 'app/build/reports/tests/testQaUnitTest/',
                reportFiles          : 'index.html',
                reportName           : "Unit Tests"
        ])
    }
}

void instrumentationTestStageBody() {
    // про фильтрацию тестов JUnit https://docs.gradle.org/current/userguide/userguide_single.html#test_filtering
    // еще про градле таски на тестирование https://github.com/googlesamples/android-testing-templates/tree/master/AndroidTestingBlueprint
    echo '\n\nSmall Instrumentation Test Started'
    def ADB = "$ANDROID_HOME/platform-tools/adb"
    def EMULATOR = "$ANDROID_HOME/tools/emulator"
    lock('avd') { //блокируем эмулятор
        sh "$ADB devices"
        sh "$EMULATOR -list-avds"
        sh "$EMULATOR -avd avd-main -no-window &"
        //todo решить проблему с тем что полностью не поддерживается виртуализация, см логи при старте
        //todo обновить os version эмулятора

        timeout(time: 120, unit: 'SECONDS') {
            sh "$ADB wait-for-device"
        }
        //нажимаем кнопку домой
        sh "$ADB shell input keyevent 3 &"
        try {
            //run only tests with annotation @SmallTest
            sh "./gradlew uninstallAll connectedAndroidTest"
        } finally {
            junit allowEmptyResults: true, testResults: '**/outputs/androidTest-results/connected/*.xml'
            publishHTML(target: [allowMissing         : true,
                                 alwaysLinkToLastBuild: false,
                                 keepAll              : true,
                                 reportDir            :
                                         'app/build/reports/androidTests/connected/',
                                 reportFiles          : 'index.html',
                                 reportName           : "Instrumental Tests"
            ])
        }
    }
}

void staticCodeAnalysisStageBody() {
    echo '\n\nStatic Code Analysis started'
    /*withSonarQubeEnv('SonarQube') {
        sh "./gradlew sonarqube --info"
    }
    timeout(time: 600, unit: 'SECONDS') {
        def qg = waitForQualityGate()
        if (qg.status != 'OK') {
            error "Pipeline aborted due to quality gate failure: ${qg.status}"
        }
    }*/
}

void deployStageBody() {
    echo '\n\nDeploy Started'
    sh "./gradlew clean uploadArchives"
}

void finalizeBody() {
    currentBuild.result = jobResult
    def jenkinsLink = "<a href=\"${JENKINS_URL}blue/organizations/jenkins/${env.JOB_NAME}/detail/${env.JOB_NAME}/${env.BUILD_NUMBER}/pipeline\">jenkins</a>"
    def message = ""
    if (jobResult != SUCCESS) {
        unsuccessReasons = ''
        for (element in stagesResult) {
            if (element.value != SUCCESS) {
                if (!unsuccessReasons.isEmpty()) {
                    unsuccessReasons += ", "
                }
                unsuccessReasons += "${element.key} -> ${element.value}"
            }
        }
        message = "Deploy ветки ${branchName} не выполнен из-за этапов: ${unsuccessReasons}. ${jenkinsLink}"
    } else {
        message = "Deploy ветки ${branchName} успешно выполнен. ${jenkinsLink}"
    }
    sendMessageToGroup(
            message,
            scm.userRemoteConfigs[0].url,
            "bitbucket",
            jobResult == SUCCESS)

}

void sendMessageToGroup(String message, String projectId, String idType, boolean success) {
    echo "Sending message to project: ${message}"
    def body = [
            id_or_name    : projectId,
            message       : message,
            message_format: "html",
            as_task       : true,
            id_type       : idType,
            notify        : true,
            color         : success ? "green" : "red",
            sender        : "Jarvis"
    ]
    jsonBody = groovy.json.JsonOutput.toJson(body)
    httpRequest consoleLogResponseBody: true,
            contentType: 'APPLICATION_JSON',
            httpMode: 'POST',
            requestBody: jsonBody,
            url: "${JARVIS_URL}notification/",
            validResponseCodes: '204'
}

// ======================================== UTILS =======================================


void stageWithStrategy(String stageName, String strategy, stageBody) {
    //https://issues.jenkins-ci.org/browse/JENKINS-39203 подождем пока сделают разные статусы на разные Stage
    stage(stageName) {
        if (strategy == SKIP_STAGE) {
            //https://issues.jenkins-ci.org/browse/JENKINS-47286 change approach when fixed
            return
        } else {
            currentBuild.result = SUCCESS
            try {
                bitbucketStatusNotify(
                        buildState: 'INPROGRESS',
                        buildKey: stageName,
                        buildName: stageName
                )
                stageBody()
                stagesResult.put(stageName, SUCCESS)
            } catch (e) {

                if (strategy == FAIL_WHEN_STAGE_ERROR) {
                    stagesResult.put(stageName, FAILURE)
                    jobResult = FAILURE
                    throw e
                } else if (strategy == UNSTABLE_WHEN_STAGE_ERROR) {
                    stagesResult.put(stageName, UNSTABLE)
                    if (jobResult != FAILURE) {
                        jobResult = UNSTABLE
                    }
                } else if (strategy == SUCCESS_WHEN_STAGE_ERROR) {
                    stagesResult.put(stageName, SUCCESS)
                } else {
                    error("Unsupported strategy " + strategy)
                }
            } finally {
                bitbucketStatus = stagesResult.get(stageName) == SUCCESS ?
                        'SUCCESSFUL' :
                        'FAILED'
                bitbucketStatusNotify(
                        buildState: bitbucketStatus,
                        buildKey: stageName,
                        buildName: stageName
                )
            }
        }
    }
}

void applyParameterIfNotEmpty(varName, param, assignmentAction) {
    if (param?.trim()) {
        echo "value of {$varName} sets from parameters to {$param}"
        assignmentAction(param)
    }
}

void printDefaultVar(varName, varValue) {
    echo "default value of {$varName} is {$varValue}"
}
