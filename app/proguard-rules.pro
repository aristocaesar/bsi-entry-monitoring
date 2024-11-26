-keep class com.bsi.entrymonitoring.MainActivity { *; }
-keep class com.bsi.entrymonitoring.EntryActivity { *; }
-keep class com.bsi.entrymonitoring.LoginActivity { *; }
-keep class com.bsi.entrymonitoring.SettingActivity { *; }
-keep class com.bsi.entrymonitoring.utils.MqttClientManager { *; }
-keep class com.bsi.entrymonitoring.model.Employee { *; }
-keep class com.bsi.entrymonitoring.model.User { *; }
-keep class com.bsi.entrymonitoring.api.ApiService { *; }
-keep class com.bsi.entrymonitoring.api.RetrofitClient { *; }

# Keep Retrofit interfaces
-keep interface retrofit2.* { *; }
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Keep classes and methods for Eclipse Paho MQTT
-keep class org.eclipse.paho.** { *; }
-dontwarn org.eclipse.paho.**
