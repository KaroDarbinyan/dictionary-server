package am.karo.dictionary.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordGroupDTO implements Serializable {

    private Long id;
    @NotNull
    private String name;

}