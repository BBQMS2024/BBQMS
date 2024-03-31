package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "teller_station")
public class TellerStation {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "display_id", referencedColumnName = "id")
    private Display display;

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
}
