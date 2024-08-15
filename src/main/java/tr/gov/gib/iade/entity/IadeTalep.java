package tr.gov.gib.iade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "iade_talep", schema = "gsths")
public class IadeTalep {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iade_talep_id_gen")
    @SequenceGenerator(name = "iade_talep_id_gen", sequenceName = "iade_talep_iade_talep_id_seq", allocationSize = 1)
    @Column(name = "iade_talep_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odeme_id", nullable = false)
    private Odeme odemeId;

    @NotNull
    @Column(name = "iade_talep_tur", nullable = false)
    private Short iadeTalepTur;

    @NotNull
    @Column(name = "optime", nullable = false)
    private OffsetDateTime optime;

    @NotNull
    @Column(name = "iade_talep_durum", nullable = false)
    private Short iadeTalepDurum;

    @Size(max = 255)
    @Column(name = "iade_aciklama")
    private String iadeAciklama;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mukellef_kullanici_id", nullable = false)
    private MukellefKullanici mukellefKullaniciId;

    @Size(max = 26)
    @NotNull
    @Column(name = "mukellef_iban", length = 26, nullable = false)
    private String mukellefIban;

}
