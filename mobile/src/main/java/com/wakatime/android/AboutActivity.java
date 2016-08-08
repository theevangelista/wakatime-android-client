package com.wakatime.android;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marcoscg.easylicensesdialog.EasyLicensesDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.text_view_version)
    TextView mTextViewVersion;

    @BindView(R.id.btn_license)
    Button mBtnLicense;
    private EasyLicensesDialog easyLicensesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        easyLicensesDialog = new EasyLicensesDialog(this);
        setVersion();
    }

    private void setVersion() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            mTextViewVersion.setText(getString(R.string.version, info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            mTextViewVersion.setText(R.string.unknown_version);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // remove from backstack and go back to dashboard below it
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (easyLicensesDialog != null) {
            easyLicensesDialog = null; // remove reference since it leaks
        }
    }

    @OnClick(R.id.btn_license)
    void openLicenseDialog(View button) {
        easyLicensesDialog.setTitle(getString(R.string.licenses));
        easyLicensesDialog.setCancelable(true);
        easyLicensesDialog.show();
    }
}
