package resources;

/**
 * Created by User on 10/18/2018.
 */
import services.StudentService;
import com.codahale.metrics.health.HealthCheck;

public class StudentDWHealthCheckResource extends HealthCheck {

    private static final String HEALTHY_MESSAGE = "The Dropwizard blog Service is healthy for read and write";
    private static final String UNHEALTHY_MESSAGE = "The Dropwizard blog Service is not healthy. ";

    private final StudentService studentService;

    public StudentDWHealthCheckResource(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Result check() throws Exception {
        String mySqlHealthStatus = studentService.performHealthCheck();

        if (mySqlHealthStatus == null) {
            return Result.healthy(HEALTHY_MESSAGE);
        } else {
            return Result.unhealthy(UNHEALTHY_MESSAGE , mySqlHealthStatus);
        }
    }
}