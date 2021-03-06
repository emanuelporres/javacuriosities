package ar.com.javacuriosities.hibernate.model.bidirectional_map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "executions")
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToMany(mappedBy = "execution")
    @MapKey(name = "name")
    private Map<String, Parameters> parameters;

    public Execution() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Parameters> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameters> parameters) {
        this.parameters = parameters;
    }
}
