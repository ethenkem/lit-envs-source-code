package com.bookmie.gitforenv.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.gitforenv.projects.dtos.CreateProjectDto;
import com.bookmie.gitforenv.utils.dtos.ResponseDto;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createProject(CreateProjectDto requestData) {
    ResponseDto response = this.projectService.createProject(requestData);
    return ResponseEntity.status(response.statusCode()).body(response);
  }
}
