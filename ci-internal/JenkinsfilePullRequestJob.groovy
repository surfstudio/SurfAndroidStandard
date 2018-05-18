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

//scm
sourceBranch = ""
destinationBranch = ""
authorUsername = ""

//default stage strategies
preMergeStageStrategy = FAIL_WHEN_STAGE_ERROR
buildStageStrategy = FAIL_WHEN_STAGE_ERROR
unitTestStageStrategy = UNSTABLE_WHEN_STAGE_ERROR
smallInstrumentationTestStageStrategy = SKIP_STAGE//UNSTABLE_WHEN_STAGE_ERROR
staticCodeAnalysisStageStrategy = UNSTABLE_WHEN_STAGE_ERROR

jobResult = SUCCESS
stagesResult = [:] //stageName : execute result

// =========================== MAIN ===============================================

node("android") {
    try {
        stage('Init') {
            initStageBody()
        }
        stageWithStrategy('PreMerge', preMergeStageStrategy) {
            preMergeStageBody()
        }
        stageWithStrategy('Build', buildStageStrategy) {
            buildStageBody()
        }
        stageWithStrategy('Unit Test', unitTestStageStrategy) {
            unitTestStageBody()
        }
        stageWithStrategy('Small Instrumentation Test', smallInstrumentationTestStageStrategy) {
            smallInstrumentationTestStageBody()
        }
        stageWithStrategy('Static Code Analysis', staticCodeAnalysisStageStrategy) {
            staticCodeAnalysisStageBody()
        }
    } finally {
        finalizeBody()
    }
}

// =================================== STAGE BODIES =======================================
void initStageBody() {
    echo '\n\nInit Started'


    printDefaultVar('preMergeStageStrategy', preMergeStageStrategy)
    printDefaultVar('buildStageStrategy', buildStageStrategy)
    printDefaultVar('unitTestStageStrategy', unitTestStageStrategy)
    printDefaultVar('smallInstrumentationTestStageStrategy', smallInstrumentationTestStageStrategy)
    printDefaultVar('staticCodeAnalysisStageStrategy', staticCodeAnalysisStageStrategy)

    //Используем нестандартные стратегии для Stage из параметров, если они установлены
    //Параметры могут быть установлены только если Job стартовали вручную
    applyParameterIfNotEmpty('preMergeStageStrategy', params.preMergeStageStrategy) {
        param -> preMergeStageStrategy = param
    }
    applyParameterIfNotEmpty('buildStageStrategy', params.buildStageStrategy) {
        param -> buildStageStrategy = param
    }
    applyParameterIfNotEmpty('unitTestStageStrategy', params.unitTestStageStrategy) {
        param -> unitTestStageStrategy = param
    }
    applyParameterIfNotEmpty('smallInstrumentationTestStageStrategy', params.smallInstrumentationTestStageStrategy) {
        param -> smallInstrumentationTestStageStrategy = param
    }
    applyParameterIfNotEmpty('staticCodeAnalysisStageStrategy', params.staticCodeAnalysisStageStrategy) {
        param -> staticCodeAnalysisStageStrategy = param
    }

    //Выбираем значения веток и автора из параметров, Установка их в параметры происходит
    // если триггером был webhook или если стартанули Job вручную
    applyParameterIfNotEmpty('sourceBranch', params.sourceBranch, {
        value -> sourceBranch = value
    })
    applyParameterIfNotEmpty('destinationBranch', params.destinationBranch, {
        value -> destinationBranch = value
    })
    applyParameterIfNotEmpty('authorUsername', params.authorUsername, {
        value -> authorUsername = value
    })

    env.BRANCH_NAME = sourceBranch //для отображения в ui, почему то не работает
}

void preMergeStageBody() {
    echo '\n\nPreMerge Started'
    checkout([
            $class                           : 'GitSCM',
            branches                         : [[name: "${destinationBranch}"]],
            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
            userRemoteConfigs                : scm.userRemoteConfigs,
            extensions                       : [
                    [
                            $class : 'PreBuildMerge',
                            options: [
                                    fastForwardMode: 'NO_FF',
                                    mergeRemote    : 'origin',
                                    mergeStrategy  : 'MergeCommand.Strategy',
                                    mergeTarget    : "${sourceBranch}"
                            ]
                    ]
            ]
    ])
    echo 'PreMerge Success'
}

void buildStageBody() {
    echo '\n\nBuild Started'
    sh "./gradlew clean"
    //sh "./gradlew clean assembleQa"
    //step([$class: 'ArtifactArchiver', artifacts: '**/*.apk'])
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

void smallInstrumentationTestStageBody() {
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
            sh "./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small"
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
    /*withSonarQubeEnv('SonarQube') { //todo заменить на lint
        sh "./gradlew sonarqube --info"
    }
    timeout(time: 600, unit: 'SECONDS') {
        def qg = waitForQualityGate()
        if (qg.status != 'OK') {
            error "Pipeline aborted due to quality gate failure: ${qg.status}"
        }
    }*/
}

void finalizeBody() {
    currentBuild.result = jobResult
    sendNotificationIfNeeded()
}

void sendNotificationIfNeeded() {
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
        message = "Build branch ${sourceBranch} ${jobResult} because: ${unsuccessReasons}; <a href=\"http://jenkins.surfstudio.ru/blue/organizations/jenkins/${env.JOB_NAME}/detail/${env.JOB_NAME}/${env.BUILD_NUMBER}/pipeline\">jenkins build</a>"
        echo "Sending message: ${message}"
        def body = [
                id_or_name    : authorUsername,
                message       : message,
                message_format: "html",
                as_task       : true,
                id_type       : "bitbucket"
        ]
        jsonBody = groovy.json.JsonOutput.toJson(body)
        httpRequest consoleLogResponseBody: true,
                contentType: 'APPLICATION_JSON',
                httpMode: 'POST',
                requestBody: jsonBody,
                url: "http://jarvis.surfstudio.ru/api/v1/message/",
                validResponseCodes: '204'
    }
}

// ======================================== UTILS =======================================
void stageWithStrategy(String stageName, String strategy, stageBody) {
    if (strategy == SKIP_STAGE) {
        //https://issues.jenkins-ci.org/browse/JENKINS-47286 change approach when fixed
        return
    } else {
        //https://issues.jenkins-ci.org/browse/JENKINS-39203 подождем пока сделают разные статусы на разные Stage
        stage(stageName) {
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

