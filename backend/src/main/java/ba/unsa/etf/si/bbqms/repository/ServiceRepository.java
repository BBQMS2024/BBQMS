package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.Service;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends BaseRepository<Service, Long> {
}

