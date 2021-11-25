package com.dbms.placementapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.dbms.placementapp.common.Constants;

import com.dbms.placementapp.models.Student;
import com.dbms.placementapp.models.User;
import com.dbms.placementapp.models.UserCreds;
import com.dbms.placementapp.models.payloads.RegisterPayload;
import com.dbms.placementapp.models.responses.Acknowledgement;
import com.dbms.placementapp.models.responses.RegisterResponse;
import com.dbms.placementapp.models.responses.StudentDetail;
import com.dbms.placementapp.models.responses.UserResponse;
import com.dbms.placementapp.repositories.RecruiterRepository;
import com.dbms.placementapp.repositories.StudentRepository;
import com.dbms.placementapp.repositories.UserCredsRepository;
import com.dbms.placementapp.repositories.UserRepository;
import com.dbms.placementapp.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    StudentService studentService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    UserCredsRepository userCredsRepo;

    @Autowired
    RecruiterRepository recruiterRepo;

    @PostMapping(path = "/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterPayload payload) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RegisterResponse response = RegisterResponse.builder().build();
        if (payload.getName() == null || payload.getName().trim().equals("")) {
            message = "Name cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getEmail() == null || payload.getEmail().trim().equals("")) {
            message = "Eamil cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getPhone() == null || payload.getPhone().trim().equals("")) {
            message = "Phone Number cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getUsername() == null || payload.getUsername().trim().equals("")) {
            message = "Username cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getPassword() == null || payload.getPassword().trim().equals("")) {
            message = "Password cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getRetypePassword() == null || payload.getRetypePassword().trim().equals("")) {
            message = "Retyped Password cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        response = studentService.createUser(payload);
        if (response.getAcknowledgement().getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserResponse> getUserById(@RequestParam("userId") Integer userId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        UserResponse response = UserResponse.builder().build();
        try {
            if (userId == null) {
                message = "UserId cannot be empty";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
            }
            Optional<User> userCollection = userRepo.findById(userId);
            if (userCollection != null && !userCollection.isPresent()) {
                message = "User with userId " + userId + " not found";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
            }
            List<UserCreds> userCredsCollection = userCredsRepo.findByUserId(userId);
            if (userCredsCollection != null && userCredsCollection.isEmpty()) {
                message = "User Credentials Could not be found";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
            }
            User user = userCollection.get();
            UserCreds userCreds = userCredsCollection.get(0);
            StudentDetail userDetail = StudentDetail.builder().userId(user.getUserId()).name(user.getName())
                    .email(user.getEmail()).phone(user.getPhone()).username(userCreds.getUsername()).build();
            List<Student> students = studentRepo.findByStudentId(userId);
            if (students != null && !students.isEmpty()) {
                Student student = students.get(0);

                userDetail.setRegNo(student.getRegNo());
                userDetail.setDob(student.getDob());
                userDetail.setCgpa(student.getCgpa());
                userDetail.setPlacementStatus(student.getPStatus());
                userDetail.setMessage(student.getMessage());
            }
            response.setDetails(userDetail);
            message = "Success";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<UserResponse>(response, HttpStatus.OK);

        } catch (Exception e) {
            message = "Failed fetching details";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<UserResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/updateuser")
    public ResponseEntity<RegisterResponse> updateUser(@RequestParam("userId") Integer userId,
            @RequestBody RegisterPayload payload) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RegisterResponse response = RegisterResponse.builder().build();
        if (payload.getEmail() == null && payload.getName() == null && payload.getPhone() == null
                && payload.getUsername() == null && payload.getPassword() == null) {
            message = "No data sent";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (userId == null) {
            message = "UserId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<User> userCollection = userRepo.findById(userId);
        if (userCollection != null && !userCollection.isPresent()) {
            message = "User with userId " + userId + " not found";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        List<UserCreds> userCredsCollection = userCredsRepo.findByUserId(userId);
        if (userCredsCollection != null && userCredsCollection.isEmpty()) {
            message = "User Credentials Could not be found";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }

        response = studentService.updateUser(payload, userId);
        if (response.getAcknowledgement().getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<RegisterResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);

    }

}
