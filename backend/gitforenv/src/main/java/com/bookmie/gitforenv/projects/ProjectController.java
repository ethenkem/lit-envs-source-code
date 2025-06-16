package com.bookmie.gitforenv.projects;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.gitforenv.auths.models.UserModel;
import com.bookmie.gitforenv.configs.components.CurrentUser;
import com.bookmie.gitforenv.projects.dtos.CreateProjectDto;
import com.bookmie.gitforenv.projects.dtos.UpdateEnvDataDto;
import com.bookmie.gitforenv.projects.models.ProjectModel;
import com.bookmie.gitforenv.utils.dtos.ResponseDto;


@RestController
@RequestMapping("/projects")
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private CurrentUser currentUser;

  @GetMapping("/")
  public ResponseEntity<ResponseDto> getProjects(Authentication auth) {
    ResponseDto res = this.projectService.getProjects(this.currentUser.getId());
    return ResponseEntity.status(res.statusCode()).body(res);
  }

  @GetMapping("/active-projects")
  public ResponseEntity<ResponseDto> getUsersActiveProjects(Authentication auth) {
    ResponseDto res = this.projectService.getProjects(this.currentUser.getId());
    return ResponseEntity.status(res.statusCode()).body(res);
  }

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createProject(Authentication auth, @RequestBody CreateProjectDto requestData) {
    ResponseDto response = this.projectService.createProject(requestData, this.currentUser.getId());
    return ResponseEntity.status(response.statusCode()).body(response);
  }

  @DeleteMapping("/delete/")
  public ResponseEntity<ResponseDto> deleteProject(Authentication auth, @RequestParam String projectId) {
    ResponseDto response = this.projectService.deleteProject(projectId, this.currentUser.getId());
    return ResponseEntity.status(response.statusCode()).body(response);
  }

  @PutMapping("/update-env-data/{projectId}/")
  public ResponseEntity<ResponseDto> updateEnvData(@PathVariable String projectId,
      @RequestBody UpdateEnvDataDto requestData) {
    ResponseDto response = this.projectService.updateEnvData(projectId, requestData.envData(),
        this.currentUser.getId());
    return ResponseEntity.status(response.statusCode()).body(response);
  }

  @GetMapping("/pull-env-data/{projectId}")
  public ResponseEntity<ResponseDto> pullEnvData(@PathVariable String projectId) {
    ResponseDto res = this.projectService.pullEnvContent(projectId);
    return ResponseEntity.status(res.statusCode()).body(res);
  }
}
