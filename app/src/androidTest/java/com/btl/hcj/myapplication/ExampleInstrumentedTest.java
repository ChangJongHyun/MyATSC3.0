package com.btl.hcj.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.btl.hcj.myapplication.Serialize.Direction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.btl.hcj.myapplication", appContext.getPackageName());

        InputStream is = null;
        AssetManager am = appContext.getAssets();

        for (int i = 0; i < 10; i++) {
            testDeserialize(is, am, i);
        }


    }

    public void testDeserialize(InputStream is, AssetManager am, int i) {
        String fileData = null;
        try {
            is = am.open("path.json");
            fileData = MyUtils.getJson(is);

            long t1 = System.currentTimeMillis();
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

            Direction d = gson.fromJson(fileData, Direction.class);
            String jsonD = gson.toJson(d);

            long t2 = System.currentTimeMillis();

            Log.i("AndroidText", "Deserialize: " + "# of" + (i + 1) + " takes " + ((t2 - t1) / 1000.0) + " seconds");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
