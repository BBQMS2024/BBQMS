package ba.unsa.etf.si.bbqms.repository;

import ba.ekapic1.stonebase.BaseRepository;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;

import java.util.Set;

public interface TellerStationRepository extends BaseRepository<TellerStation, Long> {
    Set<TellerStation> findAllByServicesContains(final Service service);
}
