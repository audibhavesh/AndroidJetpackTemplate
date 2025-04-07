# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.theoctacoder.cats_facts.ui.facts.models.** { *; }


-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.**
-dontwarn okhttp3.internal.platform.*
-dontwarn io.socket.client.IO$Options
-dontwarn io.socket.client.IO
-dontwarn io.socket.client.Socket
-dontwarn io.socket.client.SocketOptionBuilder
-dontwarn io.socket.emitter.Emitter$Listener
-dontwarn io.socket.emitter.Emitter
-dontwarn org.bouncycastle.asn1.pkcs.RSAPrivateKey
-dontwarn org.bouncycastle.asn1.pkcs.RSAPublicKey
-dontwarn org.bouncycastle.crypto.BlockCipher
-dontwarn org.bouncycastle.crypto.CipherParameters
-dontwarn org.bouncycastle.crypto.InvalidCipherTextException
-dontwarn org.bouncycastle.crypto.engines.AESEngine
-dontwarn org.bouncycastle.crypto.modes.GCMBlockCipher
-dontwarn org.bouncycastle.crypto.params.AEADParameters
-dontwarn org.bouncycastle.crypto.params.KeyParameter
-dontwarn org.bouncycastle.jce.provider.BouncyCastleProvider
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.simpleframework.xml.Attribute
-dontwarn org.simpleframework.xml.Element
-dontwarn org.simpleframework.xml.Root
-dontwarn org.simpleframework.xml.Serializer
-dontwarn org.simpleframework.xml.core.Persister
-dontwarn proguard.annotation.Keep
-dontwarn proguard.annotation.KeepClassMembers


#-keep class com.fermion.android.base.config.** { *; }

-keep enum com.fermion.android.base.**{
*;
}

-keep enum com.fermion.android.base.models.**{
*;
}


-keepclassmembers class * {
public <init>(org.json.JSONObject);
}

-keep class org.json.** { *; }

-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type


-keep,allowobfuscation,allowshrinking class retrofit2.Response

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation


-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }


