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
public class Category extends Model {

    @Id
    @Column(length = 4)
    public String id;

    @Basic(optional = false)
    public String title;

    //@OneToMany(mappedBy = "category")
    //public List<Estate> estates = new ArrayList<>();

    public static Finder<String,Category> find = new Finder<>(Category.class);

    public Category(String pID, String pTitle){
        id = pID;
        title = pTitle;
    }
}
