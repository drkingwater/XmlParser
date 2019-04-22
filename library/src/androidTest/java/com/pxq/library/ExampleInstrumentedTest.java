package com.pxq.library;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.pxq.library.bean.XmlTest;
import com.pxq.library.xml.XmlParser;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() {
        // Context of the app under test.

        XmlTest test = new XmlTest();
        test.name = "namevalue";
        test.subName = "attrvalue";
        test.name1 = "namevalue1";
        test.subName1 = "attrvalue1";
        test.name2 = "namevalue2";
        test.subName2 = "attrvalue2";
        String xml = XmlParser.toXml(test);
        Log.d(TAG, "useAppContext: " + xml);
        XmlTest xmlTest = XmlParser.fromXml(xml, XmlTest.class);
        Log.d(TAG, "useAppContext: xmlbean : " + xmlTest.toString());
    }
}
