package com.wakatime.android;

import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import static org.assertj.android.api.Assertions.assertThat;

/**
 * @author Joao Pedro Evangelista
 */
@Config(sdk = 23, constants = BuildConfig.class,
        application = WakatimeApplication.class,
        shadows = ShadowMultiDex.class)
@RunWith(RobolectricGradleTestRunner.class)
public class AboutActivityTest {

    private AboutActivity mTarget;

    @Before
    public void setUp() throws Exception {
        mTarget = Robolectric.setupActivity(AboutActivity.class);
    }

    @After
    public void tearDown() throws Exception {
        mTarget = null;
    }

    @Test
    public void shouldClickOnButtonForLicenses() throws Exception {
        Button licencesButton = (Button) mTarget.findViewById(R.id.btn_license);
        assertThat(licencesButton).isVisible()
                .hasText(R.string.os_license);

        licencesButton.performClick();
    }

    @Test
    public void shouldHaveVersionTextView() throws Exception {
        TextView versionTxt = (TextView) mTarget.findViewById(R.id.text_view_version);
        assertThat(versionTxt).isVisible()
                .hasText(R.string.version);

    }
}