package com.gmail.webserg.travelaws;

import com.gmail.webserg.travelaws.domain.User;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public User findOne(int id) {
        return new User(id, "serg", "doroshenko", 100001l, "m", "webserg@gmail.com");
    }
=======
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
    User findById(Integer id);
>>>>>>> f7e6f627c32ec51bf2bb0252255127b03416e3d4
}
