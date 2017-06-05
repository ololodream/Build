package com.example.miya_.myapplication;

/**
 * GyrSensorValue
 * Description: same as AccSensorValue, which maintains Gyroscopes data
 */

public class GyrSensorValue {

    public GyrSensorValue() {
    }

    float gyr_x;
    float gyr_y;
    float gyr_z;
    long timeInMs; // ns to ms: add 500*1000 then do timeInNano/1000000

    public float getGyr_x() {
        return gyr_x;
    }

    public void setGyr_x(float value) {
        this.gyr_x = value;
    }

    public float getGyr_y() {
        return gyr_y;
    }

    public void setGyr_y(float value) {
        this.gyr_y = value;
    }

    public float getGyr_z() {
        return gyr_z;
    }

    public void setGyr_z(float value) {
        this.gyr_z = value;
    }

    public long getTimeInMs() {
        return timeInMs;
    }

    public void setTimeInMs(long value) {
        this.timeInMs = value;
    }
}
