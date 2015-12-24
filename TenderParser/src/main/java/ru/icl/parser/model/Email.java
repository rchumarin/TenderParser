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

@Entity
@Table(name="email")
public class Email  implements java.io.Serializable {

    private String keyword;
    private String emailEmploye;
    private List<Tender> tenders = new ArrayList<Tender>();

    public Email() {}
	
    public Email(String keyword) {this.keyword = keyword;}
    
    @Id     
    @Column(name="keyword", unique=true, nullable=false)
    public String getKeyword() {
        return this.keyword;
    }    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    @Column(name="email_employe")
    public String getEmailEmploye() {
        return this.emailEmploye;
    }
    public void setEmailEmploye(String emailEmploye) {
        this.emailEmploye = emailEmploye;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "email")    
    @OrderBy(value = "id")
    public List<Tender> getTenders() {
        return this.tenders;
    }    
    public void setTenders(List<Tender> tenders) {
        this.tenders = tenders;
    }

}
