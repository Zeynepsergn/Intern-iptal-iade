package tr.gov.gib.iade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tr.gov.gib.iade.entity.IadeEmanet;

import java.util.List;

@Repository
public interface IadeEmanetRepository extends JpaRepository<IadeEmanet, Integer> {

    // İade talep ID'sine göre İadeEmanetleri bulur
    @Query("SELECT i FROM IadeEmanet i WHERE i.id = :iadeTalepId")
    List<IadeEmanet> findByIadeTalepId(@Param("iadeTalepId") Integer iadeTalepId);

    // Ödeme ID'sine göre İadeEmanetleri bulur
    @Query("SELECT i FROM IadeEmanet i WHERE i.odemeId = :odemeId")
    List<IadeEmanet> findByOdemeId(@Param("odemeId") Integer odemeId);

    // Durumuna göre İadeEmanetleri bulur
    @Query("SELECT i FROM IadeEmanet i WHERE i.iadeEmanetDurum = :durum")
    List<IadeEmanet> findByDurum(@Param("durum") Short durum);
}