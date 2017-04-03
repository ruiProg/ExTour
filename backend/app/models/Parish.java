package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class Parish extends Model {

    @Id
    @Column(length = 10)
    public String id;

    @Basic(optional = false)
    public String name;

    @ManyToOne()
    @Basic(optional = false)
    public Council council;

    public static Finder<String,Parish> find = new Finder<>(Parish.class);

    public Parish(String pID, String pName, String pCouncil){
        id = pID;
        name = pName;
        council = Council.find.byId(pCouncil);
    }
}
