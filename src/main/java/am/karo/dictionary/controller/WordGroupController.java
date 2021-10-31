package am.karo.dictionary.controller;

import am.karo.dictionary.service.WordGroupService;
import am.karo.dictionary.service.WordService;
import am.karo.dictionary.service.dto.WordDTO;
import am.karo.dictionary.service.dto.WordGroupDTO;
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
@RequestMapping("/api/word-groups")
public class WordGroupController {

    private final WordService wordService;
    private final WordGroupService wordGroupService;

    @Autowired
    public WordGroupController(
            WordService wordService,
            WordGroupService wordGroupService
    ) {
        this.wordService = wordService;
        this.wordGroupService = wordGroupService;
    }

    @PostMapping
    public ResponseEntity<WordGroupDTO> createWordGroup(@Valid @RequestBody WordGroupDTO wordGroupDTO) throws URISyntaxException {
        WordGroupDTO result = wordGroupService.save(wordGroupDTO);
        return ResponseEntity
                .created(new URI("/api/word-groups/" + result.getId()))
                .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordGroupDTO> updateWordGroup(
            @PathVariable(value = "id") final Long id,
            @Valid @RequestBody WordGroupDTO wordGroupDTO
    ) {
        return ResponseEntity.ok().body(wordGroupService.save(wordGroupDTO));
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WordGroupDTO> partialUpdateWordGroup(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody WordGroupDTO wordGroupDTO
    ) {
        return ResponseEntity.ok().body(wordGroupService.partialUpdate(wordGroupDTO));
    }

    @GetMapping
    public ResponseEntity<Page<WordGroupDTO>> getAllWordGroups(Pageable pageable) {
        Page<WordGroupDTO> page = wordGroupService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/word-groups/{id}")
    public ResponseEntity<WordGroupDTO> getWordGroup(@PathVariable Long id) {
        return ResponseEntity.ok().body(wordGroupService.findOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWordGroup(@PathVariable Long id) {
        wordGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/words")
    public ResponseEntity<Page<WordDTO>> getAllWords(@PathVariable("id") Long id, Pageable pageable) {
        Page<WordDTO> page = wordService.findAllByWordGroupId(id, pageable);
        return ResponseEntity.ok().body(page);
    }
}