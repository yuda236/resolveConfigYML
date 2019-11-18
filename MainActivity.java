package com.example.prixa;

import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.prixa.helper.Prixa;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream yml = getResources().openRawResource(R.raw.services);
        InputStream env = getResources().openRawResource(R.raw.env);
        String rsl = Prixa.resolveConfig(getResources().openRawResource(R.raw.services),getResources().openRawResource(R.raw.env));

        TextView yml1 = findViewById(R.id.yml1);
        yml1.setText(Prixa.readTextFile(yml));
        TextView env1 = findViewById(R.id.env1);
        env1.setText(Prixa.readTextFile(env));
        TextView rsl1 = findViewById(R.id.result1);
        rsl1.setText(rsl);


    }


}
