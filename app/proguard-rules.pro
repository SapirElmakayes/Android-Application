# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sapirelmakayes/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

#we want rhat everyone will can see his username
-keep keepclassmembers class com.example.myapplication.afterLogin {
    private <username>;
}

#we want that the clients will know if have problem with the enters passwords
-keep keep class package com.example.myapplication.User {
    public boolean isValidPassword(String password);
}