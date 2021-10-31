package am.karo.dictionary.repository;

import am.karo.dictionary.entity.Authority;
import am.karo.dictionary.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByName(Authorities name);
}
