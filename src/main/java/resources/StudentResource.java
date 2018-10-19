package resources;

import domain.Student;
import services.StudentService;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "student", description = "Student Resource for performing CRUD operations on Student Table")
public class StudentResource {

    private final StudentService studentService;

    public StudentResource(StudentService studentService) {
        this.studentService = studentService;
    }

    @GET
    @Timed
    public Response getStudents() {
        return Response.ok(studentService.getStudents()).build();
    }

    @GET
    @Timed
    @Path("{id}")
    public Response getStudent(@PathParam("id") final int id) {
        return Response.ok(studentService.getStudent(id)).build();
    }

    @POST
    @Timed
    public Response createStudent(@NotNull @Valid final Student student) {
        Student studentCreate = new Student(student.getFname(),student.getLname());
        return Response.ok(studentService.createStudent(studentCreate)).build();
    }

    @PUT
    @Timed
    @Path("{id}")
    public Response editStudent(@NotNull @Valid final Student student,
                                 @PathParam("id") final int id) {
        student.setId(id);
        return Response.ok(studentService.editStudent(student)).build();
    }

    @DELETE
    @Timed
    @Path("{id}")
    public Response deleteStudent(@PathParam("id") final int id) {
        Map<String, String> response = new HashMap<>();
        response.put("status", studentService.deleteStudent(id));
        return Response.ok(response).build();
    }
}