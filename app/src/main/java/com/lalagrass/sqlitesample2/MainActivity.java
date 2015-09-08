package com.lalagrass.sqlitesample2;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MsgOverviewFragment.iCallback {

    private MsgOverviewFragment mainFragment;
    public static final String TAG = "sqlitesample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mainFragment == null) {
            mainFragment = new MsgOverviewFragment();
        }
        mainFragment.setCallback(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, mainFragment).commit();
    }

    @Override
    public void onNameSelected(String name) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MsgDetailFragment fragment = MsgDetailFragment.getFragment(name);
        ft.replace(R.id.fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
