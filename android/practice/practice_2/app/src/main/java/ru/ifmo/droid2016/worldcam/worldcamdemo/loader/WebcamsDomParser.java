package ru.ifmo.droid2016.worldcam.worldcamdemo.loader;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.ifmo.droid2016.worldcam.worldcamdemo.model.Webcam;

/**
 * Методы для парсинга ответов от Webcams API при помощи JSONObject (DOM parser)
 */
public final class WebcamsDomParser {

    private static JSONObject parseJSON(InputStream in) throws JSONException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        in = new BufferedInputStream(in);

        byte[] buffer = new byte[8192];
        int readSize;

        while ((readSize = in.read(buffer)) >= 0) {
            baos.write(buffer, 0, readSize);
        }

        in.close();

        String content = baos.toString("UTF-8");
        baos.close();

        return new JSONObject(content);
    }

    @NonNull
    public static List<Webcam> parseWebcams(InputStream in) throws
            IOException,
            JSONException,
            BadResponseException {

        JSONObject res = parseJSON(in);
        List<Webcam> webcamsList = new ArrayList<>();

        if (!"OK".equals(res.optString("status", "ERROR"))) {
            throw new BadResponseException("API Error");
        }

        JSONArray resWebcams = res.getJSONObject("result").getJSONArray("webcams");

        for (int i = 0; i < resWebcams.length(); i++) {
            try {
                JSONObject webcam = resWebcams.getJSONObject(i);
                webcamsList.add(new Webcam(
                        webcam.optString("id", "Unknown ID"),
                        webcam.optString("title", "Unknown webcam"),
                        // if image is not presented, this cam is useless -> getString
                        webcam.getJSONObject("image").getJSONObject("daylight").getString("preview")
                ));
            } catch (JSONException ignored) {
            }
        }

        return webcamsList;
    }

    private WebcamsDomParser() {}
}
