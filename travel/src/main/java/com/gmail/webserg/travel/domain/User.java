package com.gmail.webserg.travel.domain;


public final class User {
    private int id;
    private String first_name;
    private String last_name;
    private long birth_date;
    private String gender;
    private String email;
    private long userVisitsPosition;
    private int userVisitsSize;

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
        result = 31 * result + first_name.hashCode();
        result = 31 * result + last_name.hashCode();
        result = 31 * result + (int) (birth_date ^ (birth_date >>> 32));
        result = 31 * result + gender.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    public int getUserVisitsSize() {
        return userVisitsSize;
    }

    public void setUserVisitsSize(int userVisitsSize) {
        this.userVisitsSize = userVisitsSize;
    }
}
