/*
 * AuthokParcelable.java
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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import cn.authok.android.Authok;
import cn.authok.android.util.AuthokUserAgent;


/**
 * This class wraps a {@link Authok} to make it Parcelable
 */
public class AuthokParcelable implements Parcelable {

    private static final double WITHOUT_DATA = 0x00;
    private static final double WITH_DATA = 0x01;
    private final Authok authok;

    public AuthokParcelable(@NonNull Authok authok) {
        this.authok = authok;
    }

    @NonNull
    public Authok getAuthok() {
        return authok;
    }

    // PARCELABLE
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(authok.getClientId());
        dest.writeString(authok.getDomainUrl());
        dest.writeString(authok.getConfigurationUrl());
        //FIXME: Find a way to pass the NetworkingClient implementation
        dest.writeString(authok.getAuthokUserAgent().getName());
        dest.writeString(authok.getAuthokUserAgent().getVersion());
        dest.writeString(authok.getAuthokUserAgent().getLibraryVersion());
    }

    public static final Parcelable.Creator<cn.authok.android.lock.AuthokParcelable> CREATOR
            = new Parcelable.Creator<cn.authok.android.lock.AuthokParcelable>() {
        public cn.authok.android.lock.AuthokParcelable createFromParcel(Parcel in) {
            return new cn.authok.android.lock.AuthokParcelable(in);
        }

        public cn.authok.android.lock.AuthokParcelable[] newArray(int size) {
            return new cn.authok.android.lock.AuthokParcelable[size];
        }
    };

    private AuthokParcelable(@NonNull Parcel in) {
        String clientId = in.readString();
        String domain = in.readString();
        String configurationDomain = in.readString();
        String telemetryName = in.readString();
        String telemetryVersion = in.readString();
        String telemetryLibraryVersion = in.readString();

        authok = new Authok(clientId, domain, configurationDomain);
        AuthokUserAgent userAgent = new AuthokUserAgent(telemetryName, telemetryVersion, telemetryLibraryVersion);
        authok.setAuthokUserAgent(userAgent);
    }
}
