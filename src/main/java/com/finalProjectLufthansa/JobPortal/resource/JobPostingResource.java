package com.finalProjectLufthansa.JobPortal.resource;

public record JobPostingResource(
        String title,
        String description,
        String requirements,
        String location,
        Long employerId 
) {
}
