package ru.ifmo.droid2016.rzddemo.api;

import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ru.ifmo.droid2016.rzddemo.utils.IOUtils;

import static ru.ifmo.droid2016.rzddemo.Constants.TAG;

/**
 * Created by dmitry.trunin on 08.11.2016.
 */

public final class RZDApi {

    private static final CookieManager rzdCookieManager = new CookieManager();


    public static boolean hasSession() {
        final List<HttpCookie> cookies =
                rzdCookieManager.getCookieStore().getCookies();
        if (cookies != null) {
            for (HttpCookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    if (!cookie.hasExpired()) {
                        return true;
                    } else {
                        rzdCookieManager.getCookieStore().removeAll();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static <T> T executeRequest(HttpURLConnection request, ApiResponseParser<T> parser)
            throws IOException, BadResponseException {
        final StethoURLConnectionManager stethoManager = new StethoURLConnectionManager("API");

        InputStream in = null;

        try {
            Log.d(TAG, "Performing request: " + request.getURL());

            stethoManager.preConnect(request, null);
            request.connect();
            stethoManager.postConnect();

            if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {

                handleCookies(request);

                in = request.getInputStream();
                in = stethoManager.interpretResponseStream(in);

                return parser.parse(in, "UTF-8");

            } else {
                // consider all other codes as errors
                throw new BadResponseException("HTTP: " + request.getResponseCode()
                        + ", " + request.getResponseMessage());
            }


        } catch (IOException e) {
            stethoManager.httpExchangeFailed(e);
            throw e;

        } finally {
            IOUtils.closeSilently(in);
            if (request != null) {
                request.disconnect();
            }
        }
    }

    public static HttpURLConnection createSessionRequest() throws IOException {
        Uri uri = Uri.parse("https://pass.rzd.ru/timetable/public/ru").buildUpon()
                .appendQueryParameter("STRUCTURE_ID", "735")
                .appendQueryParameter("referer", "1")
                .appendQueryParameter("refererPageId", "704")
                .build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        setUserAgent(connection);
        return connection;
    }

    public static HttpURLConnection createTimetableRequest(String fromStation,
                                                           String fromCode,
                                                           String toStation,
                                                           String toCode,
                                                           Calendar dateFrom,
                                                           Calendar dateTo,
                                                           String routeId)
            throws IOException, URISyntaxException {
        Uri uri = createRouteUri(fromStation, fromCode, toStation, toCode, dateFrom, dateTo)
                .appendQueryParameter("rid", routeId)
                .build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        setCommonHeaders(connection);
        return connection;
    }

    public static HttpURLConnection createRouteRequest(String fromStation,
                                                       String fromCode,
                                                       String toStation,
                                                       String toCode,
                                                       Calendar dateFrom,
                                                       Calendar dateTo)
            throws IOException, URISyntaxException {
        Uri uri = createRouteUri(fromStation, fromCode, toStation, toCode, dateFrom, dateTo)
                .build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        setCommonHeaders(connection);
        return connection;
    }

    private static void setCommonHeaders(HttpURLConnection request) {
        request.setRequestProperty("Accept", "application/json, text/javascript, */*");
        request.setRequestProperty("Accept-Language", "ru-RU,ru");
        setUserAgent(request);
        setCookies(request);
    }

    private static Uri.Builder createRouteUri(String fromStation,
                                              String fromCode,
                                              String toStation,
                                              String toCode,
                                              Calendar dateFrom,
                                              Calendar dateTo) {
        return Uri.parse("https://pass.rzd.ru/timetable/public/ru").buildUpon()
                .appendQueryParameter("STRUCTURE_ID", "735")
                .appendQueryParameter("layer_id", "5371")
                .appendQueryParameter("dir", "0")
                .appendQueryParameter("tfl", "3")
                .appendQueryParameter("checkSeats", "0")
                .appendQueryParameter("st0", fromStation)
                .appendQueryParameter("code0", fromCode)
                .appendQueryParameter("st1", toStation)
                .appendQueryParameter("code1", toCode)
                .appendQueryParameter("dt0", DateFormat.format(dateFormat, dateFrom).toString())
                .appendQueryParameter("dt1", DateFormat.format(dateFormat, dateTo).toString());
    }

    private static void setCookies(HttpURLConnection request) {
        final List<HttpCookie> cookies = rzdCookieManager.getCookieStore().getCookies();
        if (cookies != null && cookies.size() > 0) {
            final String cookiesValue = TextUtils.join(";", cookies);
            request.setRequestProperty("Cookie", cookiesValue);
        }
    }

    private static void setUserAgent(HttpURLConnection request) {
        request.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
                        + "Chrome/54.0.2840.71 Safari/537.36");
    }

    public static void handleCookies(HttpURLConnection response) {
        final Map<String, List<String>> headerFields = response.getHeaderFields();
        final List<String> cookiesHeader = headerFields.get("Set-Cookie");
        final CookieStore cookieStore = rzdCookieManager.getCookieStore();

        if (cookiesHeader != null) {
            for (String cookieValue : cookiesHeader) {
                final List<HttpCookie> cookies = HttpCookie.parse(cookieValue);
                for (HttpCookie cookie : cookies) {
                    cookieStore.add(null, cookie);
                }
            }
        }
    }

    private static final String dateFormat = "dd.MM.yyyy";

    private RZDApi() {}
}
