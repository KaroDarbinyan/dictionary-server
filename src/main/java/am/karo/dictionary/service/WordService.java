package am.karo.dictionary.service;

import am.karo.dictionary.entity.Word;
import am.karo.dictionary.mapper.WordMapper;
import am.karo.dictionary.repository.WordRepository;
import am.karo.dictionary.service.dto.WordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordService {

    private final WordMapper wordMapper;
    private final WordRepository wordRepository;

    @Autowired
    public WordService( WordMapper wordMapper, WordRepository wordRepository) {
        this.wordMapper = wordMapper;
        this.wordRepository = wordRepository;
    }

    public WordDTO save(WordDTO wordDTO) {
        Word word = wordMapper.toEntity(wordDTO);
        word = wordRepository.save(word);
        return wordMapper.toDto(word);
    }

    public WordDTO partialUpdate(Long id, WordDTO wordDTO) {
        return wordRepository
                .findById(id)
                .map(
                        existingWord -> {
                            wordMapper.partialUpdate(existingWord, wordDTO);
                            return existingWord;
                        }
                )
                .map(wordRepository::save)
                .map(wordMapper::toDto)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Page<WordDTO> findAll(Pageable pageable) {
        return wordRepository.findAll(pageable).map(wordMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<WordDTO> findAllByWordGroupId(Long id, Pageable pageable) {
        return wordRepository.findByWordGroupId(id, pageable).map(wordMapper::toDto);
    }

    @Transactional(readOnly = true)
    public WordDTO findOne(Long id) {
        return wordRepository.findById(id).map(wordMapper::toDto).orElseThrow(RuntimeException::new);
    }

    public void delete(Long id) {
        wordRepository.deleteById(id);
    }

    public Boolean existByArmenianAndEnglish(WordDTO wordDTO) {
        return wordRepository.existsByArmenianIgnoreCaseAndEnglishIgnoreCase(wordDTO.getArmenian(), wordDTO.getEnglish());
    }
}