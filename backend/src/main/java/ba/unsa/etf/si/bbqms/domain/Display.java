package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "display")
public class Display {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToOne(mappedBy = "display")
    private TellerStation tellerStation;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    public Display(){
    }

    public Display(final String name, final TellerStation tellerStation, final Branch branch) {
        this.name = name;
        this.tellerStation = tellerStation;
        this.branch = branch;
    }

    public Display(final String name, final Branch branch) { this(name, null, branch); }

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

    public TellerStation getTellerStation() {
        return tellerStation;
    }

    public void setTellerStation(final TellerStation tellerStation) {
        this.tellerStation = tellerStation;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(final Branch branch) {
        this.branch = branch;
    }
}
