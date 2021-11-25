package com.dbms.placementapp.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbms.placementapp.common.Constants;
import com.dbms.placementapp.models.AppliedFor;
import com.dbms.placementapp.models.IsPlaced;
import com.dbms.placementapp.models.Recruiter;
import com.dbms.placementapp.models.Student;
import com.dbms.placementapp.models.User;
import com.dbms.placementapp.models.UserCreds;
import com.dbms.placementapp.models.payloads.AcadDetailPayload;
import com.dbms.placementapp.models.payloads.ApplyPayload;
import com.dbms.placementapp.models.payloads.RegisterPayload;
import com.dbms.placementapp.models.responses.Acknowledgement;
import com.dbms.placementapp.models.responses.RegisterResponse;
import com.dbms.placementapp.models.responses.StudentDetail;
import com.dbms.placementapp.models.responses.StudentDetailResponse;
import com.dbms.placementapp.models.responses.UserDetail;
import com.dbms.placementapp.repositories.AppliedForRepository;
import com.dbms.placementapp.repositories.IsPlacedRepository;
import com.dbms.placementapp.repositories.RecruiterRepository;
import com.dbms.placementapp.repositories.StudentRepository;
import com.dbms.placementapp.repositories.UserCredsRepository;
import com.dbms.placementapp.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserCredsRepository userCredsRepo;

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    RecruiterRepository recruiterRepo;

    @Autowired
    IsPlacedRepository isPlacedRepo;

    @Autowired
    AppliedForRepository appliedForRepo;

    final static Logger logger = LoggerFactory.getLogger(Thread.currentThread().getClass().getName());

    public RegisterResponse createUser(RegisterPayload payload) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RegisterResponse response = RegisterResponse.builder().build();
        logger.info("Creating User...");
        try {
            String name = payload.getName().trim();
            String email = payload.getEmail().trim();
            String phone = payload.getPhone().trim();
            String username = payload.getUsername().trim();
            String password = payload.getPassword().trim();
            String retypePassword = payload.getRetypePassword().trim();

            // email validation
            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                message = "User creation failed, Email is not a valid one";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }
            List<User> collection = userRepo.findByEmail(email);
            if (collection != null && !collection.isEmpty()) {
                message = "User creation failed, Email already taken";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }

            // username validation
            regex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                message = "User creation failed, Username is not a valid one";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }
            List<UserCreds> collection1 = userCredsRepo.findByUsername(username);
            if (collection1 != null && !collection1.isEmpty()) {
                message = "User creation failed, Username already taken";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }
            // password validation
            if (password.equals(retypePassword.trim())) {
                if (password.trim().length() < 5) {
                    message = "User creation failed, Password must contain atleast 5 charecters";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
            } else {
                message = "User creation failed, Password mismatch";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }

            regex = "^[1-9][0-9]{9}$";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                message = "User creation failed, Phone Number is not a valid one";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }

            // creating user
            User user = User.builder().name(name).email(email).phone(phone).build();
            user = userRepo.save(user);
            UserCreds creds = UserCreds.builder().username(username).password(password).userId(user.getUserId())
                    .build();
            creds = userCredsRepo.save(creds);
            UserDetail userDetail = UserDetail.builder().name(name).userId(user.getUserId()).email(email).phone(phone)
                    .username(username).build();
            message = "User creation Successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            response.setUserDetail(userDetail);
            return response;
        } catch (Exception e) {
            message = "User creation failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return response;
        }

    }

    public StudentDetailResponse createStudent(AcadDetailPayload payload, Integer userId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        StudentDetailResponse response = StudentDetailResponse.builder().build();
        logger.info("Creating Student...");
        try {
            String regNo = payload.getRegNo().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(payload.getDob().trim(), formatter);
            Double cgpa = payload.getCgpa();
            List<Student> collection = studentRepo.findByRegNo(regNo);
            if (collection != null && !collection.isEmpty()) {
                message = "Adding Academic details failed,given Register Number already registered";
                status = Constants.RESPONSE_STATUS.FAILED;
                acknowledgement.setMessage(message);
                acknowledgement.setStatus(status);
                response.setAcknowledgement(acknowledgement);
                return response;
            }
            Student student = Student.builder().studentId(userId).regNo(regNo).dob(dob)
                    .pStatus(Constants.PLACEMENT_STATUS.NOT_REGISTERED)
                    .message("Please apply for the placement drive to participate in it").cgpa(cgpa).build();
            student = studentRepo.save(student);
            message = "Adding Academic details Successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            response.setStudentDetail(student);
            return response;
        } catch (Exception e) {
            message = "Adding Academic details failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return response;
        }
    }

    public RegisterResponse updateUser(RegisterPayload payload, Integer userId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        RegisterResponse response = RegisterResponse.builder().build();
        logger.info("Updating User...");
        try {

            Optional<User> userDetail = userRepo.findById(userId);
            List<UserCreds> userCredsCollection = userCredsRepo.findByUserId(userId);

            UserCreds userCreds = userCredsCollection.get(0);
            User user = userDetail.get();
            if (payload.getName() != null && !payload.getName().trim().equals("")) {
                user.setName(payload.getName().trim());
            }
            if (payload.getEmail() != null && !payload.getEmail().trim().equals("")
                    && !payload.getEmail().trim().equals(user.getEmail())) {
                String email = payload.getEmail().trim();
                List<User> userCollection = userRepo.findByEmail(email);
                if (userCollection != null && !userCollection.isEmpty()) {
                    message = "Email alreay registered!!";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);
                if (!matcher.matches()) {
                    message = "Updating User failed, Email is not a valid one";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                user.setEmail(email);
            }
            if (payload.getPhone() != null && !payload.getPhone().trim().equals("")) {
                String regex = "^[1-9][0-9]{9}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(payload.getPhone().trim());
                if (!matcher.matches()) {
                    message = "Updating User failed, Phone number is not a valid one";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                user.setPhone(payload.getPhone().trim());
            }
            user = userRepo.save(user);
            if (payload.getUsername() != null && !payload.getUsername().trim().equals("")
                    && !payload.getUsername().trim().equals(userCreds.getUsername())) {
                String username = payload.getUsername().trim();
                List<UserCreds> userCredsCollection1 = userCredsRepo.findByUsername(username);
                if (userCredsCollection1 != null && !userCredsCollection1.isEmpty()) {
                    message = "Username already registered!!";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                String regex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(username);
                if (!matcher.matches()) {
                    message = "Updating User failed, Username is not a valid one";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                userCreds.setUsername(username);
                userCreds = userCredsRepo.save(userCreds);
            }
            if (payload.getPassword() != null && !payload.getPassword().trim().equals("")
                    && !payload.getPassword().trim().equals(userCreds.getPassword())) {
                if (!payload.getPassword().trim().equals(payload.getRetypePassword().trim())) {
                    message = "Password mismatch";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                if (payload.getPassword().trim().length() < 5) {
                    message = "Updating user failed, Password must contain atleast 5 charecters";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                userCreds.setPassword(payload.getPassword().trim());
                userCreds = userCredsRepo.save(userCreds);
            }
            UserDetail uDetail = UserDetail.builder().name(user.getName()).email(user.getEmail()).phone(user.getPhone())
                    .userId(userId).username(userCreds.getUsername()).build();
            message = "Updating user details successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            response.setUserDetail(uDetail);
            return response;
        } catch (Exception e) {
            message = "Updating user details failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return response;
        }

    }

    public StudentDetailResponse updateStudent(AcadDetailPayload payload, Integer userId) {
        String message = null;
        String status = null;
        Acknowledgement acknowledgement = Acknowledgement.builder().build();
        StudentDetailResponse response = StudentDetailResponse.builder().build();
        logger.info("Updating StudentDetails...");
        try {
            Optional<User> userDetail = userRepo.findById(userId);
            User user = userDetail.get();

            List<Student> studentCollection = studentRepo.findByStudentId(userId);
            Student student = studentCollection.get(0);

            if (payload.getRegNo() != null && !payload.getRegNo().trim().equals("")
                    && !payload.getRegNo().trim().equals(student.getRegNo())) {
                String regNo = payload.getRegNo().trim();
                studentCollection = studentRepo.findByRegNo(regNo);
                if (studentCollection != null && !studentCollection.isEmpty()) {
                    message = "Register number already registered!!";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                if (regNo.length() != 9) {
                    message = "Register Number Invalid";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                student.setRegNo(regNo);

            }
            if (payload.getDob() != null && !payload.getDob().trim().equals("")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = null;
                try {
                    localDate = LocalDate.parse(payload.getDob().trim(), formatter);
                } catch (Exception e) {
                    message = "Invalid Date format";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                student.setDob(localDate);
            }
            if (payload.getCgpa() != null) {
                if (payload.getCgpa() > 10.0 || payload.getCgpa() < 0.0) {
                    message = "Invalid Cgpa Value, must be between 0 and 10";
                    status = Constants.RESPONSE_STATUS.FAILED;
                    acknowledgement.setMessage(message);
                    acknowledgement.setStatus(status);
                    response.setAcknowledgement(acknowledgement);
                    return response;
                }
                student.setCgpa(payload.getCgpa());
            }
            student = studentRepo.save(student);
            message = "Updating user details successfull";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            response.setStudentDetail(student);
            return response;
        } catch (Exception e) {
            message = "Updating academic details failed, " + e.getMessage();
            status = Constants.RESPONSE_STATUS.FAILED;
            acknowledgement.setMessage(message);
            acknowledgement.setStatus(status);
            response.setAcknowledgement(acknowledgement);
            return response;
        }
    }

    public Acknowledgement applyForRecruiter(ApplyPayload payload) {
        String message = null;
        String status = null;
        Acknowledgement response = Acknowledgement.builder().build();
        logger.info("Applying for recruiter...");
        try {
            Integer recruiterId = payload.getRecruiterId();
            Integer studentId = payload.getStudentId();
            List<Student> studentCollection = studentRepo.findByStudentId(studentId);
            if (studentCollection != null && studentCollection.isEmpty()) {
                message = "Student Not found!";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            List<IsPlaced> isPlacedCollection = isPlacedRepo.findByStudentId(studentId);
            if (isPlacedCollection != null && !isPlacedCollection.isEmpty()) {
                message = "Student already placed!";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            Student student = studentCollection.get(0);
            if (!student.getPStatus().equals(Constants.PLACEMENT_STATUS.REGISTERED)) {
                message = "Student not eligible for applying";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            List<Recruiter> recruiterCollection = recruiterRepo.findByRecruiterId(recruiterId);
            if (recruiterCollection != null && recruiterCollection.isEmpty()) {
                message = "Recruiter Not found!";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            List<AppliedFor> appliedForCollection = appliedForRepo.findByRecruiterIdAndStudentId(recruiterId,
                    studentId);
            if (appliedForCollection != null && !appliedForCollection.isEmpty()) {
                message = "Student have already applied for this role";
                status = Constants.RESPONSE_STATUS.FAILED;
                response.setMessage(message);
                response.setStatus(status);
                return response;
            }
            AppliedFor appliedFor = AppliedFor.builder().recruiterId(recruiterId).studentId(studentId)
                    .appliedDate(LocalDate.now()).status(Constants.APPLICATION_STATUS.APPLIED)
                    .message("Application Successfull").build();
            appliedFor = appliedForRepo.save(appliedFor);
            message = "Applied Successfully";
            status = Constants.RESPONSE_STATUS.SUCCESS;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        } catch (Exception e) {
            message = "Applying failed, " + e.getMessage();
            e.printStackTrace();
            status = Constants.RESPONSE_STATUS.FAILED;
            response.setMessage(message);
            response.setStatus(status);
            return response;
        }

    }
}
