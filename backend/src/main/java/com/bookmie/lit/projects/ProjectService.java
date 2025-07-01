package com.bookmie.lit.projects;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookmie.lit.ops.OpsService;
import com.bookmie.lit.projects.dtos.AddCollaboratorDto;
import com.bookmie.lit.projects.dtos.CreateProjectDto;
import com.bookmie.lit.utils.dtos.ResponseDto;

@Service
public class ProjectService {

  @Autowired
  private ProjectRepo projectRepo;

  @Autowired
  private OpsService operations;

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
    List<ProjectModel> projects = this.projectRepo.findByCollaboratorsContaining(userId);
    return new ResponseDto(200, "successfull", projects);
  }

  public ResponseDto pullEnvContent(String projectId) {
    Optional<ProjectModel> project = this.projectRepo.findById(projectId);
    String decryptedEnv = this.operations.decryptEnvData(project.get().getDotEnvData());
    return new ResponseDto(200, "successfull", decryptedEnv);
  }

  public ResponseDto updateEnvData(String projectId, String envData, String userId) {
    Optional<ProjectModel> project = this.projectRepo.findByIdAndOwner(projectId, userId);
    if (project.isPresent()) {
      ProjectModel projectObj = project.get();
      String securedData = this.operations.encryptEnvData(envData);
      projectObj.setDotEnvData(securedData);
      this.projectRepo.save(projectObj);
      return new ResponseDto(200, "successfull", null);
    }
    return new ResponseDto(404, "not found", null);
  }

  public ResponseDto addCollaborator(AddCollaboratorDto data) {
    Optional<ProjectModel> project = this.projectRepo.findById(data.projectId());
    if (project.isEmpty()) {
      return new ResponseDto(404, "project not found", null);
    }
    ProjectModel projectObj = project.get();
    projectObj.addCollaborator(data.userId());
    this.projectRepo.save(projectObj);
    return new ResponseDto(200, "user added", null);
  }
}
