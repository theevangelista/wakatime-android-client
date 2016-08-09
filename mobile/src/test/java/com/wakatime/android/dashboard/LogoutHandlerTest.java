package com.wakatime.android.dashboard;

import com.wakatime.android.BuildConfig;
import com.wakatime.android.WakatimeApplication;
import com.wakatime.android.test.support.MockSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Joao Pedro Evangelista
 */
@Config(sdk = 23, constants = BuildConfig.class,
        application = WakatimeApplication.class,
        shadows = ShadowMultiDex.class)
@RunWith(RobolectricTestRunner.class)
public class LogoutHandlerTest {

    private LogoutHandler mTarget;

    @Before
    public void setUp() throws Exception {
        mTarget = new LogoutHandler(MockSupport.mockRealm());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldClearAllDataOnDevice() throws Exception {
        final int[] count = {0};
        mTarget.clearData(() -> count[0] = 1);

        assertThat(count[0]).isEqualTo(1);
    }

}