package com.dbms.placementapp.services;

import java.util.List;

import com.dbms.placementapp.common.Constants;
import com.dbms.placementapp.models.AppliedFor;
import com.dbms.placementapp.models.IsPlaced;
import com.dbms.placementapp.models.Recruiter;
import com.dbms.placementapp.models.Student;
import com.dbms.placementapp.models.payloads.UpdateStatusPayload;
import com.dbms.placementapp.models.responses.Acknowledgement;
import com.dbms.placementapp.repositories.AppliedForRepository;
import com.dbms.placementapp.repositories.IsPlacedRepository;
import com.dbms.placementapp.repositories.RecruiterRepository;
import com.dbms.placementapp.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {
    @Autowired
    StudentRepository studentRepo;

    @Autowired
    RecruiterRepository recruiterRepo;

    @Autowired
    IsPlacedRepository isPlacedRepo;

    @Autowired
    AppliedForRepository appliedForRepo;

    public Acknowledgement registerStudent(UpdateStatusPayload payload, Integer studentId) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        try {
            List<Student> studentCollection = studentRepo.findByStudentId(studentId);
            Student student = studentCollection.get(0);
            if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.APPLIED)) {
                message = "Student have not applied . status : " + student.getPStatus();
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            student.setPStatus(Constants.PLACEMENT_STATUS.REGISTERED);
            student.setMessage("Registered for the placement drive successfully , can apply for role");
            student = studentRepo.save(student);
            message = "Student registration successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        } catch (Exception e) {
            message = "Student regestration failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }

    }

    @Transactional
    public Acknowledgement rejectStudent(UpdateStatusPayload payload, Integer studentId) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        try {
            List<Student> studentCollection = studentRepo.findByStudentId(studentId);
            Student student = studentCollection.get(0);
            if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.APPLIED)
                    && !student.getPStatus().equals(Constants.PLACEMENT_STATUS.REGISTERED)) {
                message = "Cannot reject the student , status: " + student.getPStatus();
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            List<AppliedFor> list = appliedForRepo.deleteByStudentId(studentId);
            student.setPStatus(Constants.PLACEMENT_STATUS.REJECTED);
            student.setMessage("Rejected by the placement officer due to : " + payload.getMessage());
            student = studentRepo.save(student);
            message = "Student rejected from drive";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;

        } catch (Exception e) {
            message = "Student rejection failed ," + e.getMessage();
            e.printStackTrace();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }

    }

    public Acknowledgement placeStudent(Acknowledgement payload, Integer applicationId) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        try {
            List<AppliedFor> appliedFors = appliedForRepo.findByApplicationId(applicationId);
            AppliedFor appliedFor = appliedFors.get(0);
            Integer studentId = appliedFor.getStudentId();
            List<Student> studentCollection = studentRepo.findByStudentId(studentId);
            Student student = studentCollection.get(0);
            List<Recruiter> recruiters = recruiterRepo.findByRecruiterId(appliedFor.getRecruiterId());
            if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.REGISTERED)) {
                message = "Cannot place the student , status: " + student.getPStatus();
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            List<AppliedFor> list = appliedForRepo.findByStudentId(studentId);
            for (AppliedFor appliedFor1 : list) {
                if (appliedFor1.getApplicationId() != applicationId) {
                    appliedFor1.setStatus(Constants.APPLICATION_STATUS.REJECTED);
                    appliedFor1.setMessage("Placed for another role");
                    appliedFor1 = appliedForRepo.save(appliedFor1);
                }
            }
            IsPlaced isPlaced = IsPlaced.builder().recruiterId(appliedFor.getRecruiterId()).studentId(studentId)
                    .build();
            isPlaced = isPlacedRepo.save(isPlaced);
            student.setPStatus(Constants.PLACEMENT_STATUS.PLACED);
            student.setMessage("Congratulations ,You have been Placed at " + recruiters.get(0).getName() + " as a "
                    + recruiters.get(0).getRole());
            student = studentRepo.save(student);
            appliedFor.setStatus(Constants.APPLICATION_STATUS.PLACED);
            appliedFor.setMessage("Placed");
            appliedFor = appliedForRepo.save(appliedFor);
            message = "Student changed into placed status";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;

        } catch (Exception e) {
            message = "Placing student successfull";
            e.printStackTrace();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }

    }

    public Acknowledgement revertRejection(UpdateStatusPayload payload, Integer studentId) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        try {
            List<Student> studentCollection = studentRepo.findByStudentId(studentId);
            Student student = studentCollection.get(0);
            if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.REJECTED)) {
                message = "Student have not rejected . status : " + student.getPStatus();
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            student.setPStatus(Constants.PLACEMENT_STATUS.NOT_REGISTERED);
            student.setMessage("Please apply for the placement drive to participate in it");
            student = studentRepo.save(student);
            message = "Student rejection revoked";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        } catch (Exception e) {
            message = "Student status chaage failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }
    }

    public Acknowledgement rejectApplication(Acknowledgement payload, Integer applicationId) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        try {
            List<AppliedFor> appliedFors = appliedForRepo.findByApplicationId(applicationId);
            AppliedFor appliedFor = appliedFors.get(0);
            if (!appliedFor.getStatus().equals(Constants.APPLICATION_STATUS.APPLIED)) {
                message = "Application cannot be rejected , status : " + appliedFor.getStatus();
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            appliedFor.setStatus(Constants.APPLICATION_STATUS.REJECTED);
            if (payload.getMessage() != null && !payload.getMessage().trim().equals(""))
                appliedFor.setMessage(payload.getMessage());
            else
                appliedFor.setMessage("No message from Placement Officer");
            appliedFor = appliedForRepo.save(appliedFor);
            message = "Application rejected";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        } catch (Exception e) {
            message = "Application cannot be rejected , " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }
    }

}
