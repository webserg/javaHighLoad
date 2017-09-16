package com.gmail.webserg.travel.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.webserver.handler.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataLoader<T> {
    private final Class<T[]> typeParameterClass;
    private final String nameOfCollection;
    private final int initCapacity;


    public DataLoader(Class<T[]> typeParameterClass, String nameOfCollection, int initCapacity) {
        this.typeParameterClass = typeParameterClass;
        this.nameOfCollection = nameOfCollection;
        this.initCapacity = initCapacity;
    }

    public List<T> load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File[] fileList = new java.io.File(TravelConfig.PATH).listFiles((dir, name) -> name.startsWith(nameOfCollection));
        List<T> allItems = new ArrayList<>(initCapacity);
        allItems.add(null);
        if (fileList != null) {
            Arrays.sort(fileList, Comparator.comparing(f2 -> Integer.valueOf(f2.getName().replaceAll("[^0-9]+", ""))));
            for (File f : fileList) {
                Map<String, T[]> users = mapper.readValue(f, mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, typeParameterClass));
                allItems.addAll(Arrays.asList(users.get(nameOfCollection)));
            }
        }
        return allItems;
    }

}
