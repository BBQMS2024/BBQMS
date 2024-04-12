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
<<<<<<< HEAD
=======
    private boolean active;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74

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

<<<<<<< HEAD
    public TellerStation(final String name, final Branch branch, final Display display) {
        this.name = name;
=======
    public TellerStation(final String name, final boolean active, final Branch branch, final Display display) {
        this.name = name;
        this.active = active;
>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
        this.branch = branch;
        this.display = display;
    }

<<<<<<< HEAD
=======
    public TellerStation(final String name, final Branch branch, final Display display) {
        this(name, true, branch, display);
    }

>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
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

<<<<<<< HEAD
=======
    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

>>>>>>> ebab4af6e7d562c0bcfecb58c846700ef866bc74
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
