package ba.unsa.etf.si.bbqms.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public Role(final long id, final RoleName name) {
        this.id = id;
        this.name = name;
    }

    public Role(final RoleName name) {
        this(-1, name);
    }

    public Role() {
    }
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(final RoleName name) {
        this.name = name;
    }

}

