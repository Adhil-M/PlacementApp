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
            student.setMessage("Registered for the placement drive successfully , can appl for role");
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

    public Acknowledgement placeStudent(UpdateStatusPayload payload, Integer studentId) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        try {
            List<Student> studentCollection = studentRepo.findByStudentId(studentId);
            if (payload.getRecruiterName() == null || payload.getRecruiterName().trim().equals("")) {
                message = "Recruiter name cannot be empty";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            if (payload.getRoleName() == null || payload.getRoleName().trim().equals("")) {
                message = "Role name cannot be empty";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            Student student = studentCollection.get(0);
            if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.REGISTERED)) {
                message = "Cannot place the student , status: " + student.getPStatus();
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            List<Recruiter> recruiterCollection = recruiterRepo.findByNameAndRole(payload.getRecruiterName().trim(),
                    payload.getRoleName().trim());
            if (recruiterCollection != null && recruiterCollection.isEmpty()) {
                message = "Recruiter with given role not found";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            Recruiter recruiter = recruiterCollection.get(0);
            List<AppliedFor> list = appliedForRepo.deleteByStudentId(studentId);
            IsPlaced isPlaced = IsPlaced.builder().recruiterId(recruiter.getRecruiterId()).studentId(studentId).build();
            isPlaced = isPlacedRepo.save(isPlaced);
            student.setPStatus(Constants.PLACEMENT_STATUS.PLACED);
            student.setMessage("Congratulations ,You have been Placed at " + payload.getRecruiterName() + " as a "
                    + payload.getRoleName());
            student = studentRepo.save(student);
            message = "Student changed into placed status";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;

        } catch (Exception e) {
            message = "Placing student successfull";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }

    }

}
