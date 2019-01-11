package ru.surfstudio.standard.util

import java.lang.RuntimeException

class WaitApiTestPassExceprtion : RuntimeException(
        "Test with annotation @WaitApiTest is finished without exception. " +
                "If api method is working now, remove this annotation")