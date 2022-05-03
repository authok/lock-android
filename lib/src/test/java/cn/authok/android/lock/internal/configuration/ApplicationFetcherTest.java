/*
 * ApplicationFetcherTest.java
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

import cn.authok.android.Authok;
import cn.authok.android.AuthokException;
import cn.authok.android.lock.utils.ApplicationAPI;
import cn.authok.android.lock.utils.CallbackMatcher;
import cn.authok.android.lock.utils.MockCallback;
import cn.authok.android.lock.utils.SSLTestUtils;
import com.google.gson.reflect.TypeToken;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class ApplicationFetcherTest {

    private ApplicationFetcher appFetcher;
    private ApplicationAPI mockAPI;

    @Before
    public void setUp() throws Exception {
        SSLTestUtils sslUtils = new SSLTestUtils();
        mockAPI = new ApplicationAPI(sslUtils);

        final Options options = Mockito.mock(Options.class);
        Authok account = new Authok("client_id", mockAPI.getDomain());
        account.setNetworkingClient(sslUtils.testClient);
        Mockito.when(options.getAccount()).thenReturn(account);
        appFetcher = new ApplicationFetcher(account);
    }

    @After
    public void tearDown() throws Exception {
        mockAPI.shutdown();
    }

    @Test
    public void shouldReturnApplicationOnValidJSONPResponse() throws Exception {
        mockAPI.willReturnValidJSONPResponse();
        final MockCallback<List<Connection>, AuthokException> callback = new MockCallback<>();
        appFetcher.fetch(callback);
        mockAPI.takeRequest();

        TypeToken<List<Connection>> applicationType = new TypeToken<List<Connection>>(){};
        TypeToken<AuthokException> errorType = new TypeToken<AuthokException>() {};
        assertThat(callback, CallbackMatcher.hasPayloadOfType(applicationType, errorType));
    }

    @Test
    public void shouldReturnExceptionOnInvalidJSONPResponse() throws Exception {
        mockAPI.willReturnInvalidJSONPLengthResponse();
        final MockCallback<List<Connection>, AuthokException> callback = new MockCallback<>();
        appFetcher.fetch(callback);
        mockAPI.takeRequest();

        TypeToken<List<Connection>> applicationType = new TypeToken<List<Connection>>(){};
        TypeToken<AuthokException> errorType = new TypeToken<AuthokException>() {};
        assertThat(callback, CallbackMatcher.hasErrorOfType(applicationType, errorType));
        assertThat(callback.getError(), CoreMatchers.instanceOf(AuthokException.class));
        assertThat(callback.getError().getCause().getCause().getMessage(), CoreMatchers.containsString("Invalid App Info JSONP"));
    }
}