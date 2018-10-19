package domain;

/**
 * Created by User on 10/18/2018.
 */
import com.fasterxml.jackson.annotation.JsonProperty;

public class Student {

    @JsonProperty
    private Integer id;
    @JsonProperty
    private String first_name;
    @JsonProperty
    private String last_name;

    public Student() {
        super();
    }

    public Student (String first_name, String last_name) {
        super();
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return first_name;
    }

    public void setFname(String first_name) {
        this.first_name = first_name;
    }

    public String getLname() {
        return last_name;
    }

    public void setLname(String last_name) {
        this.last_name = last_name;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name +
                '}';
    }
}
