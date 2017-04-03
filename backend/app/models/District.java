package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

import java.util.ArrayList;
import java.util.List;

@Entity
public class District extends Model {

    @Id
    @Column(length = 10)
    public String id;

    @Basic(optional = false)
    public String name;

    @OneToMany(mappedBy = "district")
    public List<Council> councils = new ArrayList<>();

    public static Finder<String,District> find = new Finder<>(District.class);

    public District(String pID, String pName){
        id = pID;
        name = pName;
    }
}
