package tr.gov.gib.iade.object.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IadeTalepResponse implements Serializable {

    private String talepSonucu;
    private Integer talepId;
}
