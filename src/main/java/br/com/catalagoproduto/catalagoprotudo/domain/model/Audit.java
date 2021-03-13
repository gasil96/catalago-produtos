package br.com.catalagoproduto.catalagoprotudo.domain.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Audit {

    @CreatedDate
    @Column(name = "DATE_CREATED")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "DATE_CHANGED")
    private LocalDateTime changed;

    @Column(name = "HOTSTNAME_CREATED_IP")
    private String userCreatedIp;

    @Column(name = "HOTSTNAME_CHANGED_IP")
    private String userChangedIp;

    @PrePersist
    public void prePersist() throws UnknownHostException {
        userCreatedIp = InetAddress.getLocalHost().getHostName();
    }

    @PreUpdate
    public void preUpdate() throws UnknownHostException {
        userChangedIp = InetAddress.getLocalHost().getHostName();
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

    public String getUserCreatedIp() {
        return userCreatedIp;
    }

    public void setUserCreatedIp(String userCreatedIp) {
        this.userCreatedIp = userCreatedIp;
    }

    public String getUserChangedIp() {
        return userChangedIp;
    }

    public void setUserChangedIp(String userChangedIp) {
        this.userChangedIp = userChangedIp;
    }
}
