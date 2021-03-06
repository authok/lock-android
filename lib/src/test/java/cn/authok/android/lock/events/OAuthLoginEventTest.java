package cn.authok.android.lock.events;

import cn.authok.android.lock.internal.configuration.OAuthConnection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class OAuthLoginEventTest {

    private OAuthConnection connection;

    @Before
    public void setUp() {
        connection = mock(OAuthConnection.class);
        when(connection.getName()).thenReturn("connectionName");
        when(connection.getStrategy()).thenReturn("strategyName");
    }

    @Test
    public void shouldGetStrategyName() {
        OAuthLoginEvent roEvent = new OAuthLoginEvent(connection, "username", "password");
        assertThat(roEvent.getStrategy(), is("strategyName"));

        OAuthLoginEvent webEvent = new OAuthLoginEvent(connection);
        assertThat(webEvent.getStrategy(), is("strategyName"));
    }

    @Test
    public void shouldGetConnectionName() {
        OAuthLoginEvent roEvent = new OAuthLoginEvent(connection, "username", "password");
        assertThat(roEvent.getConnection(), is("connectionName"));

        OAuthLoginEvent webEvent = new OAuthLoginEvent(connection);
        assertThat(webEvent.getConnection(), is("connectionName"));
    }

    @Test
    public void shouldUseActiveFlow() {
        OAuthLoginEvent roEvent = new OAuthLoginEvent(connection, "username", "password");
        assertTrue(roEvent.useActiveFlow());
    }

    @Test
    public void shouldHaveUsernameOnActiveFlow() {
        OAuthLoginEvent roEvent = new OAuthLoginEvent(connection, "username", "password");
        assertThat(roEvent.getUsername(), is("username"));
    }

    @Test
    public void shouldHavePasswordOnActiveFlow() {
        OAuthLoginEvent roEvent = new OAuthLoginEvent(connection, "username", "password");
        assertThat(roEvent.getPassword(), is("password"));
    }

    @Test
    public void shouldUseWebAuth() {
        OAuthLoginEvent webEvent = new OAuthLoginEvent(connection);
        assertFalse(webEvent.useActiveFlow());
    }

    @Test
    public void shouldNotHaveUsernameOnWebAuth() {
        OAuthLoginEvent webEvent = new OAuthLoginEvent(connection);
        assertThat(webEvent.getUsername(), is(nullValue()));
    }

    @Test
    public void shouldNotHavePasswordOnWebAuth() {
        OAuthLoginEvent webEvent = new OAuthLoginEvent(connection);
        assertThat(webEvent.getPassword(), is(nullValue()));
    }
}