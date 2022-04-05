package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceIntegrationTest {
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
        List<Nota> gradeList = StreamSupport.stream(service.getAllNote().spliterator(), false).collect(Collectors.toList());
        gradeList.forEach(grade -> service.deleteNota(grade.getID()));
    }

    @Test
    void addStudent_unit() {
        assertNull(service.addStudent(new Student("1", "A", 0, "e")));
        assertEquals(1, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
    }

    @Test
    void addAssignment_unit() {
        assertNull(service.addTema(new Tema("i", "d", 1, 1)));
        assertEquals(1, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
    }

    @Test
    void addGrade_unit() {
        service.addStudent(new Student("1", "A", 0, "e"));
        service.addTema(new Tema("i", "d", 1, 1));
        assertEquals(5, service.addNota(new Nota("1", "1", "i", 5, LocalDate.of(2021, 10, 2)), "good"));
        assertEquals(1, (int) StreamSupport.stream(service.getAllNote().spliterator(), false).count());
    }

    @Test
    void addGrade_bigBang() {
        assertNull(service.addStudent(new Student("1", "A", 0, "e")));
        assertNull(service.addTema(new Tema("i", "d", 1, 1)));
        assertEquals(5, service.addNota(new Nota("1", "1", "i", 5, LocalDate.of(2021, 10, 2)), "good"));
        assertEquals(1, (int) StreamSupport.stream(service.getAllStudenti().spliterator(), false).count());
        assertEquals(1, (int) StreamSupport.stream(service.getAllTeme().spliterator(), false).count());
        assertEquals(1, (int) StreamSupport.stream(service.getAllNote().spliterator(), false).count());
    }
}
