package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.*;

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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(final Tenant tenant) {
        this.tenant = tenant;
    }
}
