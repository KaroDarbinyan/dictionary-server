package am.karo.dictionary.repository;

import am.karo.dictionary.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("select w from Word w where w.wordGroup.id = :wordGroupId order by random()")
    Page<Word> findByWordGroupId(Long wordGroupId, Pageable page);

    Boolean existsByArmenianIgnoreCaseAndEnglishIgnoreCase(String armenian, String english);
}