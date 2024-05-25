package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.Service;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends BaseRepository<Service, Long> {
    List<Service> findAllByTenant_Code(final String code);
}

