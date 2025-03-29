package selecting.platform.model;

import jakarta.persistence.*;
import lombok.*;
import selecting.platform.model.Enum.SubCategoryKind;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_kind", length = 20)
    private SubCategoryKind categoryKind;
}
