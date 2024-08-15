package tr.gov.gib.iade.object.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IadeIslemResponse {
    private Integer iadeId;
    private Integer odemeId;
    private String tckn;
    private String mukellefAdi;
    private String mukellefSoyadi;
    private BigDecimal iadeTutar;
    private Character iadeTalepTur;
    private String iadeTalepTurDsc;
    private Character iadeDurum;
    private String iadeDurumDsc;
    private LocalDateTime iadeZamani;
    private String iadeAciklama;
}
