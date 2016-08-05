package com.wakatime.androidclient.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.WakatimeApplication;
import com.wakatime.androidclient.dashboard.DashboardActivity;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity implements ViewModel {

    @BindView(R.id.edit_text_api_key)
    TextInputEditText mEditTextApiKey;

    @BindView(R.id.input_layout_api_key)
    TextInputLayout mInputLayoutApiKey;

    @BindView(R.id.loader_user)
    SpinKitView mLoaderUser;

    @BindView(R.id.welcome_text)
    TextView mWelcomeText;

    @BindView(R.id.container_user)
    RelativeLayout mContainerUser;

    @BindView(R.id.container_key)
    LinearLayout mContainerKey;

    @BindView(R.id.text_view_credits_icon)
    TextView mCreditsIcon;

    @BindView(R.id.text_view_credits_wakatime)
    TextView mCreditsWakatime;

    @Inject
    StartPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        ((WakatimeApplication) this.getApplication()).useApiKeyComponent().inject(this);
        mCreditsIcon.setMovementMethod(LinkMovementMethod.getInstance());
        mCreditsWakatime.setMovementMethod(LinkMovementMethod.getInstance());
        mPresenter.bind(this);
        mPresenter.checkIfKeyPresent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
    }

    @Override
    public void sendUserToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
        finish(); // avoid user going back here
    }

    @Override
    public void setErrors(Map<String, Integer> errors) {
        if (!errors.isEmpty()) {
            mInputLayoutApiKey.setError(getString(errors.get("key_out_of_bounds")));
        }
    }

    @Override
    public void showError() {
        Snackbar.make(mContainerUser,
                R.string.user_fetch_error,
                Snackbar.LENGTH_LONG);
    }

    @OnClick(R.id.btn_continue)
    public void onClick() {
        mPresenter.saveUserData(this.mEditTextApiKey.getText().toString());
    }

    @Override
    public void hideLoader() {
        this.mLoaderUser.setVisibility(View.GONE);
        this.mInputLayoutApiKey.setVisibility(View.VISIBLE);
        this.mWelcomeText.setVisibility(View.VISIBLE);
        this.mContainerKey.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoader() {
        this.mLoaderUser.setVisibility(View.VISIBLE);
        this.mInputLayoutApiKey.setVisibility(View.GONE);
        this.mWelcomeText.setVisibility(View.GONE);
        this.mContainerKey.setVisibility(View.GONE);
    }
}
