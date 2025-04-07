package com.fermion.android.base.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.TextUtils
import android.util.Base64
import android.util.Patterns
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Bhavesh Auodichya.
 *
 *Utility class with some basic methods.
 *@since 1.0.0
 */
@Suppress("unused")
object Utility {

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    fun getIPAddress(useIPv4: Boolean = true): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (addressInterface in interfaces) {
                val inetAddresses: List<InetAddress> = Collections.list(addressInterface.inetAddresses)
                for (inetAddress in inetAddresses) {
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress ?: ""
                        val isIPv4 = hostAddress.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) {
                                return hostAddress
                            }
                        } else {
                            if (!isIPv4) {
                                val delimiter = hostAddress.indexOf('%') // drop ip6 zone suffix
                                return if (delimiter < 0) hostAddress.uppercase(Locale.getDefault()) else hostAddress.substring(
                                    0, delimiter
                                ).uppercase(
                                    Locale.getDefault()
                                )
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        } // for now eat exceptions
        return ""
    }


    /**
     * created on 22-08-2023 by Bhavesh Auodichya
     * check weak password
     *@since 1.0.0
     *@param password:String
     *@return boolean true if its weak password else false
     */
    fun isWeakPassword(password: String?): Boolean {
        val pattern: Pattern
        //        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        val passwordPattern = "^(?=\\d{6}$)(?:(.)\\1*|0?1?2?3?4?5?6?7?8?9?|9?8?7?6?5?4?3?2?1?0?)$"
        pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password.toString())
        return matcher.matches()
    }

    /**
     * created on 22-08-2023 by Bhavesh Auodichya
     * checks whether email is valid
     *@since 1.0.0
     *@param emailID:String
     *@return boolean returns true if email is valid else false
     */
    fun isValidEmail(emailID: String?): Boolean {
        return if (emailID == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(emailID).matches()
        }
    }

    /**
     * created on 22-08-2023 by Bhavesh Auodichya
     * converts string to camel case
     *@since 1.0.0
     *@param s:String
     *@return string with camelcase
     */
    fun getCamelCase(s: String): String {
        if (TextUtils.isEmpty(s)) {
            return ""
        }
        val parts = s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var camelCaseString = ""
        for (part in parts) {
            camelCaseString = camelCaseString + toProperCase(part) + " "
        }
        return camelCaseString
    }


    /**
     * created on 22-08-2023
     *@since  1.0.0
     *@param s:String
     *@return string with proper case
     */
    fun toProperCase(s: String?): String {
        return if (s != null) {
            if (s.length > 1) {
                s.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1)
                    .lowercase(Locale.getDefault())
            } else {
                s.uppercase(Locale.getDefault())
            }
        } else {
            ""
        }
    }


    /**
     * removes special characters from string
     *
     * @param input:String
     * @return output:String with special character removed
     *
     */
    fun removeSpecialChars(input: String): String {
        if (TextUtils.isEmpty(input)) {
            return ""
        }
        return input.replace("[-+.^:,@*!?$%&#]".toRegex(), " ")
    }


    /**
     * converts base64 to bitmap
     *
     * @param base64String:String
     * @return bitmap:Bitmap
     *
     *@throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class)
    fun base64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray =
            Base64.decode(base64String.substring(base64String.indexOf(",") + 1), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


    /**
     * converts bitmap to base 64
     *
     * @param bitmap:Bitmap
     * @return base 64 string
     */
    fun bitmapToBase64(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    /**
     * Check whether currently mobile has connected to internet
     *
     * @param context
     * @param function pass the code which should be called if internet is connected
     * eg. isOnline(context,{println("Connected")})
     * @since 1.0.0
     * @return status whether internet is connected is not
     *
     * */
    fun isOnline(context: Context, function: () -> Unit = {}): Boolean {
        var isOnline = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isOnline = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    isOnline = true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    isOnline = true
                }
            }
        }
        if (isOnline) {
            function.invoke()
        }
        return isOnline
    }

}