package org.greengin.senseitweb.rs.activities.senseit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityManager;
import org.greengin.senseitweb.logic.project.senseit.SenseItProfileRequest;
import org.greengin.senseitweb.logic.project.senseit.SensorInputRequest;

@Path("/project/{projectId}/senseit")
public class SenseItActivityService {

	@Path("/profiles")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, SenseItProfileRequest profileData, @Context HttpServletRequest request) {
		SenseItActivityManager editor = new SenseItActivityManager(projectId, request);
		return editor.createProfile(profileData);
	}

	@Path("/profile/{profileId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, SenseItProfileRequest profileData, @Context HttpServletRequest request) {
		SenseItActivityManager editor = new SenseItActivityManager(projectId, request);
		return editor.updateProfile(profileId, profileData);
	}
	
	@Path("/profile/{profileId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, @Context HttpServletRequest request) {
		SenseItActivityManager editor = new SenseItActivityManager(projectId, request);
		return editor.deleteProfile(profileId);
	}
	
	
	@Path("/profile/{profileId}/inputs")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, SensorInputRequest inputData, @Context HttpServletRequest request) {
		SenseItActivityManager editor = new SenseItActivityManager(projectId, request);
		return editor.createSensor(profileId, inputData);
	}

	@Path("/profile/{profileId}/input/{inputId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, @PathParam("inputId") Long inputId, SensorInputRequest inputData, @Context HttpServletRequest request) {
		SenseItActivityManager editor = new SenseItActivityManager(projectId, request);
		return editor.updateSensor(profileId, inputId, inputData);
	}
	
	@Path("/profile/{profileId}/input/{inputId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, @PathParam("inputId") Long inputId, @Context HttpServletRequest request) {
		SenseItActivityManager editor = new SenseItActivityManager(projectId, request);
		return editor.deleteSensor(profileId, inputId);
	}

}
