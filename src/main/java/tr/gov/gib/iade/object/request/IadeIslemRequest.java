package tr.gov.gib.iade.object.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class IadeIslemRequest  {

        private Integer odemeId;
        @JsonProperty("Tckn")
        private String tckn;
}
