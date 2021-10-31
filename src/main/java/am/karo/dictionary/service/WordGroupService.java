package am.karo.dictionary.service;

import am.karo.dictionary.entity.WordGroup;
import am.karo.dictionary.mapper.WordGroupMapper;
import am.karo.dictionary.repository.WordGroupRepository;
import am.karo.dictionary.service.dto.WordGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordGroupService {

    private final WordGroupRepository wordGroupRepository;

    private final WordGroupMapper wordGroupMapper;

    @Autowired
    public WordGroupService(WordGroupRepository wordGroupRepository, WordGroupMapper wordGroupMapper) {
        this.wordGroupRepository = wordGroupRepository;
        this.wordGroupMapper = wordGroupMapper;
    }

    public WordGroupDTO save(WordGroupDTO wordGroupDTO) {
        WordGroup wordGroup = wordGroupMapper.toEntity(wordGroupDTO);
        wordGroup = wordGroupRepository.save(wordGroup);
        return wordGroupMapper.toDto(wordGroup);
    }

    public WordGroupDTO partialUpdate(WordGroupDTO wordGroupDTO) {

        return wordGroupRepository
                .findById(wordGroupDTO.getId())
                .map(
                        existingWordGroup -> {
                            wordGroupMapper.partialUpdate(existingWordGroup, wordGroupDTO);
                            return existingWordGroup;
                        }
                )
                .map(wordGroupRepository::save)
                .map(wordGroupMapper::toDto)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Page<WordGroupDTO> findAll(Pageable pageable) {
        return wordGroupRepository.findAll(pageable).map(wordGroupMapper::toDto);
    }

    @Transactional(readOnly = true)
    public WordGroupDTO findOne(Long id) {
        return wordGroupRepository.findById(id).map(wordGroupMapper::toDto).orElseThrow(RuntimeException::new);
    }

    public void delete(Long id) {
        wordGroupRepository.deleteById(id);
    }
}