@Library('surf-lib@version-4.0.0-SNAPSHOT') // https://gitlab.com/surfstudio/infrastructure/tools/jenkins-pipeline-lib
import ru.surfstudio.ci.CommonUtil
import ru.surfstudio.ci.pipeline.base.LogRotatorUtil
import ru.surfstudio.ci.pipeline.pr.PrPipeline
import ru.surfstudio.ci.pipeline.pr.PrPipelineAndroid
import ru.surfstudio.ci.stage.StageStrategy

class ParametersSupport {

    private static int ARTIFACTS_DAYS_TO_KEEP_MAX_VALUE = 5
    private static int ARTIFACTS_NUM_TO_KEEP_MAX_VALUE = 20
    private static int DAYS_TO_KEEP_MAX_VALUE = 30
    private static int NUM_TO_KEEP_MAX_VALUE = 100

    //parameters
    public static final String SOURCE_BRANCH_PARAMETER = 'sourceBranch'
    public static final String DESTINATION_BRANCH_PARAMETER = 'destinationBranch'
    public static final String AUTHOR_USERNAME_PARAMETER = 'authorUsername'
    public static final String TARGET_BRANCH_CHANGED_PARAMETER = 'targetBranchChanged'
    public static final String ghprbPullId = 'ghprbPullId'
    public static final String CHANGE_URL = 'CHANGE_URL'

    static List<Object> properties(PrPipeline ctx) {
        def script = ctx.script
        return [
                buildDiscarder(ctx, script),
                parameters(script),
                triggers(script, ctx.repoUrl)
        ]
    }

    def static buildDiscarder(PrPipeline ctx, script) {
        return script.buildDiscarder(
                script.logRotator(
                        artifactDaysToKeepStr: LogRotatorUtil.getActualParameterValue(
                                script,
                                LogRotatorUtil.ARTIFACTS_DAYS_TO_KEEP_NAME,
                                ctx.artifactDaysToKeep,
                                ARTIFACTS_DAYS_TO_KEEP_MAX_VALUE
                        ),
                        artifactNumToKeepStr: LogRotatorUtil.getActualParameterValue(
                                script,
                                LogRotatorUtil.ARTIFACTS_NUM_TO_KEEP_NAME,
                                ctx.artifactNumToKeep,
                                ARTIFACTS_NUM_TO_KEEP_MAX_VALUE
                        ),
                        daysToKeepStr: LogRotatorUtil.getActualParameterValue(
                                script,
                                LogRotatorUtil.DAYS_TO_KEEP_NAME,
                                ctx.daysToKeep,
                                DAYS_TO_KEEP_MAX_VALUE
                        ),
                        numToKeepStr: LogRotatorUtil.getActualParameterValue(
                                script,
                                LogRotatorUtil.NUM_TO_KEEP_NAME,
                                ctx.numToKeep,
                                NUM_TO_KEEP_MAX_VALUE
                        )
                )
        )
    }

    def static parameters(script) {
        return script.parameters([
                script.string(
                        name: SOURCE_BRANCH_PARAMETER,
                        description: 'Ветка с pr, обязательный параметр'),
                script.string(
                        name: DESTINATION_BRANCH_PARAMETER,
                        description: 'Ветка, в которую будет мержиться пр, обязательный параметр'),
                script.string(
                        name: AUTHOR_USERNAME_PARAMETER,
                        description: 'username в github создателя пр, нужно для отправки собщений, обязательный параметр'),
                script.string(
                        name: ghprbPullId,
                        description: 'номер PR-а в GitHub. Можно получить из URL'),
                script.string(
                        name: CHANGE_URL,
                        description: 'URL PR-а в GitHub')
        ])
    }

    def static triggers(script, String repoUrl) {
        return script.pipelineTriggers([
                script.GenericTrigger(
                        genericVariables: [
                                [
                                        key  : SOURCE_BRANCH_PARAMETER,
                                        value: '$.pull_request.head.ref'
                                ],
                                [
                                        key  : DESTINATION_BRANCH_PARAMETER,
                                        value: '$.pull_request.base.ref'
                                ],
                                [
                                        key  : AUTHOR_USERNAME_PARAMETER,
                                        value: '$.sender.login'
                                ],
                                [
                                        key  : 'repoUrl',
                                        value: '$.repository.html_url'
                                ],
                                [
                                        key  : TARGET_BRANCH_CHANGED_PARAMETER,
                                        value: '$.target_branch.changed' //параметер добавляет jarvis
                                ],
                                [
                                        key  : ghprbPullId, // Параметр для Danger. Это номер PR-а
                                        value: '$.number'
                                ],
                                [
                                        key  : CHANGE_URL, // Параметр для Danger. URL PR-а
                                        value: '$.pull_request.html_url'
                                ]
                        ],
                        printContributedVariables: true,
                        printPostContent: true,
                        causeString: 'Triggered by GitHub',
                        regexpFilterExpression: "$repoUrl",
                        regexpFilterText: '$repoUrl'
                ),
                script.pollSCM('')
        ])
    }
}

//init
def pipeline = new PrPipelineAndroid(this)
pipeline.init()
pipeline.propertiesProvider = { ParametersSupport.properties(pipeline) }
def buildStage = pipeline.getStage(pipeline.BUILD).body

//customization
pipeline.getStage(pipeline.INSTRUMENTATION_TEST).strategy = StageStrategy.SKIP_STAGE
pipeline.getStage(pipeline.STATIC_CODE_ANALYSIS).strategy = StageStrategy.SKIP_STAGE
pipeline.getStage(pipeline.CODE_STYLE_FORMATTING).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR
pipeline.getStage(pipeline.UPDATE_CURRENT_COMMIT_HASH_AFTER_FORMAT).strategy = StageStrategy.UNSTABLE_WHEN_STAGE_ERROR

pipeline.getStage(pipeline.BUILD).body = {
    try {
        buildStage() // Вызываем дефолтную логику для билда
        this.sh("./gradlew detektCheck") // Запускаем проверку линтером

        // Если билд прошел успешно, то считываем GitHub Token записанный в секрете с ID Surf_GitHub_Bot_Personal_Access_Token в переменную окружения с именем DANGER_GITHUB_API_TOKEN
        this.withCredentials([string(credentialsId: 'Surf_GitHub_Bot_Personal_Access_Token', variable: 'DANGER_GITHUB_API_TOKEN')]) {
            // И запускаем Danger
            CommonUtil.shWithRuby(this, "make danger")
        }
    } catch (e) {
        // Если билд не прошел - была ошибка, то нам все равно надо запустить Danger, чтоб запостить ошибки
        this.sh("./gradlew detektCheck") // Запускаем проверку линтером
        this.withCredentials([string(credentialsId: 'Surf_GitHub_Bot_Personal_Access_Token', variable: 'DANGER_GITHUB_API_TOKEN')]) {
            // И запускаем Danger
            CommonUtil.shWithRuby(this, "make danger")
        }
        // А потом роняем пайплайн, чтоб билд покраснел
        throw e
    }
}

//run
pipeline.run()
