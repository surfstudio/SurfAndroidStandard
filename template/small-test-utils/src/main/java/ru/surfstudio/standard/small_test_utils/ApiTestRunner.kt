package ru.surfstudio.standard.small_test_utils

import org.junit.Test
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.robolectric.RobolectricTestRunner
import java.lang.RuntimeException

class ApiTestRunner(testClass: Class<*>?) : RobolectricTestRunner(testClass) {

    companion object {
        private const val CHECK_API_TESTS_CONSOLE_PARAMETER = "check_api"
        private const val WAIT_API_TESTS_CONSOLE_PARAMETER = "wait_api"
    }

    private var runCheckApiTests: Boolean = false
    private var runWaitApiTests: Boolean = false


    override fun methodBlock(method: FrameworkMethod): Statement {
        val defaultInvoker = super.methodBlock(method)
        return object : Statement() {
            override fun evaluate() {
                if (isMethodWithAnnotation(method, WaitApiTest::class.java)) {
                    var failed = false
                    val annotation = method.annotations.find { it.annotationClass == WaitApiTest::class } as WaitApiTest
                    try {
                        defaultInvoker.evaluate()
                    } catch (e: Throwable) {
                        if (annotation.checkedException == Throwable::class
                                || (annotation.checkedException != Throwable::class && annotation.checkedException == e::class)) {
                            System.err.println("Error occurred when test with annotation @WaitApiTest is executing. It's normal.")
                            e.printStackTrace()
                            failed = true
                        }
                    }
                    if (!failed) {
                        throw WaitApiTestPassException()
                    }
                } else {
                    defaultInvoker.evaluate()
                }
            }
        }
    }

    override fun computeTestMethods(): MutableList<FrameworkMethod> {
        extractInputParameters()
        val methods = mutableListOf<FrameworkMethod>()
        if (runCheckApiTests) {
            methods.addAll(testClass.getAnnotatedMethods(Test::class.java)
                    .asSequence()
                    .filter { !isMethodWithAnnotation(it, WaitApiTest::class.java) }
                    .toList())
        }
        if (runWaitApiTests) {
            val waitApiMethods = testClass.getAnnotatedMethods(WaitApiTest::class.java)
            checkMethodsContainsStandardTestAnnotation(waitApiMethods)
            methods.addAll(waitApiMethods)
        }

        return methods
    }

    private fun checkMethodsContainsStandardTestAnnotation(methods: MutableList<FrameworkMethod>) {
        val badMethods = methods.asSequence().filter { !isMethodWithAnnotation(it, Test::class.java) }.toList()
        if (badMethods.isNotEmpty()) {
            val badMethodsStr = badMethods.asSequence().map { it.name }.reduce { left, right -> "$left, $right" }
            throw RuntimeException("ApiCategory test methods must have @Test annotation. Wrong methods: $badMethodsStr")
        }
    }

    private fun extractInputParameters() {
        runCheckApiTests = System.getProperty(CHECK_API_TESTS_CONSOLE_PARAMETER) != null
        runWaitApiTests = System.getProperty(WAIT_API_TESTS_CONSOLE_PARAMETER) != null
        if (!runCheckApiTests && !runWaitApiTests) {
            //run all when not configured via parameter
            runCheckApiTests = true
            runWaitApiTests = true
        }
    }

    private fun isMethodWithAnnotation(method: FrameworkMethod, annotationClass: Class<out Annotation>) =
            method.method.declaredAnnotations.map { it.annotationClass.java }.toList().contains(annotationClass)
}