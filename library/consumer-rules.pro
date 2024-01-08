
-keep class * implements androidx.viewbinding.ViewBinding {
    * inflate(**);
}

-keepclassmembernames class * extends me.chan.mtrv.Renderer {
    <init>(java.lang.Object);
}