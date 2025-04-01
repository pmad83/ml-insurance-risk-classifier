package pl.pm.mlinsuranceriskclassifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pm.mlinsuranceriskclassifier.entity.Customer;

/*
 * Repozytorium CustomerRepository zapewnia operacje CRUD dla encji Customer.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
