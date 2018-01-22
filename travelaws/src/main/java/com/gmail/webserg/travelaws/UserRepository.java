package com.gmail.webserg.travelaws;

import com.gmail.webserg.travelaws.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public User findOne(int id) {
        return new User(id, "serg", "doroshenko", 100001l, "m", "webserg@gmail.com");
    }
}
