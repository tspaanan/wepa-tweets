package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @OneToMany(fetch = FetchType.LAZY)
    private List<WepaTweetter> following = new ArrayList<>();
}
