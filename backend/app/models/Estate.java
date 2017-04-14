package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;

import java.util.ArrayList;
import java.util.List;

/*
@Entity
public class Estate extends Model {


    @Id
    @Column(length = 10)
    public String id;

    @Basic(optional = false)
    public String name;

    @ManyToOne()
    @Basic(optional = false)
    public District district;

    @OneToMany(mappedBy = "council")
    public List<Parish> councils = new ArrayList<>();

    public static Finder<String,Council> find = new Finder<>(Council.class);

    public Council(String pID, String pName, String pDistrict){
        id = pID;
        name = pName;
        district = District.find.byId(pDistrict);
    }

}
*/