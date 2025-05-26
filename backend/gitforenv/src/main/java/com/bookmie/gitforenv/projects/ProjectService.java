package com.bookmie.gitforenv.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmie.gitforenv.projects.dtos.CreateProjectDto;
import com.bookmie.gitforenv.projects.models.ProjectModel;
import com.bookmie.gitforenv.projects.repos.ProjectRepo;
import com.bookmie.gitforenv.utils.dtos.ResponseDto;

@Service
public class ProjectService {

  @Autowired
  private ProjectRepo projectRepo;

  public ResponseDto createProject(CreateProjectDto data) {
    //if (this.projectRepo.findby)
    ProjectModel project = new ProjectModel(data.projectName(), data.describtion(), "");
    this.projectRepo.save(project);
    return new ResponseDto(201, "Project Added", null);
  }
}
