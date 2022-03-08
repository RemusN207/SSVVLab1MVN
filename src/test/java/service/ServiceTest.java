package service;

import domain.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    Service service;

    @BeforeEach
    void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "testFiles/Studenti.xml";
        String filenameTema = "testFiles/Teme.xml";
        String filenameNota = "testFiles/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @AfterEach
    void tearDown() {
        List<Student> list = StreamSupport.stream(service.getAllStudenti().spliterator(), false).collect(Collectors.toList());
        list.forEach(s -> service.deleteStudent(s.getID()));
    }

    @Test
    void addStudent_Success() {
        assertNull(service.addStudent(new Student("1", "AB", 100, "AB@email.com")));
        assertEquals(1, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_Failure() {
        Student stud = new Student("1", "AB", 100, "AB@email.com");
        service.addStudent(stud);
        assertEquals(stud.getID(), service.addStudent(new Student("1", "AB", 100, "AB@email.com")).getID());
    }
}
