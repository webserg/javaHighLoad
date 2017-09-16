package com.gmail.webserg.travel.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.handler.UserVisitsResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserVisitsRepo {
    void load(List<User> users, List<Location> locations, List<Visit> visits) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        for (User user : users) {
            if (user != null) {
//                List<UserVisitsResponse> userVisitsResponseList = new ArrayList<>();
//                for (Visit v : visits) {
//                    if (v != null) {
//                        if (v.getUser() == user.getId()) {
//                            String place = locations.get(v.getLocation()).getPlace();
//                            userVisitsResponseList.add(new UserVisitsResponse(
//                                    v.getMark(),
//                                    v.getVisited_at(),
//                                    place));
//                        }

                List<UserVisitsResponse> userVisitsResponseList = visits.stream()
                        .filter(v -> v != null && v.getUser() == user.getId())
                        .map(v -> {
                            if (locations.get(v.getLocation()) == null) {
                                System.out.println(v);
                            }
                            String place = locations.get(v.getLocation()).getPlace();
                            if (place == null) {
                                System.out.println(v);
                            }
                            return new UserVisitsResponse(
                                    v.getMark(),
                                    v.getVisited_at(),
                                    place);
                        }).collect(Collectors.toList());


                if (!userVisitsResponseList.isEmpty())
                    mapper.writeValue(getFile(user.getId()), userVisitsResponseList);
            }
        }
    }


    private File getFile(int userId) {
        return new File(TravelConfig.PATH + "/saved/" + "userVisits" + userId + ".json");
    }

    List<UserVisitsResponse> get(int userId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = getFile(userId);
        if (file.exists()) {
            return mapper.readValue(getFile(userId), mapper.getTypeFactory().constructCollectionLikeType(List.class, UserVisitsResponse.class));
        } else {
            return new ArrayList<>();
        }
    }
}
