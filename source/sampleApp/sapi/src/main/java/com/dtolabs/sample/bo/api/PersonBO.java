package com.dtolabs.sample.bo.api;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.dtolabs.sample.dto.Person;


@WebService
@Path("/")
public interface PersonBO {

    @GET
    @Path("/findByPersonId/{personId}/")
    @Produces({ "application/json" })
    Person findByPersonId(@PathParam("personId") long personId) throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException;;

    @GET
    @Path("/getPersonCount/")
    @Produces({ "text/plain" })
    int getPersonCount() throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException;;

    @GET
    @Path("/getPersonNames/")
    @Produces({ "application/json" })
    List<Person> getPersons() throws InvalidArgumentException, NoDataFoundException, ServiceUnAvailableException;;

    @POST
    @Path("/save/")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Boolean save(Person person) throws InvalidArgumentException, ServiceUnAvailableException;;

}
