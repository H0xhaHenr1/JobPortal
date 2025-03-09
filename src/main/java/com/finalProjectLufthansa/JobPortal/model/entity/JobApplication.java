package com.finalProjectLufthansa.JobPortal.model.entity;

import com.finalProjectLufthansa.JobPortal.model.enums.ApplicationStatus;
import jakarta.persistence.*;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String resumeUrl;

    @Column(nullable = false)
    private String diplomaUrl;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, ACCEPTED, REJECTED

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getDiplomaUrl() {
        return diplomaUrl;
    }

    public void setDiplomaUrl(String diplomaUrl) {
        this.diplomaUrl = diplomaUrl;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public JobPosting getJobPosting() {
        return job;
    }

    public void setJobPosting(JobPosting job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "id=" + id +
                ", resumeUrl='" + resumeUrl + '\'' +
                ", diplomaUrl='" + diplomaUrl + '\'' +
                ", status=" + status +
                ", applicant=" + applicant +
                ", job=" + job +
                '}';
    }

//        let's go to the controllers and repositories and service classes. Ill recap again the classes we have. User class with relations: an employer can have many jobPostings(OneToMany), an employer can have many reviews(OneToMany), a JobSeeker can have many applications(OneToMany); JobPosting class with relations: A job can be created by one employer, but an employer can create many job(ManyToOne),a job can have only one review with a OneToOne relationship, and a job can have many applications(OneToMany); JobApplication with relations: many applications may go to a job posting(ManyToOne)and review class.
}


