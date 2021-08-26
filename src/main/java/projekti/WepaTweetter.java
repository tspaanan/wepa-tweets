package projekti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author tspaanan
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class WepaTweetter extends AbstractPersistable<Long> {
    
    private String username;
    private String password;
    private String realname;
    private String random;
    //@OneToMany//(fetch = FetchType.LAZY) //fluentinium tests require EAGER
    //private List<WepaTweetter> following = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "followed")
    private List<WepaFollower> following = new ArrayList<>(); //tämä seuraa
    @JsonIgnore
    @OneToMany(mappedBy = "followedBy")
    private List<WepaFollower> followingBy = new ArrayList<>(); //nämä seuraavat
    @JsonIgnore
    @OneToMany
    private List<WepaTweetter> blocked = new ArrayList<>();
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof WepaTweetter)) {
            return false;
        }

        WepaTweetter otherWepaTweetter = (WepaTweetter) other;

        if (this.username.equals(otherWepaTweetter.username)) {
            return true;
        }

        return false;
    }

}
