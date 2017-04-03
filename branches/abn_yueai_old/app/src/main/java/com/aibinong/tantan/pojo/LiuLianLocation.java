package com.aibinong.tantan.pojo;

public class LiuLianLocation {
    public LiuLianLocation() {
    }

    public LiuLianLocation(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public double latitude = -181;
    public double longitude = -91;

    public boolean isValid() {
        if (latitude < -90 || latitude > 90) {
            return false;
        }
        if (longitude < -180 || longitude > 180) {
            return false;
        }
        if (Math.abs(longitude) <= 0.001 && Math.abs(latitude) <= 0.001) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LiuLianLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}