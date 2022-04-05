package service;

import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

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
        List<Student> studentList = StreamSupport.stream(service.getAllStudenti().spliterator(), false).collect(Collectors.toList());
        studentList.forEach(student -> service.deleteStudent(student.getID()));
        List<Tema> assignmentList = StreamSupport.stream(service.getAllTeme().spliterator(), false).collect(Collectors.toList());
        assignmentList.forEach(tema -> service.deleteTema(tema.getID()));
    }

    @Test
    void addStudent_tc01() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student(null, "A", 0, "e") ));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc02() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student("", "A", 0, "e")));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc03() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student("1", null, 0, "e")));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc04() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student("1", "", 0, "e")));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc05() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student("1", "A", -1, "e")));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc06() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student("1", "A", 0, null)));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }
    @Test
    void addStudent_tc07() {
        assertThrows(ValidationException.class, () -> service.addStudent(new Student("1", "A", 0, "")));
        assertEquals(0, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc08() {
        assertNull(service.addStudent(new Student("1", "A", 0, "e")));
        assertEquals(1, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addStudent_tc09() {
        Student stud = new Student("1", "A", 0, "e");
        service.addStudent(stud);
        assertEquals(stud.getID(), service.addStudent(new Student("1", "A", 0, "e")).getID());
        assertEquals(1, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addAssignment_tc01() {
        assertThrows(ValidationException.class, () -> service.addTema(new Tema("", "d", 1, 1) ));
        assertEquals(0, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }

    @Test
    void addAssignment_tc02() {
        assertThrows(ValidationException.class, () -> service.addTema(new Tema("i", "", 1, 1) ));
        assertEquals(0, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }

    @Test
    void addAssignment_tc03() {
        assertThrows(ValidationException.class, () -> service.addTema(new Tema("i", "d", 0, 1) ));
        assertEquals(0, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }

    @Test
    void addAssignment_tc04() {
        assertThrows(ValidationException.class, () -> service.addTema(new Tema("i", "d", 1, 0) ));
        assertEquals(0, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }

    @Test
    void addAssignment_tc05() {
        assertNull(service.addTema(new Tema("i", "d", 1, 1)));
        assertEquals(1, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }

    @Test
    void addAssignment_tc06() {
        Tema tema = new Tema("i", "d", 1, 1);
        service.addTema(tema);
        assertEquals(tema.getID(), service.addTema(new Tema("i", "d", 1, 1)).getID());
        assertEquals(1, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }
}
