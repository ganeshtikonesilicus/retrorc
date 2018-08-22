package com.example.ganeshtikone.retrorc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ganeshtikone.retrorc.feature.get.GetRequestFragment;
import com.example.ganeshtikone.retrorc.feature.multipart.MultiPartFragment;
import com.example.ganeshtikone.retrorc.feature.post.PostRequestFragment;

public class RetrofitRxActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                        navigateToGetRequestFragment();
                    return true;
                case R.id.navigation_dashboard:
                    navigateToPostRequestFragment();
                    return true;
                case R.id.navigation_notifications:
                    navigateToMultipartRequestFragment();
                    return true;
            }
            return false;
        }
    };

    private void navigateToMultipartRequestFragment() {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.containerFragment,new MultiPartFragment(), MultiPartFragment.class.getSimpleName())
            .commit();

    }

    /**
     *
     */
    private void navigateToPostRequestFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment,new PostRequestFragment(), PostRequestFragment.class.getSimpleName())
                .commit();
    }

    /**
     *
     */
    private void navigateToGetRequestFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment,new GetRequestFragment(), GetRequestFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rx);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateToGetRequestFragment();
    }

}
