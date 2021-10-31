package am.karo.dictionary.mapper;

import am.karo.dictionary.entity.Word;
import am.karo.dictionary.service.dto.WordDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;


@Service
@Mapper(componentModel = "spring", uses = {})
public interface WordMapper extends EntityMapper<WordDTO, Word> {
}
