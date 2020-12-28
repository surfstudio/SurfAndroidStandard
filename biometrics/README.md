# Biometrics
Used to implement biometrics scanner

# Using
#### Base classes:
1. [BiometricsService](lib-biometrics/src/main/java/ru/surfstudio/android/biometrics/BiometricsService.kt) - class to start process of scanning biometrics
2. [BiometricsEncryptorFactory](lib-biometrics/src/main/java/ru/surfstudio/android/biometrics/encryptor/BiometricsEncryptorFactory.kt) - factory of KeyEncryptor for encryption/decryption
3. [BiometricsKeyCreator](lib-biometrics/src/main/java/ru/surfstudio/android/biometrics/encryptor/BiometricsKeyCreator.kt) - class to create secret key for biometrics
4. [DefaultBiometricsEncryptor](lib-biometrics/src/main/java/ru/surfstudio/android/biometrics/encryptor/DefaultBiometricsEncryptor.kt) - default encryptor for `BiometricsEncryptorFactory`

[Sample of using biometrics](../security/sample/src/main/java/ru/surfstudio/android/security/sample/app/CustomApp.kt)