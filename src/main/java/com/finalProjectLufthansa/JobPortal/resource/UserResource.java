package com.finalProjectLufthansa.JobPortal.resource;

import com.finalProjectLufthansa.JobPortal.model.enums.Role;

public record UserResource(String name, String surname, String username, Role role) {
}
