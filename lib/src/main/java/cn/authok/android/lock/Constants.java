/*
 * Constants.java
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

package cn.authok.android.lock;

abstract class Constants {
    static final String LIBRARY_NAME = "lock-android";

    static final String OPTIONS_EXTRA = "cn.authok.android.lock.key.Options";

    static final String CONNECTION_SCOPE_KEY = "connection_scope";

    static final String AUTHENTICATION_ACTION = "cn.authok.android.lock.action.Authentication";
    static final String SIGN_UP_ACTION = "cn.authok.android.lock.action.SignUp";
    static final String CANCELED_ACTION = "cn.authok.android.lock.action.Canceled";
    static final String INVALID_CONFIGURATION_ACTION = "cn.authok.android.lock.action.InvalidConfiguration";

    static final String EXCEPTION_EXTRA = "cn.authok.android.lock.extra.Exception";
    static final String ERROR_EXTRA = "cn.authok.android.lock.extra.Error";
    static final String ID_TOKEN_EXTRA = "cn.authok.android.lock.extra.IdToken";
    static final String ACCESS_TOKEN_EXTRA = "cn.authok.android.lock.extra.AccessToken";
    static final String TOKEN_TYPE_EXTRA = "cn.authok.android.lock.extra.TokenType";
    static final String REFRESH_TOKEN_EXTRA = "cn.authok.android.lock.extra.RefreshToken";
    static final String EXPIRES_AT_EXTRA = "cn.authok.android.lock.extra.ExpiresAt";
    static final String SCOPE_EXTRA = "cn.authok.android.lock.extra.Scope";
    static final String EMAIL_EXTRA = "cn.authok.android.lock.extra.Email";
    static final String USERNAME_EXTRA = "cn.authok.android.lock.extra.Username";
}
