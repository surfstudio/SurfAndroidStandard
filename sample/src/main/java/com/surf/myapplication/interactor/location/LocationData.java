package com.surf.myapplication.interactor.location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationData {
    private String provider;
    private double lon;
    private double lat;
    private Long time;

    public static LocationData empty() {
        return new LocationData("Unknown", 0, 0, 0L);
    }
}
