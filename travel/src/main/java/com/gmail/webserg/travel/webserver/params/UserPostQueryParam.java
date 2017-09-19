package com.gmail.webserg.travel.webserver.params;

public class UserPostQueryParam {
    public final Integer id;
    public final String first_name;
    public final String last_name;
    public final Long birth_date;
    public final String gender;
    public final String email;

    public UserPostQueryParam(Integer id, String first_name, String last_name, Long birth_date, String gender, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.email = email;
    }
}
