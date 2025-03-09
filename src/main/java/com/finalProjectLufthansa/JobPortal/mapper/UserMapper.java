package com.finalProjectLufthansa.JobPortal.mapper;

import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.resource.UserResource;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResource toResource(final User user) {
        return new UserResource(user.getName(),
                user.getLastname(),
                user.getUsername(),
                user.getRole()
        );
    }
}
