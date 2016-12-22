package ru.ifmo.droid2016.rzddemo.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import ru.ifmo.droid2016.rzddemo.utils.IOUtils;

/**
 * Created by dmitry.trunin on 08.11.2016.
 */

public final class RouteIdParser implements ApiResponseParser<String> {

    public String parse(InputStream in, String charset)
            throws IOException, BadResponseException {
        final String response = IOUtils.readToString(in, charset);
        try {
            final JSONObject json = new JSONObject(response);
            if (!"RID".equals(json.getString("result"))) {
                throw new BadResponseException("Unexpected result: " + json.getString("result"));
            }
            return json.getString("rid");
        } catch (JSONException e) {
            throw new BadResponseException("Failed to parse json", e);
        }
    }
}
