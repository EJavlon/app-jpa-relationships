package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

    @GetMapping
    public Page<Group> getGroups(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page,10);
        return groupRepository.findAll(pageable);
    }

    @GetMapping("/byUniversityId/{universityId}")
    public Page<Group> getGroupsByUniversityId(@PathVariable Integer universityId,@RequestParam int page) {
        Pageable pageable = PageRequest.of(page,10);
        return groupRepository.findAllByFaculty_UniversityId(universityId,pageable);
    }

    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {

        Group group = new Group();
        group.setName(groupDto.getName());

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }

    @PutMapping("/{id}")
    public String editGruop(@PathVariable Integer id, @RequestBody GroupDto groupDto){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()) return "Group not found";

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) return "Faculty not found";

        Group group = optionalGroup.get();
        group.setFaculty(optionalFaculty.get());
        group.setName(groupDto.getName());
        groupRepository.save(group);

        return "Group seccessfully edited";
    }

    @DeleteMapping("/{id}")
    public String deleteGruop(@PathVariable Integer id){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()) return "Group not found";
        groupRepository.deleteById(id);
        return "Group seccessfully deleted";
    }


}
