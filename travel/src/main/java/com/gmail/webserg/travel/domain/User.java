package com.gmail.webserg.travel.domain;


public final class User {
    private int id;
    private String first_name;
    private String last_name;
    private long birth_date;
    private String gender;
    private String email;
    private transient long userVisitsPosition;
    private transient int userVisitsSize;

    public User() {
    }

    public User(int id, String first_name, String last_name, long birth_date, String gender, String email) {
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

    public void setId(int id) {
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

    public long getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(long birth_date) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (birth_date != user.birth_date) return false;
        if (!first_name.equals(user.first_name)) return false;
        if (!last_name.equals(user.last_name)) return false;
        if (!gender.equals(user.gender)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        int result = id;
        return result;
    }

    public int getUserVisitsSize() {
        return userVisitsSize;
    }

    public void setUserVisitsSize(int userVisitsSize) {
        this.userVisitsSize = userVisitsSize;
    }

    public synchronized void update(User u) {
        if(first_name != null) this.first_name = u.first_name;
        if(last_name != null) this.last_name = u.last_name;
        if(birth_date > 0) this.birth_date = u.birth_date;
        if(gender != null) this.gender = u.gender;
        if(email != null) this.email = u.email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", birth_date=" + birth_date +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", userVisitsPosition=" + userVisitsPosition +
                ", userVisitsSize=" + userVisitsSize +
                '}';
    }

    public boolean notValid() {
        return first_name == null || last_name == null || gender == null || email == null;

    }
}
