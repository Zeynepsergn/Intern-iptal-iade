package tr.gov.gib.iade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tr.gov.gib.iade.entity.VergiOdemeTur;

@Repository
public interface VergiOdemeTurRepository extends JpaRepository<VergiOdemeTur, Long> {

    @Query("SELECT v.vergiOdemeTur FROM VergiOdemeTur v WHERE v.vergiId.id = :vergiId")
    Short findVergiOdemeTurByVergiId(@Param("vergiId") Short vergiId);
}
