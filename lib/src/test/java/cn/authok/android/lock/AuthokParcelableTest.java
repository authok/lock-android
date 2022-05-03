package cn.authok.android.lock;

import android.os.Parcel;

import cn.authok.android.Authok;
import cn.authok.android.util.AuthokUserAgent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import okhttp3.HttpUrl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21)
public class AuthokParcelableTest {

    private static final String CLIENT_ID = "CLIENT_ID";
    private static final String DOMAIN = "https://my-domain.cn.authok.cncom";
    private static final String CONFIG_DOMAIN = "https://my-cdn.authok.cn";

    @Test
    public void shouldSaveClientId() {
        Authok authok = new Authok(CLIENT_ID, DOMAIN, CONFIG_DOMAIN);
        cn.authok.android.lock.AuthokParcelable authokParcelable = new cn.authok.android.lock.AuthokParcelable(authok);
        Parcel parcel = Parcel.obtain();
        authokParcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        cn.authok.android.lock.AuthokParcelable parceledAuthok = cn.authok.android.lock.AuthokParcelable.CREATOR.createFromParcel(parcel);
        assertThat(authok.getClientId(), is(equalTo(CLIENT_ID)));
        assertThat(parceledAuthok.getAuthok().getClientId(), is(equalTo(CLIENT_ID)));
    }

    @Test
    public void shouldSaveDomainUrl() {
        Authok authok = new Authok(CLIENT_ID, DOMAIN, CONFIG_DOMAIN);
        cn.authok.android.lock.AuthokParcelable authokParcelable = new cn.authok.android.lock.AuthokParcelable(authok);
        Parcel parcel = Parcel.obtain();
        authokParcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        cn.authok.android.lock.AuthokParcelable parceledAuthok = cn.authok.android.lock.AuthokParcelable.CREATOR.createFromParcel(parcel);
        assertThat(HttpUrl.parse(authok.getDomainUrl()), is(equalTo(HttpUrl.parse(DOMAIN))));
        assertThat(HttpUrl.parse(parceledAuthok.getAuthok().getDomainUrl()), is(equalTo(HttpUrl.parse(DOMAIN))));
    }

    @Test
    public void shouldSaveConfigurationUrl() {
        Authok authok = new Authok(CLIENT_ID, DOMAIN, CONFIG_DOMAIN);
        cn.authok.android.lock.AuthokParcelable authokParcelable = new cn.authok.android.lock.AuthokParcelable(authok);
        Parcel parcel = Parcel.obtain();
        authokParcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        cn.authok.android.lock.AuthokParcelable parceledAuthok = cn.authok.android.lock.AuthokParcelable.CREATOR.createFromParcel(parcel);
        assertThat(HttpUrl.parse(authok.getConfigurationUrl()), is(equalTo(HttpUrl.parse(CONFIG_DOMAIN))));
        assertThat(HttpUrl.parse(parceledAuthok.getAuthok().getConfigurationUrl()), is(equalTo(HttpUrl.parse(CONFIG_DOMAIN))));
    }

    @Test
    public void shouldSaveUserAgent() {
        AuthokUserAgent userAgent = new AuthokUserAgent("name", "version", "libraryVersion");
        Authok authok = new Authok(CLIENT_ID, DOMAIN, CONFIG_DOMAIN);
        authok.setAuthokUserAgent(userAgent);
        cn.authok.android.lock.AuthokParcelable authokParcelable = new cn.authok.android.lock.AuthokParcelable(authok);
        Parcel parcel = Parcel.obtain();
        authokParcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        cn.authok.android.lock.AuthokParcelable parceledAuthok = cn.authok.android.lock.AuthokParcelable.CREATOR.createFromParcel(parcel);
        assertThat(userAgent.getValue(), is(notNullValue()));
        assertThat(authok.getAuthokUserAgent().getValue(), is(equalTo(userAgent.getValue())));
        assertThat(parceledAuthok.getAuthok().getAuthokUserAgent().getValue(), is(equalTo(userAgent.getValue())));
    }

}