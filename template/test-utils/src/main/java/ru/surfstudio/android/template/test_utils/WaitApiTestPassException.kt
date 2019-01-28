package ru.surfstudio.android.template.test_utils

import java.lang.RuntimeException

class WaitApiTestPassException : RuntimeException(
        "Test with annotation @WaitApiTest is finished without exception. " +
                "If api method is working now, remove this annotation"
)