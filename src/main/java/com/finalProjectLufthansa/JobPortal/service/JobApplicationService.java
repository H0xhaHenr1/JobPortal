package com.finalProjectLufthansa.JobPortal.service;

import com.finalProjectLufthansa.JobPortal.mapper.JobApplicationMapper;
import com.finalProjectLufthansa.JobPortal.model.entity.JobApplication;
import com.finalProjectLufthansa.JobPortal.model.entity.JobPosting;

import com.finalProjectLufthansa.JobPortal.model.entity.User;
import com.finalProjectLufthansa.JobPortal.model.enums.ApplicationStatus;
import com.finalProjectLufthansa.JobPortal.repository.JobApplicationRepository;
import com.finalProjectLufthansa.JobPortal.repository.JobPostingRepository;
import com.finalProjectLufthansa.JobPortal.repository.UserRepository;
import com.finalProjectLufthansa.JobPortal.resource.JobApplicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final JobApplicationMapper jobApplicationMapper;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, JobPostingRepository jobPostingRepository, UserRepository userRepository, JobApplicationMapper jobApplicationMapper) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.userRepository = userRepository;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    //Method user by EMPLOYER
   public Page<JobApplicationResource> getEmployerJobApplications(int size, int page, Long jobId, Long employer, ApplicationStatus status) {
        Optional<User> user = userRepository.findById(employer);
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);
       List<JobApplicationResource> jobApplicationResourceList = new ArrayList<>();
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }else if (jobPosting.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job doesn't exist");
        } else if (!jobPosting.get().getEmployer().getUsername().equals(user.get().getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This user hasn't posted this job");
        }
        if (status == null) {
             jobApplicationResourceList = jobApplicationRepository.findByJob(jobPosting.get()).stream().map(jobApplicationMapper::toJobApplicationResource).toList();
        } else  {
             jobApplicationResourceList = jobApplicationRepository.findByJobAndStatus(jobPosting.get(), status).stream().map(jobApplicationMapper::toJobApplicationResource).toList();
        }

       return getJobApplicationResources(page, size, jobApplicationResourceList);
   }

    public void updateApplicationStatus(Long applicationId, ApplicationStatus newStatus, Long employerId) {
        Optional<JobApplication> jobApplication = jobApplicationRepository.findById(applicationId);

        if (jobApplication.isEmpty()) {
            throw new RuntimeException("Application not found");
        }

        JobPosting jobPosting = jobApplication.get().getJobPosting();
        if (!jobPosting.getEmployer().getId().equals(employerId)) {
            throw new RuntimeException("You are not authorized to update this application status");
        }

        jobApplication.get().setStatus(newStatus);
        jobApplicationRepository.save(jobApplication.get());
    }


    //Methods used by JOB_SEEKER
    public void applyForJob(String username, Long jobId, MultipartFile resumeFile, MultipartFile diplomaFile) throws IOException {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        } else if (jobPosting.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "JobPosting doesn't exist");
        }else if (jobApplicationRepository.existsByApplicantAndJob(user.get(), jobPosting.get())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already applied to this job");
        }

        String resumeUrl = saveFile(resumeFile);
        String diplomaUrl = saveFile(diplomaFile);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setApplicant(user.get());
        jobApplication.setJobPosting(jobPosting.get());
        jobApplication.setStatus(ApplicationStatus.PENDING);
        jobApplication.setResumeUrl(resumeUrl);
        jobApplication.setDiplomaUrl(diplomaUrl);

        jobApplicationRepository.save(jobApplication);
    }

    private String saveFile(MultipartFile file) throws IOException {
        try {
            String uploadDir = "D:\\JavaProjects\\JobPortal\\src\\main\\java\\com\\finalProjectLufthansa\\JobPortal\\uploads\\";
            String filePath = uploadDir + file.getOriginalFilename();
            File destinationFile = new File(filePath);

            destinationFile.getParentFile().mkdirs();

            file.transferTo(destinationFile);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + file.getOriginalFilename());
        }
    }

    public Page<JobApplicationResource> getUserApplications(String username, int page, int size) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
        List<JobApplicationResource> jobApplicationResourceList = jobApplicationRepository.findByApplicant(user.get()).stream().map(jobApplicationMapper::toJobApplicationResource).toList();
        return getJobApplicationResources(page, size, jobApplicationResourceList);
    }

    private Page<JobApplicationResource> getJobApplicationResources(int page, int size, List<JobApplicationResource> jobApplicationResourceList) {
        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), jobApplicationResourceList.size());
        List<JobApplicationResource> pageContent = jobApplicationResourceList.subList(start, end);
        return new PageImpl<>(pageContent, pageable, jobApplicationResourceList.size());
    }

}

//Pageable pageable = PageRequest.of(page, size);
//
//int start = (int) pageable.getOffset();
//int end = Math.min((start + pageable.getPageSize()), jobApplicationResourceList.size());
//List<JobApplicationResource> pageContent = jobApplicationResourceList.subList(start, end);
//        return new PageImpl<>(pageContent, pageable, jobApplicationResourceList.size());
