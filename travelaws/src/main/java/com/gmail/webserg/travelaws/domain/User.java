package com.gmail.webserg.travelaws.domain;


public final class User {
    private Integer id;
    private String first_name;
    private String last_name;
    private Long birth_date;
    private String gender;
    private String email;
    private transient long userVisitsPosition;
    private transient int userVisitsSize;

    public User() {
    }

    public User(Integer id, String first_name, String last_name, Long birth_date, String gender, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Long getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Long birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUserVisitsPosition() {
        return userVisitsPosition;
    }

    public void setUserVisitsPosition(long userVisitsPosition) {
        this.userVisitsPosition = userVisitsPosition;
    }

}
