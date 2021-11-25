package com.dbms.placementapp.services;

import java.util.List;

import com.dbms.placementapp.common.Constants;
import com.dbms.placementapp.models.Recruiter;
import com.dbms.placementapp.models.responses.Acknowledgement;
import com.dbms.placementapp.models.responses.RecruiterResponse;
import com.dbms.placementapp.repositories.RecruiterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RecruiterService {

    @Autowired
    RecruiterRepository recruiterRepo;

    final static Logger logger = LoggerFactory.getLogger(Thread.currentThread().getClass().getName());

    public RecruiterResponse createRecruiter(Recruiter payload) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RecruiterResponse response = RecruiterResponse.builder().build();
        logger.info("Creating Recruiter...");
        try {
            String name = payload.getName().trim();
            String role = payload.getRole().trim();
            Integer ctc = payload.getCtc();
            Recruiter recruiter = Recruiter.builder().build();
            if (ctc <= 0) {
                message = "Recruiter creation failed, Ctc must be greater than 0";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }
            List<Recruiter> recruiterCollection = recruiterRepo.findByNameAndRole(name, role);
            if (recruiterCollection != null && !recruiterCollection.isEmpty()) {
                message = "Recruiter creation failed, Same recruiter with same role already exists";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }
            recruiter.setName(name);
            recruiter.setRole(role);
            recruiter.setCtc(ctc);
            recruiter = recruiterRepo.save(recruiter);
            message = "Recruiter creation Successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            response.setRecruiterDetail(recruiter);
            return response;
        } catch (Exception e) {
            message = "Recruiter creation failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return response;
        }
    }

    public RecruiterResponse updateRecruiter(Recruiter payload, Integer recruiterId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RecruiterResponse response = RecruiterResponse.builder().build();
        logger.info("Updating Recruiter...");
        try {
            List<Recruiter> recruiterCollection = recruiterRepo.findByRecruiterId(recruiterId);
            Recruiter recruiter = recruiterCollection.get(0);
            if (payload.getName() != null && !payload.getName().trim().equals("")) {
                recruiter.setName(payload.getName().trim());
            }
            if (payload.getRole() != null && !payload.getRole().trim().equals("")) {
                recruiter.setRole(payload.getRole().trim());
            }
            recruiterCollection = recruiterRepo.findByNameAndRole(recruiter.getName(), recruiter.getRole());
            if (recruiterCollection != null && !recruiterCollection.isEmpty()) {
                Recruiter recruiter1 = recruiterCollection.get(0);
                if (recruiter.getRecruiterId() != recruiter1.getRecruiterId()) {
                    message = "updating recruiter failed, recruiter with given name and role already exists";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
            }
            if (payload.getCtc() != null) {
                if (payload.getCtc() <= 0) {
                    message = "updating recruiter failed, Ctc must be greater than 0";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                recruiter.setCtc(payload.getCtc());
            }
            recruiter = recruiterRepo.save(recruiter);
            message = "Updating Recruiter Successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            response.setRecruiterDetail(recruiter);
            return response;
        } catch (Exception e) {
            message = "Updating Recruiter failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return response;
        }
    }

}
