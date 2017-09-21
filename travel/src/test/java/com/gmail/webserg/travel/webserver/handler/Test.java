package com.gmail.webserg.travel.webserver.handler;

import com.gmail.webserg.travel.domain.Visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        System.out.println("c:\\asdasd\\adasd\\users_10.json".replaceAll("[^0-9]",""));
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit(1,1,1,1,1));
        visits.add(new Visit(2,1,1,2,1));
        visits.add(new Visit(3,1,1,3,1));
        visits.add(new Visit(4,1,1,4,1));
        System.out.println(visits);
        visits.remove(new Visit(2,1,1,2,1));
        System.out.println(visits);
        Map<Integer,Visit> map = visits.stream().collect(Collectors.toMap(Visit::getId, Function.identity()));
        System.out.println(map);
        map.remove(3);
        System.out.println(map);
    }
}
