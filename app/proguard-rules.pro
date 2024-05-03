-keepattributes SourceFile, LineNumberTable

# Rules needed for kotlinx.serialization
-if @kotlinx.serialization.Serializable class **
-keep class <1> {
    *;
}

-dontwarn org.slf4j.**