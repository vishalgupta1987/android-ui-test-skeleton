# README #

### What is this repository for? ###

This is a stand alone test app skeleton, which can be used to write Espresso/UIAutomator tests targeting any application.

### Instructions ###
To properly use this skeleton, you need to know the package name of the targeted app for which you wanted to run the test. Replace "PACKAGE NAME HERE" with the targeted app package name at following places.

1. app/build.gradle
2. app/src/androidTest/java/com/sampleapp/blackbox/test/SampleEspressoTest.java

Build command: ./gradlew assembleAndroidTest

Need to resign both this test app and the target APK with the same key

Then install both on device/emulator and run instrumentation as follows:

Command to run the test: adb shell am instrument -w -r -e class com.sampleapp.blackbox.test.SampleEspressoTest com.blackbox.testt/android.support.test.runner.AndroidJUnitRunner
