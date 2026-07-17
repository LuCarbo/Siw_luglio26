package it.uniroma3.siw.repository;


import it.uniroma3.siw.model.Arbitro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbitroRepository extends JpaRepository<Arbitro, Long> {
    // Utile per controllare che non si inseriscano arbitri doppi
    boolean existsByCodiceArbitrale(String codiceArbitrale);
}