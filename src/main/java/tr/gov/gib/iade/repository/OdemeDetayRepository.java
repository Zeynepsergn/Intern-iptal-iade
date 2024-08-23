package tr.gov.gib.iade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tr.gov.gib.iade.entity.OdemeDetay;

import java.util.List;

@Repository
public interface OdemeDetayRepository extends JpaRepository<OdemeDetay, Integer> {
    @Query("""
        SELECT o FROM OdemeDetay o
        JOIN FETCH o.odemeId od
        JOIN FETCH o.mukellefKullaniciId m
        WHERE m.tckn = :tckn
    """)
    List<OdemeDetay> findOdemeDetaysByTckn(@Param("tckn") String tckn);

    @Query("""
        SELECT d FROM OdemeDetay d
        JOIN FETCH d.mukellefKullaniciId mk
        JOIN FETCH d.odemeId o
        JOIN FETCH o.mukellefBorcId b
        WHERE mk.tckn = :tckn AND o.id = :odemeId
    """)
    List<OdemeDetay> findOdemeDetaysByTcknAndOdemeId(@Param("tckn") String tckn, @Param("odemeId") Integer odemeId);

    @Query("SELECT d FROM OdemeDetay d WHERE d.odemeId.id IN :odemeId")
    List<OdemeDetay> findByOdemeIdIn(@Param("odemeId") List<Integer> odemeId);

    @Query("""
        SELECT d FROM OdemeDetay d
        JOIN FETCH d.mukellefKullaniciId mk
        JOIN FETCH d.odemeId o
        JOIN FETCH d.odemeId.mukellefBorcId b
        WHERE d.odemeId.id = :odemeId
    """)
    OdemeDetay findByOdemeId(@Param("odemeId") Integer odemeId);

    @Query("""
        SELECT d FROM OdemeDetay d
        JOIN FETCH d.mukellefKullaniciId mk
        JOIN FETCH d.odemeId o
        JOIN FETCH d.odemeId.mukellefBorcId b
        WHERE d.mukellefKullaniciId.tckn = :tckn AND b.mukellefKullaniciId = mk AND d.odemeId.id = o.id
    """)
    List<OdemeDetay> findOdemeDetayByTckn(@Param("tckn") String tckn);
}
