package com.bookmie.lit.projects;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.bookmie.lit.utils.dtos.ApiResponse;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private CurrentUser currentUser;

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<ProjectModel>>> getProjects(Authentication auth) {
    List<ProjectModel> projects = this.projectService.getProjects(this.currentUser.getId());
    ApiResponse<List<ProjectModel>> response = ApiResponse.<List<ProjectModel>>builder()
        .success(true)
        .message("successfull")
        .data(projects)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/active-projects")
  public ResponseEntity<ApiResponse<List<ProjectModel>>> getUsersActiveProjects(Authentication auth) {
    List<ProjectModel> projects = this.projectService.getActiveProjects(this.currentUser.getId());
    ApiResponse<List<ProjectModel>> response = ApiResponse.<List<ProjectModel>>builder()
        .success(true)
        .message("successfull")
        .data(projects)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<ProjectModel>> createProject(Authentication auth, @RequestBody CreateProjectDto requestData) {
    ProjectModel project = this.projectService.createProject(requestData, this.currentUser.getId());
    ApiResponse<ProjectModel> response = ApiResponse.<ProjectModel>builder()
        .success(true)
        .message("Project Added")
        .data(project)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<ApiResponse<Void>> deleteProject(Authentication auth, @PathVariable String projectId) {
    this.projectService.deleteProject(projectId, this.currentUser.getId());
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(true)
        .message("Project deleted")
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update-env-data/{projectId}/")
  public ResponseEntity<ApiResponse<Void>> updateEnvData(@PathVariable String projectId,
      @RequestBody UpdateEnvDataDto requestData) {
    this.projectService.updateEnvData(projectId, requestData.envData(),
        this.currentUser.getId());
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(true)
        .message("successfull")
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/pull-env-data/{projectId}")
  public ResponseEntity<ApiResponse<String>> pullEnvData(@PathVariable String projectId) {
    String decryptedEnv = this.projectService.pullEnvContent(projectId);
    ApiResponse<String> response = ApiResponse.<String>builder()
        .success(true)
        .message("successfull")
        .data(decryptedEnv)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/invite")
  public ResponseEntity<ApiResponse<Void>> pullEnvData(@RequestBody InviteUserDto data) throws Exception {
    this.projectService.sendInvitation(data);
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(true)
        .message("Invitation sent")
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/accept-invite")
  public ResponseEntity<ApiResponse<Void>> addCollaborator(@RequestBody AddCollaboratorDto data) {
    this.projectService.addCollaborator(data);
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(true)
        .message("User added as collaborator")
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/collabs/{projectId}")
  public ResponseEntity<ApiResponse<List<UserPublicDto>>> getCollabs(@PathVariable String projectId) {
    List<UserPublicDto> collabs = this.projectService.getCollaboratorDetails(projectId);
    ApiResponse<List<UserPublicDto>> response = ApiResponse.<List<UserPublicDto>>builder()
        .success(true)
        .message("Collaborators fetched")
        .data(collabs)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{projectId}/collabs/{userId}")
  public ResponseEntity<ApiResponse<List<UserPublicDto>>> removeCollaborator(
      @PathVariable String projectId,
      @PathVariable String userId) {
    List<UserPublicDto> collabs = projectService.removeCollaborator(projectId, userId);
    ApiResponse<List<UserPublicDto>> response = ApiResponse.<List<UserPublicDto>>builder()
        .success(true)
        .message("Collaborator deleted")
        .data(collabs)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }
}
