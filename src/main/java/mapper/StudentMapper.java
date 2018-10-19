package mapper;

/**
 * Created by User on 10/18/2018.
 */
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import domain.Student;


public class StudentMapper implements ResultSetMapper<Student> {
    private static final String ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    public Student map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        Student student = new Student(resultSet.getString(FIRST_NAME), resultSet.getString(LAST_NAME));
        student.setId(resultSet.getInt(ID));
        return student;
    }
}