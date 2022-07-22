@file:JvmName("Base64")
package android.util

import java.util.Base64

/**
 * Mocks Android's built in Base64 encoder (android.util.Base64) so that we can test code that
 * uses the Base64 encode/decode methods without needed to manually mock or inject a Base64
 * abstraction for every test.
 *
 * This mock is very basic, it currently only supports the android.util.Base64.DEFAULT flag. To
 * test code that utilizes other encode/decode settings, this mock will need to be expanded.
 */
object Base64 {
    @JvmStatic fun decode(str: String, flags: Int): ByteArray = Base64.getDecoder().decode(str)
    @JvmStatic fun encodeToString(input: ByteArray, flags: Int): String =
        Base64.getEncoder().encodeToString(input)
}