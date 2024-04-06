package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "teller_station")
public class TellerStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "display_id", referencedColumnName = "id")
    private Display display;

    @ManyToMany
    @JoinTable(
            name = "teller_station_service",
            joinColumns = @JoinColumn(name = "teller_station_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services;

    public TellerStation() {
    }

    public TellerStation(final String name, final boolean active, final Branch branch, final Display display) {
        this.name = name;
        this.active = active;
        this.branch = branch;
        this.display = display;
    }

    public TellerStation(final String name, final Branch branch, final Display display) {
        this(name, true, branch, display);
    }

    public TellerStation(final String name, final Branch branch) {
        this(name, branch, null);
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(final Branch branch) {
        this.branch = branch;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(final Display display) {
        this.display = display;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(final Set<Service> services) {
        this.services = services;
    }
}
