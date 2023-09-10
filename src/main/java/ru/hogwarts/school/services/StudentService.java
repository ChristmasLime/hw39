package ru.hogwarts.school.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;


    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStud(Student student) {
        logger.info("Run method createStud ");
        return studentRepository.save(student);
    }

    public Student findStud(Long id) {
        logger.info("Run method findStud ");
        return studentRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Student editStud(Student student) {
        logger.info("Run method editStud ");
        return studentRepository.save(student);
    }

    public void deleteStud(Long id) {
        logger.info("Run method deleteStud ");
        studentRepository.deleteById(id);

    }

    public Collection<Student> getAllStud() {
        logger.info("Run method getAllStud ");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudByAge(int age) {
        logger.info("Run method getStudByAge ");
        return studentRepository.getStudByAge(age);
    }

    public Collection<Student> getStudentsAgeBetween(int minAge, int maxAge) {
        logger.info("Run method getStudentsAgeBetween ");
        return studentRepository.findStudByAgeBetween(minAge, maxAge);
    }


    public Collection<Student> getByFacultyId(Long id) {
        logger.info("Run method getByFacultyId ");
        return facultyRepository.findById(id)
                .map(Faculty::getStudents)
                .orElseThrow(NoSuchElementException::new);
    }

    public Long getCountOfStudents() {
        logger.info("Run method getCountOfStudents ");
        return studentRepository.getCountOfStudents();
    }

    public Double getAverageAgeOfStudents() {
        logger.info("Run method getAverageAgeOfStudents ");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Run method findLastFiveStudents ");
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Student> studentsPage = studentRepository.findLastFiveStudents(pageRequest);
        return studentsPage.getContent();
    }

    public List<String> getNames(char firstSymbol) {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> Character.toLowerCase(name.charAt(0))
                        == Character.toLowerCase(firstSymbol))
                .collect(Collectors.toList());
    }

    public double getAveregeAge() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElseThrow();
    }

    public void printStudentNamesWithoutExecutor() {
        List<Student> students = studentRepository.findAll();
        if (students.size() < 6) {
            System.out.println("Not enough students to print names.");
            return;
        }
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 2; i++) {
                System.out.println("Thread 1: " + students.get(i).getName());
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 2; i < 4; i++) {
                System.out.println("Thread 2: " + students.get(i).getName());
            }
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();

        } catch (Exception e) {
            logger.error("An error occurred", e);
        }

    }

    public void print () {
        List<Student> all = studentRepository.findAll();
        System.out.println("Выведены в основном потоке :");
        System.out.println(all.get(0).getName());
        System.out.println(all.get(1).getName());

        new Thread(()->{
            System.out.println("Выведены в первом  параллельном потоке :");
            System.out.println(all.get(2).getName());
            System.out.println(all.get(3).getName());
        }).start();

        new Thread(()->{
            System.out.println("Выведены во втором  параллельном потоке :");
            System.out.println(all.get(4).getName());
            System.out.println(all.get(5).getName());
        }).start();
    }


}


