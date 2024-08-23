package tr.gov.gib.iade.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tr.gov.gib.iade.entity.IadeTalep;

import java.util.List;

@Repository
public interface IadeTalepRepository extends JpaRepository<IadeTalep, Integer> {

    @Query("""
        SELECT m FROM IadeTalep m
        JOIN FETCH m.mukellefKullaniciId k
        JOIN FETCH m.odemeId o
        WHERE k.tckn = :tckn AND o.id = :odemeId
        """)
    List<IadeTalep> findIadeTalepsByTcknAndOdemeId(@Param("tckn") String tckn, @Param("odemeId") Integer odemeId);

    @Query("""
        SELECT m FROM IadeTalep m
        JOIN FETCH m.mukellefKullaniciId k
        JOIN FETCH m.odemeId o
        WHERE k.tckn = :tckn
    """)
    List<IadeTalep> findIadeTalepsByTckn(@Param("tckn") String tckn);

    IadeTalep findIadeTalepById(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE IadeTalep i SET i.iadeTalepDurum = :newStatus WHERE i.id = :id AND i.iadeTalepDurum = 0")
    int updateIadeTalepDurum(@Param("id") Integer id, @Param("newStatus") Short newStatus);
}
