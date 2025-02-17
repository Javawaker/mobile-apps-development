package com.example.lab2;

import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.Context;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_list);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        TextView textView = (TextView)findViewById(R.id.textViewSnsr);
        List<String> listSensorType = new ArrayList<>();

        for (int i = 0; i < deviceSensors.size(); i++) {
            listSensorType.add(i + 1 + getString(R.string.ClParanth) + " " + deviceSensors.get(i).getName() + "\n"+
                    getString(R.string.index) + " " + deviceSensors.get(i).getType() + "\n" +
                    getString(R.string.manufacture) + " " + deviceSensors.get(i).getVendor() + "\n" +
                    getString(R.string.ver) + " " + deviceSensors.get(i).getVersion() + "\n" +
                    getString(R.string.power) + " " + deviceSensors.get(i).getPower() + " " + getString(R.string.m) + "\n"+
                    getString(R.string.delay)  + deviceSensors.get(i).getMinDelay() + getString(R.string.comma) + deviceSensors.get(i).getMaxDelay() + getString(R.string.mk) + "\n"+
                    getString(R.string.max) + " " + deviceSensors.get(i).getMaximumRange() +"\n"+
                    getString(R.string.eps) + " " + deviceSensors.get(i).getResolution() + "\n"
            );
        }

        for (int i = 0; i < deviceSensors.size(); i++) {
            textView.append(listSensorType.get(i) + "\n" );
        }

    }
    public void clickButton(View imageView) {
        finish();
    }
}
