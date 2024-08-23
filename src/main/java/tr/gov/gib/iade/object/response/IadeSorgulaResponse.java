package tr.gov.gib.iade.object.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IadeSorgulaResponse implements Serializable {

    private Integer borcId;
    private Integer odemeId;
    private String tckn;
    private BigDecimal tutar;
    private String ad;
    private String soyad;
    private String unvan;
    private String oid;
}
