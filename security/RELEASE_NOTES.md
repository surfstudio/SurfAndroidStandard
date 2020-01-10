[TOC]
# Security-sample-template Release Notes
## 0.5.0-alpha
##### Security
* SBB-2697 Fixed method return value ```CertificatePinnerCreator.extractPeerCertificate```
* ANDDEP-687 Changed "com.squareup.okhttp3:logging-interceptor" dependency from "api" to "implementation" type
* Added new security utils:
    * [ReleaseAppChecker](lib-security/src/main/java/ru/surfstudio/android/security/app/ReleaseAppChecker.kt) - a class with different checks for release application.
    * [ReleaseSignatureChecker](lib-security/src/main/java/ru/surfstudio/android/security/app/ReleaseSignatureChecker.kt) - a class for checking release signature.
    * [SecurityUtils](lib-security/src/main/java/ru/surfstudio/android/security/crypto/security/SecurityUtils.kt) - added different utils for encoding and decoding.
## 0.4.0
##### Security
* ANDDEP-82 Security Module
* [AppDebuggableChecker](lib-security/src/main/java/ru/surfstudio/android/security/app/AppDebuggableChecker.kt) - a class that checks the debuggable flags of the application when it starts.
* [RootChecker](lib-security/src/main/java/ru/surfstudio/android/security/root/RootChecker.kt) - checks for root-rights on the device.
* [KeyEncryptor](lib-security/src/main/java/ru/surfstudio/android/security/crypto/KeyEncryptor.kt) - an abstract class for implementing secure [Encryptor'a](filestorage/src/main/java/ru/surfstudio/android/filestorage/encryptor/Encryptor.kt).
* [CertificatePinnerCreator](lib-security/src/main/java/ru/surfstudio/android/security/ssl/CertificatePinnerCreator.kt) - a class that creates a CertificatePinner for OkHttpClient to implement ssl-pinning.
* [SessionManager](lib-security/src/main/java/ru/surfstudio/android/security/session/SessionManager.kt) - Manager for tracking the Activity session.
* [SecurityUiExtensions](lib-security/src/main/java/ru/surfstudio/android/security/ui/SecurityUiExtensions.kt) - - Utilities for implementing a secure UI.

* Painted Security tips that must be considered in the application.