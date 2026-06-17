package com.bookmie.lit.projects;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.lit.configs.components.CurrentUser;
import com.bookmie.lit.projects.dtos.AddCollaboratorDto;
import com.bookmie.lit.projects.dtos.CreateProjectDto;
import com.bookmie.lit.projects.dtos.InviteUserDto;
import com.bookmie.lit.projects.dtos.UpdateEnvDataDto;
import com.bookmie.lit.users.dtos.UserPublicDto;
import com.bookmie.lit.utils.ResponseFactory;
import com.bookmie.lit.utils.dtos.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectService projectService;
  private final CurrentUser currentUser;

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<ProjectModel>>> getProjects(Authentication auth) {
    return ResponseFactory.ok("successfull", this.projectService.getProjects(this.currentUser.getId()));
  }

  @GetMapping("/active-projects")
  public ResponseEntity<ApiResponse<List<ProjectModel>>> getUsersActiveProjects(Authentication auth) {
    return ResponseFactory.ok("successfull", this.projectService.getActiveProjects(this.currentUser.getId()));
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<ProjectModel>> createProject(Authentication auth, @RequestBody CreateProjectDto requestData) {
    return ResponseFactory.created("Project Added", this.projectService.createProject(requestData, this.currentUser.getId()));
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<ApiResponse<Void>> deleteProject(Authentication auth, @PathVariable String projectId) {
    this.projectService.deleteProject(projectId, this.currentUser.getId());
    return ResponseFactory.ok("Project deleted");
  }

  @PutMapping("/update-env-data/{projectId}/")
  public ResponseEntity<ApiResponse<Void>> updateEnvData(@PathVariable String projectId,
      @RequestBody UpdateEnvDataDto requestData) {
    this.projectService.updateEnvData(projectId, requestData.envData(), this.currentUser.getId());
    return ResponseFactory.ok("successfull");
  }

  @GetMapping("/pull-env-data/{projectId}")
  public ResponseEntity<ApiResponse<String>> pullEnvData(@PathVariable String projectId) {
    return ResponseFactory.ok("successfull", this.projectService.pullEnvContent(projectId));
  }

  @PostMapping("/invite")
  public ResponseEntity<ApiResponse<Void>> sendInvitation(@RequestBody InviteUserDto data) throws Exception {
    this.projectService.sendInvitation(data);
    return ResponseFactory.ok("Invitation sent");
  }

  @PostMapping("/accept-invite")
  public ResponseEntity<ApiResponse<Void>> addCollaborator(@RequestBody AddCollaboratorDto data) {
    this.projectService.addCollaborator(data);
    return ResponseFactory.ok("User added as collaborator");
  }

  @GetMapping("/collabs/{projectId}")
  public ResponseEntity<ApiResponse<List<UserPublicDto>>> getCollabs(@PathVariable String projectId) {
    return ResponseFactory.ok("Collaborators fetched", this.projectService.getCollaboratorDetails(projectId));
  }

  @DeleteMapping("/{projectId}/collabs/{userId}")
  public ResponseEntity<ApiResponse<List<UserPublicDto>>> removeCollaborator(
      @PathVariable String projectId,
      @PathVariable String userId) {
    return ResponseFactory.ok("Collaborator deleted", this.projectService.removeCollaborator(projectId, userId));
  }
}
