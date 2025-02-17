package com.example.lab2;

import static com.example.lab2.R.id.buttonGetSnrs;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import AccelPackage.AccelerometerSensorListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AccelerometerSensorListener sensorListener;
    private TextView textViewAccel;
    private TextView textViewAccelMin;
    private TextView textViewAccelMax;
    private float accelMax = 0;
    private float accelMin = 299792458; //скорость света в вакууме


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sensor = findViewById(buttonGetSnrs);
        sensor.setOnClickListener(this);

        textViewAccel = findViewById(R.id.textView_Accel);
        textViewAccelMin = findViewById(R.id.textView_AccelMin);
        textViewAccelMax = findViewById(R.id.textView_AccelMax);
        sensorListener = new AccelerometerSensorListener(this, absoluteAcceleration -> runOnUiThread(() -> {
            if (accelMin > absoluteAcceleration) {
                accelMin = absoluteAcceleration;
                textViewAccelMin.setText(String.format(getString(R.string.minAbsAcc) + " %.2f", accelMin));
            }
            if (accelMax < absoluteAcceleration) {
                accelMax = absoluteAcceleration;
                textViewAccelMax.setText(String.format(getString(R.string.maxAbsAcc) + " %.2f", accelMax));
            }
            textViewAccel.setText(String.format(getString(R.string.absAcc) + " %.2f", absoluteAcceleration));
        }));
        }



    @Override
    protected void onResume() {
        super.onResume();
        sensorListener.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorListener.unregister();
    }
    public void onClick(View v) {
        Intent intent = new Intent(this, activity.class);
        startActivity(intent);

    }
    public void showDialog(View button2) {
        DialogWindow dialog = new DialogWindow();
        dialog.show(getSupportFragmentManager(), "custom");
    }
}