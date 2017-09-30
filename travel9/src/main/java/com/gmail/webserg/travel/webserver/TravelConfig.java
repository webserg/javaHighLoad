package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.webserver.handler.Utils;

public class TravelConfig {
    public static final String PATH = Utils.DATA_PATH == null ? "/tmp/unzipped" : Utils.DATA_PATH;
}
