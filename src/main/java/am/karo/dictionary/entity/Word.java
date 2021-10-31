package am.karo.dictionary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "word")
@EqualsAndHashCode(callSuper = true)
public class Word extends AbstractAuditingEntity {

    @Column(name = "armenian", nullable = false)
    private String armenian;

    @Column(name = "english", nullable = false)
    private String english;

    @ManyToOne(optional = false)
    private WordGroup wordGroup;


}