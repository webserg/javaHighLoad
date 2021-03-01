package com.gmail.webserg.travelaws;

import com.gmail.webserg.travelaws.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") Integer id) {
<<<<<<< HEAD
        return userRepository.findOne(id);
=======
        userRepository.findAll();
        return userRepository.findById(id);
>>>>>>> f7e6f627c32ec51bf2bb0252255127b03416e3d4
    }
}
