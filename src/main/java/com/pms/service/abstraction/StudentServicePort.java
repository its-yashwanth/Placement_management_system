package com.pms.service.abstraction;

import com.pms.dto.StudentProfileForm;
import com.pms.model.Student;
import java.util.List;

public interface StudentServicePort {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student save(Student student);
    Student getByEmail(String email);
    Student updateProfile(Long studentId, StudentProfileForm form);
    void deleteStudent(Long studentId);
}
