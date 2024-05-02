package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String number;
    private Instant createdAt;
    private String deviceToken;
    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch branch;

    /**
     * Represents the teller station to which ticket is currently assigned, if assigned at all.
     */
    @OneToOne
    @JoinColumn(name = "teller_station_id", referencedColumnName = "id")
    private TellerStation tellerStation;

    public Ticket() {
    }

    public Ticket(final String number, final Instant createdAt, final String deviceToken, final Service service, final Branch branch, final TellerStation tellerStation) {
        this.number = number;
        this.createdAt = createdAt;
        this.deviceToken = deviceToken;
        this.service = service;
        this.branch = branch;
        this.tellerStation = tellerStation;
    }

    public Ticket(final String number, final Instant createdAt, final String deviceToken, final Service service, final Branch branch) {
        this(number, createdAt, deviceToken, service, branch, null);
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Service getService() {
        return service;
    }

    public void setService(final Service service) {
        this.service = service;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(final Branch branch) {
        this.branch = branch;
    }

    public TellerStation getTellerStation() {
        return tellerStation;
    }

    public void setTellerStation(final TellerStation tellerStation) {
        this.tellerStation = tellerStation;
    }
}
