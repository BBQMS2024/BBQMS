package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TellerStation> tellerStations;

    @ManyToMany(mappedBy = "branches")
    private Set<BranchGroup> branchGroups;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(final Tenant tenant) {
        this.tenant = tenant;
    }

    public Set<TellerStation> getTellerStations() {
        return tellerStations;
    }

    public void setTellerStations(final Set<TellerStation> tellerStations) {
        this.tellerStations = tellerStations;
    }

    public Set<BranchGroup> getBranchGroups() {
        return branchGroups;
    }

    public void setBranchGroups(final Set<BranchGroup> branchGroups) {
        this.branchGroups = branchGroups;
    }
}
