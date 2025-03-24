package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;
import selecting.platform.model.Enum.SubCategoryKind;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subcategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subcategoryId;

    @Column
    @Enumerated(EnumType.STRING)
    private SubCategoryKind subCategoryKind;

    @OneToMany(mappedBy = "subcategory")
    private Set<ServicePost> servicePosts = new HashSet<>();
}
