package tr.gov.gib.iade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "odeme", schema = "gsths")
public class Odeme {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "odeme_id_gen")
    @SequenceGenerator(name = "odeme_id_gen", sequenceName = "odeme_odeme_id_seq", allocationSize = 1)
    @Column(name = "odeme_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mukellef_borc_id")
    private MukellefBorc mukellefBorcId;


    @NotNull
    @Column(name = "optime", nullable = false)
    private OffsetDateTime optime;

    @NotNull
    @Column(name = "odeme_durum" , nullable = false)
    private Short odemeDurum;

    @NotNull
    @Column(name = "vergi_tur_id", nullable = false)
    private Short vergiTurId;



}