package com.dbms.placementapp.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.dbms.placementapp.common.Constants;
import com.dbms.placementapp.models.Recruiter;
import com.dbms.placementapp.models.Student;
import com.dbms.placementapp.models.User;
import com.dbms.placementapp.models.UserCreds;
import com.dbms.placementapp.models.payloads.AcadDetailPayload;
import com.dbms.placementapp.models.payloads.ApplyPayload;
import com.dbms.placementapp.models.payloads.RegisterPayload;
import com.dbms.placementapp.models.responses.Acknowledgement;
import com.dbms.placementapp.models.responses.RecruiterList;
import com.dbms.placementapp.models.responses.RegisterResponse;
import com.dbms.placementapp.models.responses.StudentDetail;
import com.dbms.placementapp.models.responses.StudentDetailResponse;
import com.dbms.placementapp.models.responses.UserDetail;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/student")
public class StudentController {

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

    @PostMapping(path = "/academicdetails")
    public ResponseEntity<StudentDetailResponse> addAcadDetails(@RequestBody AcadDetailPayload payload,
            @RequestParam("userId") Integer userId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        StudentDetailResponse response = StudentDetailResponse.builder().build();
        if (userId == null) {
            message = "UserId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<User> userCollection = userRepo.findById(userId);
        if (userCollection != null && !userCollection.isPresent()) {
            message = "User with userId " + userId + " not found";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        List<Student> collection = studentRepo.findByStudentId(userId);
        if (collection != null && !collection.isEmpty()) {
            message = "Already added Academic Details for user,please update Academic details is needed.";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }

        if (payload.getRegNo() == null || "".equals(payload.getRegNo().trim())) {
            message = "Register Number cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getRegNo().trim().length() != 9) {
            message = "Register Number Invalid";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getDob() == null || "".equals(payload.getDob().trim())) {
            message = "Date of Birth cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate localDate = LocalDate.parse(payload.getDob().trim(), formatter);
        } catch (Exception e) {
            message = "Invalid Date format";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getCgpa() == null) {
            message = "Cgpa cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getCgpa() > 10.0 || payload.getCgpa() < 0.0) {
            message = "Invalid Cgpa Value, must be between 0 and 10";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        response = studentService.createStudent(payload, userId);
        if (response.getAcknowledgement().getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.OK);

    }

    @PutMapping(path = "/academicdetails")
    public ResponseEntity<StudentDetailResponse> updateAcademicDetails(@RequestParam("userId") Integer userId,
            @RequestBody AcadDetailPayload payload) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        StudentDetailResponse response = StudentDetailResponse.builder().build();
        if (payload.getRegNo() == null && payload.getDob() == null && payload.getCgpa() == null) {
            message = "No data sent";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        if (userId == null) {
            message = "UserId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<User> userCollection = userRepo.findById(userId);
        if (userCollection != null && !userCollection.isPresent()) {
            message = "User with userId " + userId + " not found";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        List<Student> collection = studentRepo.findByStudentId(userId);
        if (collection != null && collection.isEmpty()) {
            message = "Academic Details Not Added,please add it before updating";
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        response = studentService.updateStudent(payload, userId);
        if (response.getAcknowledgement().getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<StudentDetailResponse>(response, HttpStatus.OK);

    }

    @GetMapping(path = "/fetchrecruiter")
    public ResponseEntity<RecruiterList> fetchRecruiter(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "role", required = false, defaultValue = "") String role) {
        try {
            List<Recruiter> list = null;
            RecruiterList response = RecruiterList.builder().build();
            if ((name == null || name.equals("")) && (role == null || role.equals(""))) {
                list = recruiterRepo.findAll();
            } else if (role == null || role.equals("")) {
                list = recruiterRepo.findByName(name);
            } else if (name == null || name.equals("")) {
                list = recruiterRepo.findByRole(role);
            } else {
                list = recruiterRepo.findByNameAndRole(name, role);
            }
            response.setRecruiterCount(list.size());
            response.setRecruiterDetails(list);
            return new ResponseEntity<RecruiterList>(response, HttpStatus.OK);
        } catch (Exception e) {
            RecruiterList response = null;
            return new ResponseEntity<RecruiterList>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/apply")
    public ResponseEntity<Acknowledgement> Apply(@RequestBody ApplyPayload payload) {
        String status = null;
        String message = null;
        Acknowledgement response = Acknowledgement.builder().build();
        if (payload.getRecruiterId() == null) {
            message = "RecruiterId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        if (payload.getStudentId() == null) {
            message = "StudentId cannot be empty";
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        response = studentService.applyForRecruiter(payload);
        if (response.getStatus().equals(Constants.RESPONSE_STATUS.FAILED)) {
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Acknowledgement>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/register")
    public ResponseEntity<Acknowledgement> registerForPLacement(@RequestParam("studentId") Integer studentId) {
        String status = null;
        String message = null;
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
        if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.NOT_REGISTERED)) {
            message = "Student Not eligible to register for the drive :" + student.getPStatus();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
        student.setPStatus(Constants.PLACEMENT_STATUS.APPLIED);
        student.setMessage("Applied for the placement drive");
        try {
            student = studentRepo.save(student);
            message = "Applied for drive successfully";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.OK);
        } catch (Exception e) {
            message = "Could not apply for drive " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return new ResponseEntity<Acknowledgement>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
