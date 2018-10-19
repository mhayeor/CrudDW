package services;

        import dao.StudentDao;
        import domain.Student;
        import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
        import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
        import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import javax.ws.rs.WebApplicationException;
        import javax.ws.rs.core.Response.Status;

        import java.util.List;
        import java.util.Objects;

public abstract class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private static final String DATABASE_ACCESS_ERROR =
            "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
    private static final String DATABASE_CONNECTION_ERROR =
            "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
    private static final String UNEXPECTED_DATABASE_ERROR =
            "Unexpected error occurred while attempting to reach the database. Details: ";
    private static final String SUCCESS = "Success";
    private static final String UNEXPECTED_DELETE_ERROR = "An unexpected error occurred while deleting student.";
    private static final String STUDENT_NOT_FOUND = "Student id %s not found.";

    @CreateSqlObject
    abstract StudentDao studentDao();

    public List<Student> getStudents() {
        return studentDao().getStudents();
    }

    public Student getStudent(int id) {
        Student student = studentDao().getStudent(id);
        if (Objects.isNull(student)) {
            throw new WebApplicationException(String.format(STUDENT_NOT_FOUND, id), Status.NOT_FOUND);
        }
        return student;
    }

    public Student createStudent(Student student) {
        studentDao().createStudent(student);
        return studentDao().getStudent(studentDao().lastInsertId());
    }

    public Student editStudent(Student student) {
        if (Objects.isNull(studentDao().getStudent(student.getId()))) {
            throw new WebApplicationException(String.format(STUDENT_NOT_FOUND, student.getId()),
                    Status.NOT_FOUND);
        }
        studentDao().editStudent(student);
        return studentDao().getStudent(student.getId());
    }

    public String deleteStudent(final int id) {
        int result = studentDao().deleteStudent(id);
        logger.info("Result in StudentService.deleteStudent is: {}", result );
        switch (result) {
            case 1:
                return SUCCESS;
            case 0:
                throw new WebApplicationException(String.format(STUDENT_NOT_FOUND, id), Status.NOT_FOUND);
            default:
                throw new WebApplicationException(UNEXPECTED_DELETE_ERROR, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public String performHealthCheck() {
        try {
            studentDao().getStudents();
        } catch (UnableToObtainConnectionException ex) {
            return checkUnableToObtainConnectionException(ex);
        } catch (UnableToExecuteStatementException ex) {
            return checkUnableToExecuteStatementException(ex);
        } catch (Exception ex) {
            return UNEXPECTED_DATABASE_ERROR + ex.getCause().getLocalizedMessage();
        }
        return null;
    }

    private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
        if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
            return DATABASE_ACCESS_ERROR + ex.getCause().getLocalizedMessage();
        } else if (ex.getCause() instanceof java.sql.SQLException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return UNEXPECTED_DATABASE_ERROR + ex.getCause().getLocalizedMessage();
        }
    }

    private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
        if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return UNEXPECTED_DATABASE_ERROR + ex.getCause().getLocalizedMessage();
        }
    }
}