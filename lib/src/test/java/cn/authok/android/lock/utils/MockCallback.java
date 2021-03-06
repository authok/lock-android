/*
 * MockBaseCallback.java
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

package cn.authok.android.lock.utils;


import androidx.annotation.NonNull;

import cn.authok.android.AuthokException;
import cn.authok.android.callback.Callback;

import java.util.concurrent.Callable;

public class MockCallback<T, U extends AuthokException> implements Callback<T, U> {

    private T payload;
    private U error;

    @Override
    public void onSuccess(@NonNull T payload) {
        this.payload = payload;
    }

    @Override
    public void onFailure(@NonNull U error) {
        this.error = error;
    }

    public Callable<T> payload() {
        return new Callable<T>() {
            @Override
            public T call() {
                return payload;
            }
        };
    }

    public Callable<U> error() {
        return new Callable<U>() {
            @Override
            public U call() {
                return error;
            }
        };
    }

    public T getPayload() {
        return payload;
    }

    public U getError() {
        return error;
    }
}
