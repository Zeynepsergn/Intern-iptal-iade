package tr.gov.gib.iade.object.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IadeTalepRequest extends BaseId{

    private Short iadeTalepTur;
    private Short iadeTalepDurum;
    private String iadeAciklama;
    private Integer mukellefKullaniciId;
    private String mukellefIban;
}
