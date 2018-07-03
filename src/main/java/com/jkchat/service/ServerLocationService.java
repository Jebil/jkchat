package com.jkchat.service;

import com.jkchat.models.ServerLocation;

public interface ServerLocationService {
    ServerLocation getLocation(String ipAddress);
}
