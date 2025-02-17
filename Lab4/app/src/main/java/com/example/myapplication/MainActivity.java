package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerLiquid;
    private EditText editTextVolume, editTextWattage;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerLiquid = findViewById(R.id.spinnerLqdLst);
        editTextVolume = findViewById(R.id.vTextInput);
        editTextWattage = findViewById(R.id.pWTextInput);
        Button buttonCalculate = findViewById(R.id.calcBtn);
        textViewResult = findViewById(R.id.textViewRslt);

        // Настройка Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.liquids_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLiquid.setAdapter(adapter);

        buttonCalculate.setOnClickListener(v -> {
            String liquid = spinnerLiquid.getSelectedItem().toString();
            double volume = parseDoubleOrNull(editTextVolume.getText().toString());
            double wattage = parseDoubleOrNull(editTextWattage.getText().toString());
            if (volume == 0 && wattage == 0) {
                runOnUiThread(() -> textViewResult.setText(R.string.Error_Empty));
            }
            else if(volume == 0) {
                runOnUiThread(() -> textViewResult.setText(R.string.Error_Empty_Volume));
            } else if (wattage == 0) {
                textViewResult.setText(R.string.Error_Empty_Wattage);
            }
            else {
                calculateBoilingTime(liquid, volume, wattage);
            }
        });
    }

    private void calculateBoilingTime(String liquid, double volume, double heaterWattage) {
        new Thread(() -> {
            try {
                String query = String.format(Locale.getDefault(), "liquid=%s&volume=%f&heaterWattage=%f",
                        URLEncoder.encode(liquid, "UTF-8"), volume, heaterWattage);
                URL url = new URL(getString(R.string.URL) + query);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                double timeToBoil = jsonResponse.getDouble("timeToBoil");
                double heatCapacity = jsonResponse.getDouble("heatCapacity");
                double boilingTemperature = jsonResponse.getDouble("boilingTemperature");

                String resultMsg = getString(R.string.heatCapacity) + ", " + getString(R.string.Unit_J_K_KG) + ": " +
                        new DecimalFormat("0.00").format(heatCapacity) + "\n" +
                        getString(R.string.boilingTemperature) + ", " + getString(R.string.Unit_0C) + ": " +
                        new DecimalFormat("0.00").format(boilingTemperature) + "\n" +
                        getString(R.string.heatingTime) + ", " + getString(R.string.Unit_S) + ": " +
                        new DecimalFormat("0.00").format(timeToBoil);

                runOnUiThread(() -> textViewResult.setText(resultMsg));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> textViewResult.setText(R.string.Error_Empty_Wattage));
            }
        }).start();
    }

    public double parseDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
