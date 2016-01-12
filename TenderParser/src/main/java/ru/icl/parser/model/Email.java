package ru.icl.parser.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable; 

@Entity
@Table(name="email")
public class Email  implements Serializable {
    
    @Id     
    @Column(name="keyword", unique=true, nullable=false)
    private String keyword;
    
    @Column(name="email_employe")
    private String emailEmploye;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "email")    
    private Set<Tender> tenders = new HashSet(0);    
    
    public Email() {}
	
    public Email(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getEmailEmploye() {
        return this.emailEmploye;
    }
    public void setEmailEmploye(String emailEmploye) {
        this.emailEmploye = emailEmploye;
    }

    public Set<Tender> getTenders() {
        return tenders;
    }
    public void setTenders(Set<Tender> tenders) {
        this.tenders = tenders;
    }    
}
