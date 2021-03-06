/*
 * PasswordlessLoginEventTest.java
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

package cn.authok.android.lock.events;

import cn.authok.android.authentication.AuthenticationAPIClient;
import cn.authok.android.authentication.AuthenticationException;
import cn.authok.android.authentication.PasswordlessType;
import cn.authok.android.lock.adapters.Country;
import cn.authok.android.lock.internal.configuration.PasswordlessMode;
import cn.authok.android.request.AuthenticationRequest;
import cn.authok.android.request.Request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class PasswordlessLoginEventTest {

    private static final String CONNECTION_NAME = "connectionName";
    private static final String EMAIL = "an@email.com";
    private static final String PHONE_NUMBER_WITH_CODE = "+11234567890";
    private static final String PHONE_NUMBER_WITHOUT_CODE = "1234567890";
    private static final String CODE = "123456";
    private static final String CONNECTION_KEY = "connection";

    private Country country;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        country = new Country("US", "+1");
    }

    @Test
    public void shouldHaveNullCodeByDefault() {
        PasswordlessLoginEvent event = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_LINK, EMAIL);

        Assert.assertThat(event.getEmailOrNumber(), is(equalTo(EMAIL)));
        Assert.assertThat(event.getMode(), is(equalTo(PasswordlessMode.EMAIL_LINK)));
        Assert.assertThat(event.getCode(), is(nullValue()));
    }

    @Test
    public void shouldSetTheCode() {
        PasswordlessLoginEvent event = PasswordlessLoginEvent.submitCode(PasswordlessMode.EMAIL_CODE, CODE);

        Assert.assertThat(event.getMode(), is(equalTo(PasswordlessMode.EMAIL_CODE)));
        Assert.assertThat(event.getCode(), is(equalTo(CODE)));
    }

    @Test
    public void shouldGetValidCodeRequestWhenUsingEmailAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithEmail(EMAIL, PasswordlessType.CODE)).thenReturn(request);
        when(request.addParameter(CONNECTION_KEY, CONNECTION_NAME)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_CODE, EMAIL);
        Request<Void, AuthenticationException> resultRequest = emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(request));
    }

    @Test
    public void shouldGetValidCodeRequestWhenUsingEmailAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithEmail(EMAIL, PasswordlessType.ANDROID_LINK)).thenReturn(request);
        when(request.addParameter(CONNECTION_KEY, CONNECTION_NAME)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_LINK, EMAIL);
        Request<Void, AuthenticationException> resultRequest = emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(request));
    }

    @Test
    public void shouldGetValidCodeRequestWhenUsingSMSAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.CODE)).thenReturn(request);
        when(request.addParameter(CONNECTION_KEY, CONNECTION_NAME)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.SMS_CODE, PHONE_NUMBER_WITHOUT_CODE, country);
        Request<Void, AuthenticationException> resultRequest = emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(request));
    }

    @Test
    public void shouldGetValidCodeRequestWhenUsingSMSAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.ANDROID_LINK)).thenReturn(request);
        when(request.addParameter(CONNECTION_KEY, CONNECTION_NAME)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.SMS_LINK, PHONE_NUMBER_WITHOUT_CODE, country);
        Request<Void, AuthenticationException> resultRequest = emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(request));
    }

    @Test
    public void shouldCallApiClientPasswordlessStartWhenUsingSMSAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.ANDROID_LINK)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.SMS_LINK, PHONE_NUMBER_WITHOUT_CODE, country);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(client).passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.ANDROID_LINK);
    }

    @Test
    public void shouldCallApiClientPasswordlessStartWhenUsingSMSAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.CODE)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.SMS_CODE, PHONE_NUMBER_WITHOUT_CODE, country);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(client).passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.CODE);
    }

    @Test
    public void shouldCallApiClientPasswordlessStartWhenUsingEmailAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithEmail(EMAIL, PasswordlessType.ANDROID_LINK)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_LINK, EMAIL);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(client).passwordlessWithEmail(EMAIL, PasswordlessType.ANDROID_LINK);
    }

    @Test
    public void shouldCallApiClientPasswordlessStartWhenUsingEmailAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithEmail(EMAIL, PasswordlessType.CODE)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_CODE, EMAIL);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(client).passwordlessWithEmail(EMAIL, PasswordlessType.CODE);
    }

    @Test
    public void shouldSetConnectionWhenUsingSMSAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.ANDROID_LINK)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.SMS_LINK, PHONE_NUMBER_WITHOUT_CODE, country);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(request).addParameter(CONNECTION_KEY, CONNECTION_NAME);
    }

    @Test
    public void shouldSetConnectionWhenWhenUsingSMSAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithSMS(PHONE_NUMBER_WITH_CODE, PasswordlessType.CODE)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.SMS_CODE, PHONE_NUMBER_WITHOUT_CODE, country);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(request).addParameter(CONNECTION_KEY, CONNECTION_NAME);
    }

    @Test
    public void shouldSetConnectionWhenWhenUsingEmailAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithEmail(EMAIL, PasswordlessType.ANDROID_LINK)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_LINK, EMAIL);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(request).addParameter(CONNECTION_KEY, CONNECTION_NAME);
    }

    @Test
    public void shouldSetConnectionWhenWhenUsingEmailAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        Request<Void, AuthenticationException> request = mock(Request.class);
        when(client.passwordlessWithEmail(EMAIL, PasswordlessType.CODE)).thenReturn(request);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.requestCode(PasswordlessMode.EMAIL_CODE, EMAIL);
        emailCodeEvent.getCodeRequest(client, CONNECTION_NAME);

        verify(request).addParameter(CONNECTION_KEY, CONNECTION_NAME);
    }

    @Test
    public void shouldGetValidLoginRequestWhenUsingEmailAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithEmail(EMAIL, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.EMAIL_CODE, CODE);
        AuthenticationRequest resultRequest = emailCodeEvent.getLoginRequest(client, EMAIL);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(authRequest));
    }

    @Test
    public void shouldGetValidLoginRequestWhenUsingEmailAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithEmail(EMAIL, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.EMAIL_LINK, CODE);
        AuthenticationRequest resultRequest = emailCodeEvent.getLoginRequest(client, EMAIL);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(authRequest));
    }

    @Test
    public void shouldGetValidLoginRequestWhenUsingSMSAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithPhoneNumber(PHONE_NUMBER_WITH_CODE, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.SMS_CODE, CODE);
        AuthenticationRequest resultRequest = emailCodeEvent.getLoginRequest(client, PHONE_NUMBER_WITH_CODE);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(authRequest));
    }

    @Test
    public void shouldGetValidLoginRequestWhenUsingSMSAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithPhoneNumber(PHONE_NUMBER_WITH_CODE, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.SMS_LINK, CODE);
        AuthenticationRequest resultRequest = emailCodeEvent.getLoginRequest(client, PHONE_NUMBER_WITH_CODE);

        Assert.assertThat(resultRequest, notNullValue());
        Assert.assertThat(resultRequest, equalTo(authRequest));
    }

    @Test
    public void shouldCallApiClientPasswordlessLoginWhenUsingEmailAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithEmail(EMAIL, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.EMAIL_CODE, CODE);
        emailCodeEvent.getLoginRequest(client, EMAIL);

        verify(client).loginWithEmail(EMAIL, CODE);
        verify(client, never()).getProfileAfter(authRequest);
    }

    @Test
    public void shouldCallApiClientPasswordlessLoginWhenUsingEmailAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithEmail(EMAIL, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.EMAIL_LINK, CODE);
        emailCodeEvent.getLoginRequest(client, EMAIL);

        verify(client).loginWithEmail(EMAIL, CODE);
        verify(client, never()).getProfileAfter(authRequest);
    }

    @Test
    public void shouldCallApiClientPasswordlessLoginWhenUsingSMSAndCode() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithPhoneNumber(PHONE_NUMBER_WITH_CODE, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.SMS_CODE, CODE);
        emailCodeEvent.getLoginRequest(client, PHONE_NUMBER_WITH_CODE);

        verify(client).loginWithPhoneNumber(PHONE_NUMBER_WITH_CODE, CODE);
        verify(client, never()).getProfileAfter(authRequest);
    }

    @Test
    public void shouldCallApiClientPasswordlessLoginWhenUsingSMSAndLink() {
        AuthenticationAPIClient client = mock(AuthenticationAPIClient.class);
        AuthenticationRequest authRequest = mock(AuthenticationRequest.class);
        when(client.loginWithPhoneNumber(PHONE_NUMBER_WITH_CODE, CODE)).thenReturn(authRequest);

        PasswordlessLoginEvent emailCodeEvent = PasswordlessLoginEvent.submitCode(PasswordlessMode.SMS_LINK, CODE);
        emailCodeEvent.getLoginRequest(client, PHONE_NUMBER_WITH_CODE);

        verify(client).loginWithPhoneNumber(PHONE_NUMBER_WITH_CODE, CODE);
        verify(client, never()).getProfileAfter(authRequest);
    }
}