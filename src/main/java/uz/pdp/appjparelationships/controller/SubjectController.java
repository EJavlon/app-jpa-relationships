package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    @PostMapping
    public String addSubject(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }

    @GetMapping
    public Page<Subject> getSubjects(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page,10);
        return subjectRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()) return null;
        return optionalSubject.get();
    }

    @PutMapping("/{id}")
    public String editSubject(@PathVariable Integer id, @RequestBody Subject subject){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()) return "Subject not found";
        subject.setId(id);
        subjectRepository.save(subject);
        return "Subject seccessfully edited";
    }

    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()) return "Subject not found";
        subjectRepository.delete(optionalSubject.get());
        return "Subject seccessfuly deleted";
    }
}
