package ru.ifmo.droid2016.rzddemo.api;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import ru.ifmo.droid2016.rzddemo.utils.IOUtils;

/**
 * Created by dmitry.trunin on 08.11.2016.
 */

public interface ApiResponseParser<T> {

    @NonNull
    T parse(InputStream in, String charset) throws IOException, BadResponseException;

    ApiResponseParser NULL_PARSER = new ApiResponseParser() {
        @NonNull
        @Override
        public Object parse(InputStream in, String charset)
                throws IOException, BadResponseException {
            IOUtils.readFully(in);
            return new Object();
        }
    };
}
