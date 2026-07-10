# Glint Production ProGuard Rules
# Optimized for Jetpack Compose, Kotlin Coroutines, Retrofit, Moshi, Room, and CameraX.

# -----------------------------------------------------------------------------------
# General Rules
# -----------------------------------------------------------------------------------

# Preserve line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Preserve Annotations and Signatures for Reflection-based libraries
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# -----------------------------------------------------------------------------------
# Jetpack Compose
# -----------------------------------------------------------------------------------
-keepclassmembers class androidx.compose.ui.platform.ComposeView {
   public *;
}
-keep class androidx.compose.material.icons.** { *; }

# -----------------------------------------------------------------------------------
# Kotlin Coroutines
# -----------------------------------------------------------------------------------
# Keep internal dispatcher names for runtime resolution
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-dontwarn kotlinx.coroutines.**

# -----------------------------------------------------------------------------------
# Retrofit / OkHttp
# -----------------------------------------------------------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature, InnerClasses, EnclosingMethod
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# -----------------------------------------------------------------------------------
# Moshi (JSON Serialization)
# -----------------------------------------------------------------------------------
# Retain generic type information and JsonClass constructors
-keep class com.squareup.moshi.* { *; }
-keep class kotlin.reflect.jvm.internal.** { *; }

# Keep generated adapters
-keep class *JsonAdapter { *; }

# -----------------------------------------------------------------------------------
# CameraX
# -----------------------------------------------------------------------------------
-keep class androidx.camera.core.** { *; }
-keep class androidx.camera.camera2.** { *; }
-keep class androidx.camera.lifecycle.** { *; }
-keep class androidx.camera.view.** { *; }

# -----------------------------------------------------------------------------------
# Google Play Services & Billing
# -----------------------------------------------------------------------------------
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
-keep class com.android.billingclient.** { *; }
-dontwarn com.android.billingclient.**

# -----------------------------------------------------------------------------------
# Glint Data Models (Crucial for Obfuscation)
# -----------------------------------------------------------------------------------
# Prevent Moshi/Room from failing due to renamed fields
-keepclassmembers class com.jn.glint.model.** { *; }
