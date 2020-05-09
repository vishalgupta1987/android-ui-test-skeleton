/*
 * Copyright 2015, Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sampleapp.blackbox.testrules;

import android.os.Debug;
import android.os.Trace;
import android.util.Log;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule enables {@link Trace Tracing} for each test. The section name
 * used for the Trace API is the name of the test being run.
 * <p>
 * To enable AndroidTracing on a test simply add this rule like so and it will be enabled/disabled
 * when the platform support for Tracing exists (API Level 18 or higher).
 *
 * <pre>
 * @Rule
 * public EnableTestTracing mEnableTestTracing = new EnableTestTracing();
 * </pre>
 */
public class EnableDebugTestTracing extends ExternalResource {

    private String mTestName;
    private static final String TAG = "TokopediaBBT";

    @Override
    public Statement apply(Statement base, Description description) {
        mTestName = description.getMethodName();

        return super.apply(base, description);
    }

    @Override
    public void before() {
        Log.i(TAG, "Trace Before " + mTestName);
        Debug.startMethodTracing(mTestName);
    }

    @Override
    public void after() {
        Debug.stopMethodTracing();
        Log.i(TAG, "Trace After");
    }
}
