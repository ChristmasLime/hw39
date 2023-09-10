package ru.hogwarts.school.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFacul(Faculty faculty) {
        logger.info("Run method createFacul ");
        return facultyRepository.save(faculty);
    }

    public Faculty findFacul(Long id) {
        logger.info("Run method findFacul ");
        return facultyRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Faculty editFacul(Faculty faculty) {
        logger.info("Run method editFacul ");
        return facultyRepository.save(faculty);
    }

    public void deleteFacul(Long id) {
        logger.info("Run method deleteFacul ");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFacul() {
        logger.info("Run method getAllFacul ");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getFaculByColor(String color) {
        logger.info("Run method getFaculByColor ");
        return facultyRepository.getFacultiesByColor(color);
    }

    public Collection<Faculty> getFacultyNameOrColor(String searchString) {
        logger.info("Run method getFacultyNameOrColor ");
        return facultyRepository.getFacultyByNameIgnoreCaseOrColorIgnoreCase(searchString, searchString);
    }

    public Faculty getByStudentId(Long id) {
        logger.info("Run method getByStudentId ");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElseThrow();


    }
    public String getLongestName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }

}

