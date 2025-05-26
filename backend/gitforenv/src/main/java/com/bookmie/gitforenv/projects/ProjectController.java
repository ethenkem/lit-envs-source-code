package com.bookmie.gitforenv.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.gitforenv.projects.dtos.CreateProjectDto;
import com.bookmie.gitforenv.utils.dtos.ResponseDto;

@RestController
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  public ResponseEntity<ResponseDto> createProject(CreateProjectDto requestData) {
    ResponseDto response = this.projectService.createProject(requestData);
    return ResponseEntity.status(response.statusCode()).body(response);
  }
}
