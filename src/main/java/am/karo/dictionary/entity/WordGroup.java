package am.karo.dictionary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "word_group")
@EqualsAndHashCode(callSuper = true)
public class WordGroup extends AbstractAuditingEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "wordGroup")
    private Set<Word> words = new HashSet<>();

}