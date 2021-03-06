/*
 * ApplicationFetcher.java
 *
 * Copyright (c) 2022 Authok (http://authok.cn)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package cn.authok.android.lock.internal.configuration;

import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import cn.authok.android.Authok;
import cn.authok.android.AuthokException;
import cn.authok.android.authentication.AuthenticationException;
import cn.authok.android.callback.Callback;
import cn.authok.android.request.HttpMethod;
import cn.authok.android.request.RequestOptions;
import cn.authok.android.request.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

public class ApplicationFetcher {

    private static final String JSONP_PREFIX = "Authok.setClient(";

    private final Authok account;

    /**
     * Helper class to fetch the Application information from Authok Dashboard.
     *
     * @param account the Application details to build the request uri.
     */
    public ApplicationFetcher(@NonNull Authok account) {
        this.account = account;
    }

    /**
     * Fetch application information from Authok
     *
     * @param callback to notify on success/error
     */
    public void fetch(@NonNull Callback<List<Connection>, AuthokException> callback) {
        FetchTask task = new FetchTask(account);
        task.execute(callback);
    }

    @VisibleForTesting
    static Gson createGson() {
        Type applicationType = new TypeToken<List<Connection>>() {
        }.getType();
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(applicationType, new ApplicationDeserializer())
                .create();
    }

    private static class FetchTask extends AsyncTask<Callback<List<Connection>, AuthokException>, Void, Void> {

        private final Authok account;

        public FetchTask(Authok account) {
            this.account = account;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(Callback<List<Connection>, AuthokException>... callbacks) {
            makeApplicationRequest(account, callbacks[0]);
            return null;
        }

        private void makeApplicationRequest(Authok account, Callback<List<Connection>, AuthokException> callback) {
            Uri uri = Uri.parse(account.getConfigurationUrl()).buildUpon().appendPath("client")
                    .appendPath(account.getClientId() + ".js").build();

            try {
                ServerResponse res = account.getNetworkingClient().load(uri.toString(), new RequestOptions(HttpMethod.GET.INSTANCE));
                List<Connection> connections = parseJSONP(res.getBody());
                callback.onSuccess(connections);
            } catch (IOException e) {
                AuthokException exception = new AuthokException("An error occurred while fetching the client information from the CDN.", e);
                callback.onFailure(new AuthenticationException("Failed to fetch the Application", exception));
            } catch (AuthokException e) {
                callback.onFailure(new AuthenticationException("Could not parse Application JSONP", e));
            }
        }

        private List<Connection> parseJSONP(InputStream is) throws AuthokException {
            try {
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                for (int result = bis.read(); result != -1; result = bis.read()) {
                    buf.write((byte) result);
                }
                String json = buf.toString(Charset.defaultCharset().name());
                final int length = JSONP_PREFIX.length();
                if (json.length() < length) {
                    throw new JSONException("Invalid App Info JSONP");
                }
                json = json.substring(length);
                JSONTokener tokenizer = new JSONTokener(json);
                if (!tokenizer.more()) {
                    throw tokenizer.syntaxError("Invalid App Info JSONP");
                }
                Object nextValue = tokenizer.nextValue();
                if (!(nextValue instanceof JSONObject)) {
                    tokenizer.back();
                    throw tokenizer.syntaxError("Invalid JSON value of App Info");
                }
                JSONObject jsonObject = (JSONObject) nextValue;
                Type applicationType = new TypeToken<List<Connection>>() {
                }.getType();
                return createGson().fromJson(jsonObject.toString(), applicationType);
            } catch (IOException | JSONException e) {
                throw new AuthokException("Failed to parse response to request", e);
            }
        }
    }
}
