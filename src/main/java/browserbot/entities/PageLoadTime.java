
package browserbot.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class PageLoadTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime localDateTime;

    @NotNull
    @Size(min = 5, message = "Minimum 5 characters")
    private String url;

    @Min(value = 1, message = "No less then zero.")
    private long loadTime;

    public PageLoadTime() {

    }

    public PageLoadTime(String url, long loadTime) {
        this.loadTime = loadTime;
        this.url = url;
    }
}
