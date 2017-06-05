package com.example.miya_.myapplication;

/**
* Name:AccSensorValue
* Description: get and set accelerometer sensor data
* */
public class AccSensorValue {

    public AccSensorValue() {
    }

    float acc_x;
    float acc_y;
    float acc_z;
    long timeInMs; // ns to ms: add 500*1000 then do timeInNano/1000000

    public float getAcc_x() {
        return acc_x;
    }

    public void setAcc_x(float value) {
        this.acc_x = value;
    }

    public float getAcc_y() {
        return acc_y;
    }

    public void setAcc_y(float value) {
        this.acc_y = value;
    }

    public float getAcc_z() {
        return acc_z;
    }

    public void setAcc_z(float value) {
        this.acc_z = value;
    }

    public long getTimeInMs() {
        return timeInMs;
    }

    public void setTimeInMs(long value) {
        this.timeInMs = value;
    }
}
