package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.webserver.handler.Utils;

public class TravelConfig {
    public static final String PATH = Utils.dataPath == null ? "/tmp/unzipped" : Utils.dataPath;
}
