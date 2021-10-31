package am.karo.dictionary.repository;

import am.karo.dictionary.entity.WordGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordGroupRepository extends JpaRepository<WordGroup, Long> {
}