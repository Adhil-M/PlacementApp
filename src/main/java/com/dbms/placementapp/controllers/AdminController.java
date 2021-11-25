package com.dbms.placementapp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dbms.placementapp.common.Constants;
import com.dbms.placementapp.models.Recruiter;
import com.dbms.placementapp.models.Student;
import com.dbms.placementapp.models.User;
import com.dbms.placementapp.models.UserCreds;
import com.dbms.placementapp.models.payloads.UpdateStatusPayload;
import com.dbms.placementapp.models.responses.Acknowledgement;
import com.dbms.placementapp.models.responses.RecruiterResponse;
import com.dbms.placementapp.models.responses.StudentDetail;
import com.dbms.placementapp.models.responses.StudentList;
import com.dbms.placementapp.repositories.RecruiterRepository;
import com.dbms.placementapp.repositories.StudentRepository;
import com.dbms.placementapp.repositories.UserCredsRepository;
import com.dbms.placementapp.repositories.UserRepository;
import com.dbms.placementapp.services.AdminService;
import com.dbms.placementapp.services.RecruiterService;
import com.dbms.placementapp.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    StudentService studentService;

    @Autowired
    RecruiterService recruiterService;

    @Autowired
    AdminService adminService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    RecruiterRepository recruiterRepo;

    @Autowired
    UserCredsRepository userCredsRepo;

    @PostMapping(path = "/registerrecruiter")
    public ResponseEntity<RecruiterResponse> RegisterRecruiter(@RequestBody Recruiter payload) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RecruiterResponse response = RecruiterResponse.builder().build();
        if (payload.getName() == null || payload.getName().trim().equals("")) {
            message = "Name cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getRole() == null || payload.getRole().trim().equals("")) {
            message = "Role cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getCtc() == null) {
            message = "Ctc cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        response = recruiterService.createRecruiter(payload);
        if (response.getAcknowledgement().getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RecruiterResponse>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/updaterecruiter")
    public ResponseEntity<RecruiterResponse> RegisterRecruiter(@RequestBody Recruiter payload,
            @RequestParam("recruiterId") Integer recruiterId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RecruiterResponse response = RecruiterResponse.builder().build();
        if (payload.getName() == null && payload.getRole() == null && payload.getCtc() == null) {
            message = "No data sent";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (recruiterId == null) {
            message = "recruiterId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        List<Recruiter> recruiterCollection = recruiterRepo.findByRecruiterId(recruiterId);
        if (recruiterCollection != null && recruiterCollection.isEmpty()) {
            message = "Recruiter with recruiterId " + recruiterId + " not found";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        response = recruiterService.updateRecruiter(payload, recruiterId);
        if (response.getAcknowledgement().getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<RecruiterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RecruiterResponse>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/removerecruiter")
    public ResponseEntity<Acknowledgement> RemoveRecruiter(@RequestParam("recruiterId") Integer recruiterId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        if (recruiterId == null) {
            message = "recruiterId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            return new ResponseEntity<Acknowledgement>(acknowledgement, HttpStatus.BAD_REQUEST);
        }
        List<Recruiter> recruiterCollection = recruiterRepo.findByRecruiterId(recruiterId);
        if (recruiterCollection != null && recruiterCollection.isEmpty()) {
            message = "Recruiter with recruiterId " + recruiterId + " not found";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            return new ResponseEntity<Acknowledgement>(acknowledgement, HttpStatus.BAD_REQUEST);
        }
        Recruiter recruiter = recruiterCollection.get(0);
        try {
            recruiterRepo.delete(recruiter);
            message = "Recruiter with recruiterId " + recruiterId + " removed successfully";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            return new ResponseEntity<Acknowledgement>(acknowledgement, HttpStatus.OK);
        } catch (Exception e) {
            message = "removing recruiter failed , " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            return new ResponseEntity<Acknowledgement>(acknowledgement, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/updatestatus")
    public ResponseEntity<Acknowledgement> upadateStudentStatus(@RequestParam("studentId") Integer studentId,
            @RequestBody UpdateStatusPayload payload) {
        String message = null;
        String status = null;
        Acknowledgement response = Acknowledgement.builder().build();
        if (studentId == null) {
            message = "StudentId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        List<Student> studentCollection = studentRepo.findByStudentId(studentId);
        if (studentCollection != null && studentCollection.isEmpty()) {
            message = "Student Not found!";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        Student student = studentCollection.get(0);
        String newStatus = payload.getStatus();
        if (newStatus == null || newStatus.trim().equals("")) {
            message = "status cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        newStatus.toLowerCase();
        switch (newStatus) {
        case Constants.PLACEMENT_STATUS.REGISTERED:
            response = adminService.registerStudent(payload, studentId);
            break;
        case Constants.PLACEMENT_STATUS.REJECTED:
            response = adminService.rejectStudent(payload, studentId);
            break;
        case Constants.PLACEMENT_STATUS.PLACED:
            response = adminService.placeStudent(payload, studentId);
            break;
        default:
            message = "status vale not valid";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }

        if (response.getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Acknowledgement>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/fetchstudents")
    public ResponseEntity<StudentList> getMethodName(@RequestParam(name = "regNo", required = false) String regNo) {
        try {
            List<Student> list = null;
            StudentList response = StudentList.builder().build();
            if (regNo == null || regNo.trim().equals("")) {
                list = studentRepo.findAll();
            } else {
                list = studentRepo.findByRegNo(regNo);
            }
            List<StudentDetail> list2 = new ArrayList<StudentDetail>();
            StudentDetail details = null;
            Optional<User> userCollection = null;
            User user = null;
            List<UserCreds> userCredsCollection = null;
            UserCreds userCreds = null;
            for (Student student : list) {
                userCollection = userRepo.findById(student.getStudentId());
                userCredsCollection = userCredsRepo.findByUserId(student.getStudentId());
                userCreds = userCredsCollection.get(0);
                user = userCollection.get();
                details = StudentDetail.builder().userId(user.getUserId()).name(user.getName()).email(user.getEmail())
                        .phone(user.getPhone()).regNo(student.getRegNo()).dob(student.getDob()).cgpa(student.getCgpa())
                        .placementStatus(student.getPStatus()).message(student.getMessage())
                        .username(userCreds.getUsername()).build();
                list2.add(details);
            }
            response.setStudentsCount(list.size());
            response.setStudentDetail(list2);
            return new ResponseEntity<StudentList>(response, HttpStatus.OK);
        } catch (Exception e) {
            StudentList response = null;
            return new ResponseEntity<StudentList>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
