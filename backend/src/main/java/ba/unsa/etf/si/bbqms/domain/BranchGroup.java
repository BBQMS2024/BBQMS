package ba.unsa.etf.si.bbqms.domain;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74

import java.util.Set;

@Entity
@Table(name = "branch_group")
public class BranchGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "branch_group_branch",
            joinColumns = @JoinColumn(name = "branch_group_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<Branch> branches;

    @ManyToMany
    @JoinTable(
            name = "branch_group_service",
            joinColumns = @JoinColumn(name = "branch_group_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services;

<<<<<<< HEAD
    public BranchGroup() {
    }

    public BranchGroup(final String name, final Set<Branch> branches, final Set<Service> services) {
        this.name = name;
        this.branches = branches;
        this.services = services;
=======
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    public BranchGroup() {
    }

    public BranchGroup(final String name, final Set<Branch> branches, final Set<Service> services, final Tenant tenant) {
        this.name = name;
        this.branches = branches;
        this.services = services;
        this.tenant = tenant;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
    }

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

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(final Set<Branch> branches) {
        this.branches = branches;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(final Set<Service> services) {
        this.services = services;
    }
<<<<<<< HEAD
=======

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(final Tenant tenant) {
        this.tenant = tenant;
    }
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
}
