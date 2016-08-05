package com.wakatime.androidclient.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.wakatime.androidclient.R;
import com.wakatime.androidclient.WakatimeApplication;
import com.wakatime.androidclient.dashboard.environment.EnvironmentFragment;
import com.wakatime.androidclient.dashboard.project.ProjectFragment;
import com.wakatime.androidclient.support.context.NavigationHeaderView;
import com.wakatime.androidclient.user.UserStartActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity
        implements LogoutActionView, NavigationView.OnNavigationItemSelectedListener,
        EnvironmentFragment.OnProgrammingFragmentInteractionListener,
        ProjectFragment.OnProjectFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.content_dashboard)
    FrameLayout contentDashboard;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    NavigationHeaderView navigationHeaderView;

    @Inject
    LogoutHandler mLogoutHandler;

    private Fragment programmingFragment;

    private Fragment projectFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        ((WakatimeApplication) this.getApplication()).useNetworkComponent()
                .inject(this);

        toolbar.setTitle(R.string.title_activity_dashboard);
        setSupportActionBar(toolbar);

        restoreFragments(savedInstanceState);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navView.setNavigationItemSelectedListener(this);
        navigationHeaderView.on(navView.getHeaderView(0)).load();
        // Called new fragment when there is no other to saved
        if (savedInstanceState == null) {
            changeToDefaultFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (programmingFragment != null && programmingFragment.isAdded()) {
            getSupportFragmentManager()
                    .putFragment(outState, EnvironmentFragment.KEY, this.programmingFragment);
        }
        if (projectFragment != null && projectFragment.isAdded()) {
            getSupportFragmentManager()
                    .putFragment(outState, ProjectFragment.KEY, this.projectFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_environment) {
            changeToDefaultFragment();
        } else if (id == R.id.drawer_projects) {
            this.projectFragment = ProjectFragment.newInstance();
            changeFragment(this.projectFragment);
        } else if (id == R.id.drawer_logout) {
            this.logout();
        } else {
            changeToDefaultFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeToDefaultFragment() {
        this.programmingFragment = EnvironmentFragment.newInstance();
        changeFragment(this.programmingFragment);
    }

    private void restoreFragments(Bundle bundle) {
        if (bundle == null) return;

        this.programmingFragment = getSupportFragmentManager()
                .getFragment(bundle, EnvironmentFragment.KEY);

        this.projectFragment = getSupportFragmentManager()
                .getFragment(bundle, ProjectFragment.KEY);
    }

    private void changeFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_dashboard, fragment)
                .commit();
    }

    @Override
    public void logout() {
        mLogoutHandler.clearData(() -> {
            startActivity(new Intent(this, UserStartActivity.class));
            finish();
        });

    }
}
