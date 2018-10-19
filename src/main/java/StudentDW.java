/**
 * Created by User on 10/15/2018.
 */
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import resources.StudentDWHealthCheckResource;
import resources.StudentResource;
import resources.PingResource;
import services.StudentService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import org.skife.jdbi.v2.DBI;


public class StudentDW extends Application<StudentDWConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(StudentDW.class);
    private static final String SQL = "sql";
    private static final String DROPWIZARD_MYSQL_SERVICE = "Dropwizard MySQL Service";

    public static void main(String[] args) throws Exception {
        new StudentDW().run("server", args[0]);
    }

    @Override
    public void initialize(Bootstrap<StudentDWConfiguration> b) {
        b.addBundle(new SwaggerBundle<StudentDWConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(StudentDWConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(StudentDWConfiguration config, Environment env)
            throws Exception {
        // Datasource configuration
        final DataSource dataSource =
                config.getDataSourceFactory().build(env.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        // Register Health Check
        StudentDWHealthCheckResource healthCheck =
                new StudentDWHealthCheckResource(dbi.onDemand(StudentService.class));
        env.healthChecks().register(DROPWIZARD_MYSQL_SERVICE, healthCheck);
        logger.info("Registering RESTful API resources");
        env.jersey().register(new PingResource());
        env.jersey().register(new StudentResource(dbi.onDemand(StudentService.class)));
    }
}