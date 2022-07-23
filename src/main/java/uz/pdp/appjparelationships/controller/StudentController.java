package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private GroupRepository groupRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{universityId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return  studentRepository.findAllByGroup_FacultyId(facultyId,pageable);
    }

    //4. GROUP OWNER
    @GetMapping("/forGroupOwner/{groupId}")
    public Page<Student> getStudentListForGroupOwner(@PathVariable Integer groupId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return  studentRepository.findAllByGroupId(groupId,pageable);
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto){
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()) return "Address not found";

        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) return "Address not found";

        boolean exists = studentRepository.existsByFirstNameAndLastNameAndAddress
                (studentDto.getFirstName(), studentDto.getLastName(), optionalAddress.get());
        if (exists) return "Such a student exists";

        Student student = new Student();
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setSubjects(Arrays.asList(studentDto.getSubjects()));
        studentRepository.save(student);

        return "Student seccessfully saved";
    }

    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) return "Student not found";

        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()) return "Address not found";

        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()) return "Address not found";

        Student student = optionalStudent.get();
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setSubjects(Arrays.asList(studentDto.getSubjects()));
        studentRepository.save(student);

        return "Student seccessfully edited";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) return "Student not found";
        studentRepository.delete(optionalStudent.get());
        return "Student seccessfully deleted";
    }
}
