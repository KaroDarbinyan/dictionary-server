package am.karo.dictionary.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordDTO implements Serializable {

    private Long id;
    @NotNull
    private String armenian;
    @NotNull
    private String english;
    @NotNull
    private WordGroupDTO wordGroup;

}