package com.finalProjectLufthansa.JobPortal.resource;

import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.enums.ApplicationStatus;

public record JobApplicationResource(String name, String lastname, String jobName, String resumeUrl, String diplomaUrl, ApplicationStatus applicationStatus) {
}
