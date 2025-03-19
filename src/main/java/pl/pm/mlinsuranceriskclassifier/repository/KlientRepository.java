package pl.pm.mlinsuranceriskclassifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pm.mlinsuranceriskclassifier.entity.Klient;

/*
 * Repozytorium KlientRepository zapewnia operacje CRUD dla encji Klient.
 */
@Repository
public interface KlientRepository extends JpaRepository<Klient, Long> {
}
