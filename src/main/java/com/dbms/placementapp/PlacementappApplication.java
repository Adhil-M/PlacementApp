package com.dbms.placementapp;

import java.time.LocalDate;

import com.dbms.placementapp.models.Student;
import com.dbms.placementapp.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PlacementappApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlacementappApplication.class, args);
		System.out.println("Running...");
	}
}
