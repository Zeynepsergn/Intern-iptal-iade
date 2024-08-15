package tr.gov.gib.iade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mukellef_kullanici", schema = "gsths")
public class MukellefKullanici {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mukellef_kullanici_id_gen")
    @SequenceGenerator(name = "mukellef_kullanici_id_gen", sequenceName = "mukellef_kullanici_mukellef_kullanici_id_seq", allocationSize = 1)
    @Column(name = "mukellef_kullanici_id", nullable = false)
    private Integer id;

    @Size(max = 11)
    @NotNull
    @Column(name = "tckn", length = 11 , nullable = false)
    private String tckn;

    @Size(max = 10)
    @NotNull
    @Column(name = "vkn", length = 10 , nullable = false)
    private String vkn;

    @Size(max = 100)
    @NotNull
    @Column(name = "mukellef_ad", length = 100 , nullable = false)
    private String mukellefAd;

    @Size(max = 100)
    @NotNull
    @Column(name = "mukellef_soyad", length = 100 , nullable = false)
    private String mukellefSoyad;

    @Size(max = 100)
    @NotNull
    @Column(name = "mukellef_unvan", length = 100 , nullable = false)
    private String mukellefUnvan;

}