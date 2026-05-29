package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentService {
    @Autowired
	private StudentRepository repo;

	public List<Student> getAll() {
		return repo.findAll();
	}

	public void save(Student student) {
		repo.save(student);
	}

	public Student getByControlNumber(Integer controlNumber) {
		return repo.findById(controlNumber).get();
	}

	public void delete(Integer controlNumber) {
		repo.deleteById(controlNumber);
	}
}