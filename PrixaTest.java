package com.example.prixa.helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.example.prixa.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class PrixaTest{
    private InputStream services;
    private InputStream enviroment;
    private InputStream result;

    @Test
    public void resolveConfig() {
        File file_services = new File("src/main/res/raw/services.yml");
        File file_env = new File("src/main/res/raw/env.txt");
        File file_result = new File("src/main/res/raw/result.json");

        try {
            services = new FileInputStream(file_services);
            enviroment = new FileInputStream(file_env);
            result = new FileInputStream(file_result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String expected = Prixa.readTextFile(result).replace("\r\n","").replace(" ","");
        String result = Prixa.resolveConfig(services,enviroment).replace("\r\n","").replace(" ","");
        assertEquals(result.toString(),expected.toString());
    }

    @Before
    public void setUp() throws Exception {

    }
}