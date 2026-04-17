package com.pms.service;

import com.pms.dto.StudentProfileForm;
import com.pms.model.Student;
import com.pms.repository.ApplicationRepository;
import com.pms.repository.StudentRepository;
import com.pms.service.abstraction.StudentServicePort;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService implements StudentServicePort {

    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;

    public StudentService(StudentRepository studentRepository, ApplicationRepository applicationRepository) {
        this.studentRepository = studentRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getByEmail(String email) {
        return studentRepository.findByUserAccountEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student not found for email: " + email));
    }

    @Override
    public Student updateProfile(Long studentId, StudentProfileForm form) {
        Student student = getStudentById(studentId);
        student.setFullName(form.getFullName());
        student.setBranch(form.getBranch());
        student.setCgpa(form.getCgpa());
        student.setSkills(form.getSkills());
        student.setResumeUrl(form.getResumeUrl());
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudent(Long studentId) {
        applicationRepository.deleteByStudentId(studentId);
        studentRepository.deleteById(studentId);
    }
}
