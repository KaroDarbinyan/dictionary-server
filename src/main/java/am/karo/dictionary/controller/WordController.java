package am.karo.dictionary.controller;

import am.karo.dictionary.service.WordService;
import am.karo.dictionary.service.dto.WordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/words")
public class WordController {

    private final WordService wordService;

    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping
    public ResponseEntity<WordDTO> createWord(@Valid @RequestBody WordDTO wordDTO) throws URISyntaxException {
        WordDTO result = wordService.save(wordDTO);
        return ResponseEntity.created(new URI("/api/words/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordDTO> updateWord(@PathVariable(value = "id") final Long id, @Valid @RequestBody WordDTO wordDTO) throws URISyntaxException {
        WordDTO result = wordService.save(wordDTO);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WordDTO> partialUpdateWord(
            @PathVariable(value = "id") final Long id,
            @NotNull @RequestBody WordDTO wordDTO
    ) {
        return ResponseEntity.ok().body(wordService.partialUpdate(id, wordDTO));
    }

    @GetMapping
    public ResponseEntity<Page<WordDTO>> getAllWords(Pageable pageable) {
        Page<WordDTO> page = wordService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WordDTO> getWord(@PathVariable Long id) {
        return ResponseEntity.ok().body(wordService.findOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        wordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/exist")
    public ResponseEntity<Boolean> existWord(@Valid @RequestBody WordDTO wordDTO) {
        return ResponseEntity.ok().body(wordService.existByArmenianAndEnglish(wordDTO));
    }
}