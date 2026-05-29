package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("students")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class StudentController {

    @Autowired
	private StudentService service;

	@GetMapping
	public List<Student> getAll() {
		return service.getAll();
	}

	@GetMapping("{controlNumber}")
	public ResponseEntity<Student> getByControlNumber(@PathVariable Integer controlNumber) {
		Student student = service.getByControlNumber(controlNumber);
		return new ResponseEntity<Student>(student, HttpStatus.OK);
	}

	@PostMapping
	public void registrar(@RequestBody Student student) {
		service.save(student);
	}

	@PutMapping("{controlNumber}")
	public ResponseEntity<?> update(@RequestBody Student student, @PathVariable Integer controlNumber) {
		try {
			Student auxStudent = service.getByControlNumber(controlNumber);
			student.setControlNumber(auxStudent.getControlNumber());
			service.save(student);
			return new ResponseEntity<String>("Updated record", HttpStatus.OK);
		}catch (NoSuchElementException e) {
	        return new ResponseEntity<String>("The record with the control number provided is not found in the database", HttpStatus.NOT_FOUND);
	    }
	}

	@DeleteMapping("{controlNumber}")
	public void delete(@PathVariable Integer controlNumber) {
		service.delete(controlNumber);
	}

}
