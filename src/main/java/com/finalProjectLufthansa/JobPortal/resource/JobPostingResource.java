package com.finalProjectLufthansa.JobPortal.resource;

public record JobPostingResource(
        String title,
        String description,
        String requirements,
        String location,
        Long employerId // Store only the employer's ID instead of the full User object
) {
}
