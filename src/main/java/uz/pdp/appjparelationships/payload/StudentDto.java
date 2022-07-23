package uz.pdp.appjparelationships.payload;

import lombok.Data;
import uz.pdp.appjparelationships.entity.Subject;


@Data
public class StudentDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer addressId;
    private Integer groupId;
    private Subject []subjects;
}
