package com.johan.jnet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.johan.jnet.http.Call;
import com.johan.jnet.http.Callback;
import com.johan.jnet.http.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestService testService = JNet.create(TestService.class);
        Call<String> call = testService.hiBaidu();
        call.call(getClass().getName(), new Callback<String>() {
            @Override
            public void onResponse(final Response<String> respond) {
                System.out.print("body ----> " + respond.getBody());
                Toast.makeText(MainActivity.this, "length ----> " + respond.getBody().length(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(String reason) {
                System.out.print("reason ----> " + reason);
            }
        });
        JNet.cancel(getClass().getName());
    }

}
