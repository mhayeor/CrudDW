package dao;

/**
 * Created by User on 10/18/2018.
 */
import domain.Student;
import mapper.StudentMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(StudentMapper.class)
public interface StudentDao {

    @SqlQuery("select * from student;")
    public List<Student> getStudents();

    @SqlQuery("select * from student where id = :id")
    public Student getStudent(@Bind("id") final int id);

    @SqlUpdate("insert into student(first_name, last_name) values(:first_name, :last_name)")
    void createStudent(@BindBean final Student student);

    @SqlUpdate("update student set first_name = coalesce(:first_name, first_name), " +
            " last_name = coalesce(:last_name, last_name)" +
            " where id = :id")
    void editStudent(@BindBean final Student student);

    @SqlUpdate("delete from student where id = :id")
    int deleteStudent(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    public int lastInsertId();
}