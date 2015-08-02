package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.UserFragment;

public class UserActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Fragment f = UserFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_user_container, f)
                .commit();
    }
}
