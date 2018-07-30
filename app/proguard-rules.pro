# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\SDK\sdk/tools/proguard/proguard-android.txt
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
-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-keepattributes *Annotation*     # 保持注解
-dontoptimize                    # 优化不优化输入的类文件
-dontwarn
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
  -dontwarn okio.**
#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.widget.SearchView { *; }
-keepattributes *Annotation*

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

#生成日志数据，gradle build时在本项目根目录输出
-dump class_files.txt            #apk包内所有class的内部结构
-printseeds seeds.txt            #未混淆的类和成员
-printusage unused.txt           #打印未被使用的代码
-printmapping mapping.txt        #混淆前后的映射


#加上之后才可以进行调试
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable

-keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
-keep class com.xxx.**{*;}       #不混淆某个包内的所有文件
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class cn.jk.kaoyandanci.model.*{ *; }
 ### greenDAO 3
 -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
 public static java.lang.String TABLENAME;
 }
 -keep class **$Properties

 # If you do not use SQLCipher:
 -dontwarn org.greenrobot.greendao.database.**
 # If you do not use RxJava:
 -dontwarn rx.**
  
### greenDAO 2
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

-dontwarn com.xxx**              #忽略某个包的警告
-keepattributes Signature        #不混淆泛型
-keepnames class * implements java.io.Serializable #不混淆Serializable

-keepclassmembers class **.R$* { #不混淆资源类
  public static <fields>;
}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-dontwarn com.tencent.bugly.** #不混淆bugly
-keep public class com.tencent.bugly.**{*;}
-dontwarn rx.**
-renamesourcefileattribute SourceFile # 保持行号
-keepattributes SourceFile,LineNumberTable