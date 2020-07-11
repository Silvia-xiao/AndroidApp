/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restMem;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author xnxxxnxx
 */
@Entity
@Table(name = "CREDENTIALS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credentials.findAll", query = "SELECT c FROM Credentials c")
    , @NamedQuery(name = "Credentials.findByUsername", query = "SELECT c FROM Credentials c WHERE c.username = :username")
    , @NamedQuery(name = "Credentials.findByPaswd", query = "SELECT c FROM Credentials c WHERE c.paswd = :paswd")
    , @NamedQuery(name = "Credentials.findBySignupDate", query = "SELECT c FROM Credentials c WHERE c.signupDate = :signupDate")})
public class Credentials implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "PASWD")
    private String paswd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SIGNUP_DATE")
    @Temporal(TemporalType.DATE)
    private Date signupDate;
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID")
    @OneToOne(optional = false)
    private Person personId;

    public Credentials() {
    }

    public Credentials(String username) {
        this.username = username;
    }

    public Credentials(String username, String paswd, Date signupDate) {
        this.username = username;
        this.paswd = paswd;
        this.signupDate = signupDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaswd() {
        return paswd;
    }

    public void setPaswd(String paswd) {
        this.paswd = paswd;
    }

    public Date getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(Date signupDate) {
        this.signupDate = signupDate;
    }

    public Person getPersonId() {
        return personId;
    }

    public void setPersonId(Person personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credentials)) {
            return false;
        }
        Credentials other = (Credentials) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restMem.Credentials[ username=" + username + " ]";
    }
    
}
