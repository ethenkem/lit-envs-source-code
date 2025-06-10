package com.bookmie.gitforenv.projects;

import java.util.List;
import java.util.Optional;

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

  public ResponseDto createProject(CreateProjectDto data, String userId) {
    if (this.projectRepo.findByProjectName(data.projectName()).isPresent()) {
      return new ResponseDto(409, "Project with similar name already exists", null);
    }
    ProjectModel project = new ProjectModel(data.projectName(), data.description(), userId);
    this.projectRepo.save(project);
    return new ResponseDto(201, "Project Added", null);
  }

  public ResponseDto deleteProject(String projectId, String userId) {
    if (this.projectRepo.findByIdAndOwner(projectId, userId).isPresent()) {
      this.projectRepo.deleteById(projectId);
      return new ResponseDto(200, "Project deleted", null);
    }
    return new ResponseDto(400, "No active Project", null);
  }

  public ResponseDto getProjects(String userId) {
    List<ProjectModel> projects = this.projectRepo.findByOwner(userId);
    return new ResponseDto(200, "successfull", projects);
  }

  public ResponseDto updateEnvData(String projectId, String envData, String userId) {
    Optional<ProjectModel> project = this.projectRepo.findByIdAndOwner(projectId, userId);
    if (project.isPresent()) {
      ProjectModel projectObj = project.get();
      projectObj.setDotEnvData(envData);
      this.projectRepo.save(projectObj);
      return new ResponseDto(200, "successfull", null);
    }
    return new ResponseDto(404, "not found", null);
  }
}
