package ru.surfstudio.android.security.connection.srp

import com.nimbusds.srp6.*
import ru.surfstudio.android.dagger.scope.PerApplication
import java.math.BigInteger
import javax.inject.Inject

private const val SRP6A = "{srp6a}"
private const val SRP_CONFIG_ID = "1"
private const val PASSWORD_HASH_DELIMITER = ","

/**
 * Интерактор для получения зашифрованного пароля, полученного по протоколу SRP
 */
@PerApplication
class SrpChallengeInteractor @Inject constructor() {

    private val cryptoBitSize = 2048
    private val cryptoH = "SHA-256"

    /**
     * Метод для шифрования логина и пароля, введенных пользователем при авторизации.
     */
    fun getCryptoCredentialsBySrp(username: String,
                                  password: String,
                                  srpChallenge: SrpChallenge): SRP6ClientCredentials {
        val client = SRP6ClientSession()
        client.xRoutine = XRoutineWithUserIdentity()
        val config = SRP6CryptoParams.getInstance(cryptoBitSize, cryptoH)
        client.step1(username, password)
        return client.step2(
                config,
                BigIntegerUtils.fromHex(srpChallenge.salt),
                BigIntegerUtils.fromHex(srpChallenge.b))
    }

    /**
     * Метод для шифрования пароля при регистрации на основании логина и пароля,
     * введенных в форме создания учетной записи.
     */
    fun generatePasswordForRegistration(username: String,
                                        password: String,
                                        srp6ConfigRegistration: Srp6ConfigRegistration): String {
        val config = SRP6CryptoParams(
                BigInteger(srp6ConfigRegistration.N),
                BigInteger.valueOf(srp6ConfigRegistration.g.toLong()),
                srp6ConfigRegistration.H)

        val gen = SRP6VerifierGenerator(config)
        gen.xRoutine = XRoutineWithUserIdentity()
        val salt = BigInteger(gen.generateRandomSalt()).abs()
        val verifier = gen.generateVerifier(salt, username, password)

        return StringBuilder()
                .append(SRP6A)
                .append(BigIntegerUtils.toHex(salt))
                .append(PASSWORD_HASH_DELIMITER)
                .append(BigIntegerUtils.toHex(verifier))
                .append(PASSWORD_HASH_DELIMITER)
                .append(SRP_CONFIG_ID)
                .toString()
    }
}