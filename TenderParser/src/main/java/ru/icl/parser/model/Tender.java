package ru.icl.parser.model;

import java.util.Set;
import javax.persistence.*;
import javax.persistence.FetchType;
import java.io.Serializable;

@Entity
@Table(name="tender")
public class Tender  implements Serializable {
    
    @Id
    @Column(name = "id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private int id;

    @Column(name="id_tender", unique=true, nullable=false)
    private String idTender;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "keyword") 
    private Email email;
    
    @Column(name="company_tender")
    private String companyTender;
    
    @Column(name="name_tender")
    private String nameTender;
    
    @Column(name="cost_tender")    
    private String costTender;
    
    @Column(name="deadline_tender")
    private String deadlineTender;
    
    @Column(name="url_tender")
    private String urlTender;

    public Tender() {}
	
    public Tender(int id) {this.id = id;}

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
 
    public String getIdTender() {
        return this.idTender;
    }
    public void setIdTender(String idTender) {
        this.idTender = idTender;
    }
           
    public Email getEmail() {
        return this.email;
    }    
    public void setEmail(Email email) {
        this.email = email;
    }         

    public String getCompanyTender() {
        return this.companyTender;
    }    
    public void setCompanyTender(String companyTender) {
        this.companyTender = companyTender;
    }

    public String getNameTender() {
        return this.nameTender;
    }    
    public void setNameTender(String nameTender) {
        this.nameTender = nameTender;
    }

    public String getCostTender() {
        return this.costTender;
    }    
    public void setCostTender(String costTender) {
        this.costTender = costTender;
    }

    public String getDeadlineTender() {
        return this.deadlineTender;
    }    
    public void setDeadlineTender(String deadlineTender) {
        this.deadlineTender = deadlineTender;
    }

    public String getUrlTender() {
        return this.urlTender;
    }    
    public void setUrlTender(String urlTender) {
        this.urlTender = urlTender;
    }
}
