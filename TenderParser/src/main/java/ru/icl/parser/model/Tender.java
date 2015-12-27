package ru.icl.parser.model;

import javax.persistence.*;
import org.hibernate.FetchMode;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name="tender")
public class Tender  implements java.io.Serializable {
    
    private int id;
    private String idTender;
    private Email email;
    private String companyTender;
    private String nameTender;
    private String costTender;
    private String deadlineTender;
    private String urlTender;

    public Tender() {}
	
    public Tender(int id) {this.id = id;}
    
    public Tender(int id, String idTender, Email email, String companyTender, 
            String nameTender, String costTender, String deadlineTender, String urlTender) {
        this.id = id;
        this.idTender = idTender;
        this.email = email;
        this.companyTender = companyTender;
        this.nameTender = nameTender;
        this.costTender = costTender;
        this.deadlineTender = deadlineTender;
        this.urlTender = urlTender;
    }
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="id_tender", unique=true, nullable=false)
    public String getIdTender() {
        return this.idTender;
    }
    public void setIdTender(String idTender) {
        this.idTender = idTender;
    }
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "keyword")        
    public Email getEmail() {
        return this.email;
    }    
    public void setEmail(Email email) {
        this.email = email;
    }

    
    @Column(name="company_tender")
    public String getCompanyTender() {
        return this.companyTender;
    }    
    public void setCompanyTender(String companyTender) {
        this.companyTender = companyTender;
    }
    
    @Column(name="name_tender")
    public String getNameTender() {
        return this.nameTender;
    }    
    public void setNameTender(String nameTender) {
        this.nameTender = nameTender;
    }
    
    @Column(name="cost_tender")
    public String getCostTender() {
        return this.costTender;
    }    
    public void setCostTender(String costTender) {
        this.costTender = costTender;
    }

    @Column(name="deadline_tender")
    public String getDeadlineTender() {
        return this.deadlineTender;
    }    
    public void setDeadlineTender(String deadlineTender) {
        this.deadlineTender = deadlineTender;
    }
    
    @Column(name="url_tender")
    public String getUrlTender() {
        return this.urlTender;
    }    
    public void setUrlTender(String urlTender) {
        this.urlTender = urlTender;
    }

}

