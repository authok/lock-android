/*
 * DbSignUpEvent.java
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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.authok.android.authentication.AuthenticationAPIClient;
import cn.authok.android.authentication.AuthenticationException;
import cn.authok.android.request.Request;
import cn.authok.android.request.SignUpRequest;
import cn.authok.android.result.DatabaseUser;

import java.util.HashMap;
import java.util.Map;

public class DatabaseSignUpEvent extends DatabaseEvent {

    private static final String KEY_USER_METADATA = "user_metadata";

    @NonNull
    private final String password;
    private final Map<String, String> rootAttributes;
    private final Map<String, String> userMetadata;

    public DatabaseSignUpEvent(@NonNull String email, @NonNull String password, @Nullable String username) {
        super(email, username);
        this.password = password;
        this.rootAttributes = new HashMap<>();
        this.userMetadata = new HashMap<>();
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setRootAttributes(@NonNull Map<String, String> attributes) {
        this.rootAttributes.putAll(attributes);
    }

    /**
     * Set the fields to set as user_metadata
     *
     * @param customFields user_metadata fields to set
     */
    public void setExtraFields(@NonNull Map<String, String> customFields) {
        this.userMetadata.putAll(customFields);
    }


    @NonNull
    public SignUpRequest getSignUpRequest(@NonNull AuthenticationAPIClient apiClient, @NonNull String connection) {
        SignUpRequest request = apiClient.signUp(getEmail(), getPassword(), getUsername(), connection, userMetadata);
        if (!rootAttributes.isEmpty()) {
            request.addSignUpParameters(rootAttributes);
        }
        return request;
    }

    @NonNull
    public Request<DatabaseUser, AuthenticationException> getCreateUserRequest(@NonNull AuthenticationAPIClient apiClient, @NonNull String connection) {
        Request<DatabaseUser, AuthenticationException> request = apiClient.createUser(getEmail(), getPassword(), getUsername(), connection, userMetadata);
        if (!rootAttributes.isEmpty()) {
            request.addParameters(rootAttributes);
        }
        return request;
    }
}
