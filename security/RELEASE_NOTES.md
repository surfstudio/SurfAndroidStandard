[TOC]
# Security-sample-template Release Notes
## 0.4.0
##### Security
* ANDDEP-82 Security Module
    * [AppDebuggableChecker] (security-sample-template / security / src / main / java / ru / surfstudio / android / security / app / AppDebuggableChecker.kt) - a class that checks the debuggable flags of the application when it starts.
    * [RootChecker] (security-sample-template / security / src / main / java / ru / surfstudio / android / security / root / RootChecker.kt) - checks for root-rights on the device.
    * [KeyEncryptor] (security-sample-template / security / src / main / java / ru / surfstudio / android / security / crypto / KeyEncryptor.kt) - an abstract class for implementing secure [Encryptor'a] (filestorage / src / main /java/ru/surfstudio/android/filestorage/encryptor/Encryptor.kt).
    * [CertificatePinnerCreator] (security-sample-template / security / src / main / java / ru / surfstudio / android / security / ssl / CertificatePinnerCreator.kt) - a class that creates a CertificatePinner for OkHttpClient to implement ssl-pinning.
    * [SessionManager] (security-sample-template / security / src / main / java / ru / surfstudio / android / security / session / SessionManager.kt) - Manager for tracking the Activity session.
    * [SecurityUiExtensions] (security-sample-template / security / src / main / java / ru / surfstudio / android / security / ui / SecurityUiExtensions.kt) - - Utilities for implementing a secure UI.

    * Painted Security tips that must be considered in the application.