package com.sampleapp.blackbox.test;

import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Debug;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.view.View;

import com.blackbox.test.BuildConfig;
import com.sampleapp.blackbox.testrules.Repeat;
import com.sampleapp.blackbox.testrules.RepeatRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleEspressoTest {
    private static final String APP_PACKAGE = "PACKAGE NAME HERE";
    private static final String PAGE_APPLINK = "TEST APP LINK";
    private static final String TAG = "SampleEspressoTest";

    private static Long startTime;
    private static Long endTime;


    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    @BeforeClass
    public static void setup() {
    }

    //Rule to repeat the test multiple times
    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    @Test
    @Repeat(5)
    public void testHomePerf() throws Exception {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        //Launch the application
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        UiObject go = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/onboarding_skip_button"));

        if (go.waitForExists(1000)) {
            go.click();
        }

        logBegin("home_page_load");
        //find search box on home page
        UiObject searchBox = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_to_check"));
        searchBox.waitForExists(5000);
        logEnd("home_page_load");

        logBegin("home_page_banner_load");
        //find banner on home page
        UiObject bannerHome = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_to_check_banner_load"));
        bannerHome.waitForExists(5000);
        logEnd("home_page_banner_load");

        logBegin("home_page_scroll");
        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        //appViews.scrollForward();
        appViews.scrollToEnd(15);
        logEnd("home_page_scroll");


        //close the activity
        mDevice.pressBack();
    }


    @Test
    @Repeat(5)
    public void testUsingAppLink() throws Exception {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        //Launch the specific page using applink
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setPackage(APP_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(PAGE_APPLINK));
        context.startActivity(intent);

        logBegin("test_page_load");
        UiObject shopName = mDevice.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/id_to_check"));
        shopName.waitForExists(2000);
        logEnd("test_page_load");

        //wait for 3 secs
        Thread.sleep(1000);
        mDevice.pressBack();
    }

    private static void logBegin(String scenarioName) {
        if (BuildConfig.TRACE_ENABLED) {
            Debug.startMethodTracingSampling(scenarioName, 50 * 1024 * 1024, 1000);
        }
        Log.i(TAG, "Scenario.begin " + scenarioName);
        startTime = System.nanoTime();
    }

    private static void logEnd(String scenarioName) {
        if (BuildConfig.TRACE_ENABLED) {
            Debug.stopMethodTracing();
        }
        Log.i(TAG, "Scenario.end " + scenarioName);
        endTime = System.nanoTime();
        long output = endTime - startTime;
        Log.i(TAG, "Scenario.ExecutionTime " + scenarioName + ":: " + (output / 1000000f) + " ms");

        Log.i(TAG, "Scenario.Traffic Stats Received " + scenarioName + ":: " + (TrafficStats.getMobileRxBytes() / 1024) + " kb");
        Log.i(TAG, "Scenario.Traffic Stats Transferred " + scenarioName + ":: " + (TrafficStats.getMobileTxBytes() / 1024) + " kb");

        Log.i(TAG, "Scenario.Memory Stats Total " + scenarioName + ":: " + +(Runtime.getRuntime().totalMemory() / 1024) + " kb");
        Log.i(TAG, "Scenario.Memory Stats Free " + scenarioName + ":: " + +(Runtime.getRuntime().freeMemory() / 1024) + " kb");
    }
}
