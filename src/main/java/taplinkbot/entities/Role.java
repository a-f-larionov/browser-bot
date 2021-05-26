package taplinkbot.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    private Long id;

    @Size(min = 2, message = "Must be at least 2 characters")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
