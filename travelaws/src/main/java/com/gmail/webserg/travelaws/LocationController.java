package com.gmail.webserg.travelaws;

import com.gmail.webserg.travelaws.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Location getLocation(@PathVariable("id") Integer id) {
        return locationRepository.findOne(id);
    }
}
