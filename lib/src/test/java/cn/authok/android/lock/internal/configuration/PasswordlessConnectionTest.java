package cn.authok.android.lock.internal.configuration;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static cn.authok.android.lock.internal.configuration.ConnectionMatcher.hasType;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PasswordlessConnectionTest {

    @Test
    public void shouldHaveName() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "name");
        PasswordlessConnection connection = Connection.newConnectionFor("sms", values);
        assertThat(connection.getName(), is("name"));
    }

    @Test
    public void shouldHaveStrategy() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "name");
        PasswordlessConnection connection = Connection.newConnectionFor("sms", values);
        assertThat(connection.getStrategy(), is("sms"));
    }

    @Test
    public void shouldBePasswordlessType() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "sms");
        PasswordlessConnection connection = Connection.newConnectionFor("sms", values);
        assertThat(connection, hasType(AuthType.PASSWORDLESS));
    }
}