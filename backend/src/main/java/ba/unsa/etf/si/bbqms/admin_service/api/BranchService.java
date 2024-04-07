package ba.unsa.etf.si.bbqms.admin_service.api;

import ba.unsa.etf.si.bbqms.domain.Branch;
import ba.unsa.etf.si.bbqms.domain.Service;
import ba.unsa.etf.si.bbqms.domain.TellerStation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BranchService {
    Optional<Branch> findById(final long branchId);
    Branch createBranch(final String name, final List<String> tellerStations, final String tenantCode) throws Exception;
    Set<Branch> getBranches(final String tenantCode);
    Branch updateBranch(final long branchId, final String name, final List<String> tellerStations, final String tenantCode) throws Exception;
    void removeBranch(final long branchId);
    TellerStation addStation(final String name, final long branchId) throws Exception;
    void removeStation(final long stationId) throws Exception;
    TellerStation updateStation(final long stationId, final String name) throws Exception;
    Set<Service> extractPossibleServices(final Branch branch);
}
