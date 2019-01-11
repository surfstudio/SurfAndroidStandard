package ru.surfstudio.standard.util

import org.junit.Test
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.robolectric.RobolectricTestRunner
import java.lang.RuntimeException

class ApiTestRunner(testClass: Class<*>?) : RobolectricTestRunner(testClass) {

    companion object {
        private const val CHECK_API_TESTS_CONSOLE_PARAMETER = "check_api"  // -Dcheck_api
        private const val WAIT_API_TESTS_CONSOLE_PARAMETER = "wait_api"    // -Dwait_api
    }

    private var runCheckApiTests: Boolean = false
    private var runWaitApiTest: Boolean = false


    override fun methodBlock(method: FrameworkMethod): Statement {
        val defaultInvoker = super.methodBlock(method)
        return object : Statement() {
            override fun evaluate() {
                if (isMethodWithAnnotation(method, WaitApiTest::class.java)) {
                    var failed = false
                    try {
                        defaultInvoker.evaluate()
                    } catch (e: Throwable) {
                        System.err.println("Error occurred, when test with annotation @WaitApiTest is executing. It's normal.")
                        e.printStackTrace()
                        failed = true
                    }
                    if (!failed) {
                        throw WaitApiTestPassExceprtion()
                    }
                } else {
                    defaultInvoker.evaluate()
                }
            }
        }
    }

    override fun computeTestMethods(): MutableList<FrameworkMethod> {
        extractInputParameters()
        checkStandardTestMethods()
        val methods = mutableListOf<FrameworkMethod>()
        if (runCheckApiTests) {
            methods.addAll(testClass.getAnnotatedMethods(CheckApiTest::class.java))
        }
        if (runWaitApiTest) {
            methods.addAll(testClass.getAnnotatedMethods(WaitApiTest::class.java))
        }
        checkMethodsContainsStandardTestAnnotation(methods)
        return methods
    }

    private fun checkMethodsContainsStandardTestAnnotation(methods: MutableList<FrameworkMethod>) {
        val badMethods = methods.asSequence().filter { !isMethodWithAnnotation(it, Test::class.java) }.toList()
        if (badMethods.isNotEmpty()) {
            val badMethodsStr = badMethods.asSequence().map { it.name }.reduce { left, right -> "$left, $right" }
            throw RuntimeException("Api test methods must have @test annotation. Wrong methods: $badMethodsStr")
        }

    }

    private fun extractInputParameters() {
        runCheckApiTests = System.getProperty(CHECK_API_TESTS_CONSOLE_PARAMETER) != null
        runWaitApiTest = System.getProperty(WAIT_API_TESTS_CONSOLE_PARAMETER) != null
        if (!runCheckApiTests && !runWaitApiTest) {
            //run all when not configured via parameter
            runCheckApiTests = true
            runWaitApiTest = true
        }
    }

    private fun checkStandardTestMethods() {
        val methods = testClass.getAnnotatedMethods(Test::class.java)
        methods.forEach {
            if (!isMethodWithAnnotation(it, WaitApiTest::class.java) &&
                    !isMethodWithAnnotation(it, CheckApiTest::class.java)) {
                throw RuntimeException("Api Test class cannot contains test method without " +
                        "@WaitApiTest and @CheckApiTest annotations, wrong method: " + it.name)
            }
        }
    }

    private fun isMethodWithAnnotation(method: FrameworkMethod, annotationClass: Class<out Annotation>) =
            method.method.declaredAnnotations.map { it.annotationClass.java }.toList().contains(annotationClass)


}