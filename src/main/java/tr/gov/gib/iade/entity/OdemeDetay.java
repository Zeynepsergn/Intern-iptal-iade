package tr.gov.gib.iade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal; // BigDecimal eklenmeli
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "odeme_detay", schema = "gsths")
public class OdemeDetay {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "odeme_detay_id_gen")
    @SequenceGenerator(name = "odeme_detay_id_gen", sequenceName = "odeme_detay_odeme_detay_id_seq", allocationSize = 1)
    @Column(name = "odeme_detay_id", nullable = false)
    private Integer odemeDetayid;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "odeme_id", nullable = false)
    private Odeme odemeId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mukellef_kullanici_id", nullable = false)
    private MukellefKullanici mukellefKullaniciId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vergi_id", nullable = false)
    private Vergi vergiId;

    @Size(max = 10)
    @NotNull
    @Column(name = "oid", length = 10, nullable = false)
    private String oid;

    @NotNull
    @Size(max = 100)
    @Column(name = "aciklama", length = 100, nullable = false)
    private String aciklama;

    @NotNull
    @Column(name = "odenen_borc_miktari", nullable = false)
    private BigDecimal odenenBorcMiktari; // Double yerine BigDecimal

    @NotNull
    @Column(name = "odeme_detay_durum", nullable = false)
    private Short odemeDetayDurum;

    @NotNull
    @Column(name = "iade_zamani", nullable = false)
    private OffsetDateTime iadeZamani;

    @NotNull
    @Column(name = "optime", nullable = false)
    private OffsetDateTime optime;
}
