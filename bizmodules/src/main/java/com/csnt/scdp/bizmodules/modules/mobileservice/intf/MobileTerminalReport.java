package com.csnt.scdp.bizmodules.modules.mobileservice.intf;

import org.apache.cxf.jaxrs.model.wadl.Description;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/9/23.
 */

@WebService
@Path("/mobile")
@Consumes(MediaType.APPLICATION_JSON)
public interface MobileTerminalReport {

    @WebMethod
    @GET
    @Path("/dashboard")
    @Produces(MediaType.APPLICATION_JSON)
    public Map getReport();

    @WebMethod
    @POST
    @Path("/uploadRegID")
    @Produces(MediaType.APPLICATION_JSON)
    HashMap uploadRegID(String pushToken);
}
