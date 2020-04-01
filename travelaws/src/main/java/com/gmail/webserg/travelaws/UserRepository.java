package com.gmail.webserg.travelaws;

import com.gmail.webserg.travelaws.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
    User findById(Integer id);
}
