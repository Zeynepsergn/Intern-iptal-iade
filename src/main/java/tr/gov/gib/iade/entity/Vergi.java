package tr.gov.gib.iade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vergi", schema = "gsths")
public class Vergi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vergi_id_gen")
    @SequenceGenerator(name = "vergi_id_gen", sequenceName = "vergi_vergi_id_seq", allocationSize = 1)
    @Column(name = "vergi_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "vergi_aciklama" , nullable = false)
    private String vergiAciklama;

    @NotNull
    @Column(name = "vergi_durum" , nullable = false)
    private Short vergiDurum;

}