package am.karo.dictionary.mapper;

import am.karo.dictionary.entity.WordGroup;
import am.karo.dictionary.service.dto.WordGroupDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", uses = {})
public interface WordGroupMapper extends EntityMapper<WordGroupDTO, WordGroup> {
}
