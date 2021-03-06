package group.project.buddi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Class to handle main execution and contains all fragments
 *
 * @author Team Buddi
 * @version 1.0
 */
public class HomeActivity extends AppCompatActivity {

    // Initialize variables
    private AppBarLayout mAppBarLayout;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView mName;
    private TextView mEmail;
    private MatchesFragment matchesFragment = new MatchesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set elevation of Toolbar to 0
        mAppBarLayout = (AppBarLayout) findViewById(R.id.mAppBarLayout);
        mAppBarLayout.setElevation(0);

        // Set a Toolbar to replace the ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        // Find our navigation view
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        setFragment(matchesFragment);

        // Find our navigation drawer header view
        View hView = nvDrawer.getHeaderView(0);

        // Set up header text
        mName = (TextView) hView.findViewById(R.id.headerName);
        mEmail = (TextView) hView.findViewById(R.id.headerEmail);

        // Load user information into nav header
        loadInfo();
    }

    /**
     * Method to load name and email address into navigation drawer
     */
    private void loadInfo() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.oauth), Context.MODE_PRIVATE);

        mName.setText(sharedPref.getString("name", "Name"));
        mEmail.setText(sharedPref.getString("email", "email@domain.com"));
    }

    /**
     * Sets current fragment
     *
     * @param fragment fragment to set
     */
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();
    }

    /**
     * Method for toggling drawer in toolbar
     *
     * @return
     */
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Method to set up drawer content
     *
     * @param navigationView the navigation drawer view
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    /**
     * Method to handle drawer item action
     *
     * @param menuItem which menuItem has been selected
     */
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;

        // Change fragment depending on which menu item was selected
        switch (menuItem.getItemId()) {
            case R.id.nav_matches:
                fragmentClass = matchesFragment.getClass();
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
            default:
                fragmentClass = MatchesFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
}


