package com.btl.hcj.myapplication;

import com.btl.hcj.myapplication.Serialize.Direction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    public ExampleUnitTest() throws IOException {

        long t1 = System.currentTimeMillis();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        String fileData = new String(Files.readAllBytes(Paths.get("./test.json")));
        Direction d = gson.fromJson(fileData, Direction.class);
        String jsonD = gson.toJson(d);

        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000.0 + " seconds");
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

}
