package com.finalProjectLufthansa.JobPortal.repository;

import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;
import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.resource.JobPostingResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
//    Page<JobPosting> findByEmployerAndTitleContainingAndLocationContaining(User user, String title, String location, Pageable pageable);
//
//    Page<JobPosting> findByEmployerAndTitleContaining(User user, String title, Pageable pageable);

    @Query("SELECT j FROM JobPosting j WHERE "
            + "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) "
            + "AND (:jobTitle IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :jobTitle, '%'))) "
            + "AND (:employerName IS NULL OR LOWER(j.employer.username) LIKE LOWER(CONCAT('%', :employerName, '%')))")
    List<JobPosting> searchJobs(@Param("location") String location, @Param("jobTitle") String jobTitle, @Param("employerName") String employerName);

    @Query("SELECT j FROM JobPosting j WHERE j.employer = :employer "
            + "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) "
            + "AND (:jobTitle IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :jobTitle, '%')))")
    List<JobPosting> findByEmployerAndFilters(@Param("employer") User employer, @Param("location") String location, @Param("jobTitle") String jobTitle);

    Page<JobPosting> findByEmployer(User employer, Pageable pageable);


    JobPosting findByTitle(String title);
}
