package br.com.catalagoproduto.catalagoprotudo.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Audit {

    @Column(name = "DATE_CREATED")
    private LocalDateTime created;

    @Column(name = "DATE_CHANGED")
    private LocalDateTime changed;

    @Column(name = "HOTSTNAME_CREATED_IP")
    private String userCreatedHostname;

    @Column(name = "HOTSTNAME_CHANGED_IP")
    private String userChangedHostname;

    @PrePersist
    public void prePersist() throws UnknownHostException {
        userCreatedHostname = InetAddress.getLocalHost().getHostName();
        created = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() throws UnknownHostException {
        userChangedHostname = InetAddress.getLocalHost().getHostName();
        changed = LocalDateTime.now();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getChanged() {
        return changed;
    }

    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }

    public String getUserCreatedHostname() {
        return userCreatedHostname;
    }

    public void setUserCreatedHostname(String userCreatedHostname) {
        this.userCreatedHostname = userCreatedHostname;
    }

    public String getUserChangedHostname() {
        return userChangedHostname;
    }

    public void setUserChangedHostname(String userChangedHostname) {
        this.userChangedHostname = userChangedHostname;
    }
}
