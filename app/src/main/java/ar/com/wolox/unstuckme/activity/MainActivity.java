package ar.com.wolox.unstuckme.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import ar.com.wolox.unstuckme.R;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
