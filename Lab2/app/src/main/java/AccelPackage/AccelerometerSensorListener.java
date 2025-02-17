package AccelPackage;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

public class AccelerometerSensorListener implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final OnAccelerationChangedListener listener;

    public interface OnAccelerationChangedListener {
        void onAccelerationChanged(float absoluteAcceleration);
    }

    public AccelerometerSensorListener(Context context, OnAccelerationChangedListener listener) {
        this.listener = listener;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void register() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float absoluteAcceleration = (float) Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1] - 9.81, 2) + Math.pow(event.values[2], 2));

            if (listener != null) {
                listener.onAccelerationChanged(absoluteAcceleration);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
