package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

class Liquid {
    double heatCapacity; //Дж/(кг*K)
    double boilingTemperature;
    double density = 1;

    Liquid(double heatCapacity, double boilingTemperature) {
        this.heatCapacity = heatCapacity;
        this.boilingTemperature = boilingTemperature;
    }

    Liquid(double heatCapacity, double boilingTemperature, double density) {
        this.heatCapacity = heatCapacity;
        this.boilingTemperature = boilingTemperature;
        this.density = density;
    }

    private static double calculateTimeToBoilingPointTemplate(Liquid liquid, double volume, double startTemp, double endTemp, double heaterWattage) {
        return ((liquid.heatCapacity * volume * liquid.density * (endTemp - startTemp)) / (heaterWattage));
    }

    static double calculateTimeToBoilingPoint(Liquid liquid, double volume, double startTemp, double heaterWattage) {
        return calculateTimeToBoilingPointTemplate(liquid, volume, startTemp, liquid.boilingTemperature, heaterWattage);
    }

    static double calculateTimeToBoilingPoint(Liquid liquid, double volume, double startTemp, double endTemp, double heaterWattage) {
        return calculateTimeToBoilingPointTemplate(liquid, volume, startTemp, endTemp, heaterWattage);
    }

    static double calculateTimeToBoilingPoint(Liquid liquid, double volume, double heaterWattage) {
        double startTemp = 24;
        return calculateTimeToBoilingPointTemplate(liquid, volume, startTemp, liquid.boilingTemperature, heaterWattage);
    }

    double calculateTimeToBoilingPoint(double volume, double startTemp, double heaterWattage) {
        return calculateTimeToBoilingPointTemplate(this, volume, startTemp, boilingTemperature, heaterWattage);
    }


}



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // The TextView to show your Text
        TextView dialogTextView = new TextView(this);
        dialogTextView.setLineSpacing(55, 0);
        dialogTextView.setPadding(70, 20, 70, 40);
        dialogTextView.setTextIsSelectable(true);
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        // Build the Dialog
        AlertDialog dialog = builderDialog
                .setView(dialogTextView)
                .setCancelable(true)
                .create();

        AlertDialog dialog3 = new AlertDialog.Builder(this).create();
        dialog3.setTitle(R.string.Error_Title);


        //создаем пять различных жидкостей
        Liquid water = new Liquid(4186, 100);
        Liquid ethyl = new Liquid(2470, 78.37, 0.789);
        Liquid oil = new Liquid(2100, 140, 0.850);
        Liquid glycerol = new Liquid(2430, 290, 1.252);
        Liquid benzene = new Liquid(1050, 80.1, 0.876);

        final Liquid[] liquid = new Liquid[1];
        final double[] volume = {0};
//        double startTemp = 24;//C
        final double[] heaterWattage = new double[1];

        //добавляем надписи для спинера
        String[] arraySpinner = new String[]{
                getString(R.string.water),
                getString(R.string.ethyl),
                getString(R.string.oil),
                getString(R.string.glycerol),
                getString(R.string.benzene)
        };

        Spinner s = (Spinner) findViewById(R.id.spinnerLqdLst);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, arraySpinner);
        adapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        s.setAdapter(adapter);


        //button and calculation
        Button calculateButton = (Button) findViewById(R.id.calcBtn);
        calculateButton.setOnClickListener(v -> {
            volume[0] = parseDoubleOrNull(((EditText) findViewById(R.id.vTextInput)).getText().toString());
            heaterWattage[0] = parseDoubleOrNull(((EditText) findViewById(R.id.pWTextInput)).getText().toString());
            dialog.setTitle(R.string.Error_Title);
            if (volume[0] <= 0 && heaterWattage[0] <= 0) {
                dialogTextView.setText(R.string.Error_Empty);
                dialog.show();
                return;
            } else if (volume[0] <= 0) {
                dialogTextView.setText(R.string.Error_Empty_Volume);
                dialog.show();
                return;
            } else if (heaterWattage[0] <= 0) {
                dialogTextView.setText(R.string.Error_Empty_Wattage);
                dialog.show();
                return;
            }


            if (s.getSelectedItem().toString().equals(getString(R.string.water)))
                liquid[0] = water;
             else if (s.getSelectedItem().toString().equals(getString(R.string.ethyl)))
                liquid[0] = ethyl;
             else if (s.getSelectedItem().toString().equals(getString(R.string.oil)))
                liquid[0] = oil;
             else if (s.getSelectedItem().toString().equals(getString(R.string.glycerol)))
                liquid[0] = glycerol;
             else if (s.getSelectedItem().toString().equals(getString(R.string.benzene)))
                liquid[0] = benzene;


            double timeToHeat = Liquid.calculateTimeToBoilingPoint(liquid[0], volume[0], 24, heaterWattage[0]);
            String resultMsg = getString(R.string.heatCapacity) + ", " + getString(R.string.Unit_J_K_KG) + ": " +
                    new DecimalFormat("0.00").format(liquid[0].heatCapacity) + "\n" +
                    getString(R.string.boilingTemperature) + ", " + getString(R.string.Unit_0C) + ": " +
                    new DecimalFormat("0.00").format(liquid[0].boilingTemperature) + "\n" +
                    getString(R.string.heatingTime) + ", " + getString(R.string.Unit_S) + ": " +
                    new DecimalFormat("0.00").format(timeToHeat);

            dialogTextView.setText(resultMsg);
            dialog.setTitle(R.string.result);
            dialog.show();
        });
    }

    public double parseDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}