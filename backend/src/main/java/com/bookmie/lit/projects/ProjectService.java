package com.bookmie.lit.projects;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bookmie.lit.configs.services.EmailService;
import com.bookmie.lit.ops.OpsService;
import com.bookmie.lit.projects.dtos.AddCollaboratorDto;
import com.bookmie.lit.projects.dtos.CreateProjectDto;
import com.bookmie.lit.projects.dtos.InviteUserDto;
import com.bookmie.lit.users.UserModel;
import com.bookmie.lit.users.UserRepository;
import com.bookmie.lit.users.dtos.UserPublicDto;
import com.bookmie.lit.utils.EmailTemplateLoader;
import com.bookmie.lit.utils.exceptions.BadRequestException;
import com.bookmie.lit.utils.exceptions.DuplicateResourceException;
import com.bookmie.lit.utils.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepo projectRepo;
  private final OpsService operations;
  private final EmailService emailService;
  private final UserRepository usersRepo;

  public ProjectModel createProject(CreateProjectDto data, String userId) {
    if (this.projectRepo.findByProjectName(data.projectName()).isPresent()) {
      throw new DuplicateResourceException("Project with similar name already exists");
    }
    ProjectModel project = new ProjectModel(data.projectName(), data.description(), userId);
    project.addCollaborator(userId);
    return this.projectRepo.save(project);
  }

  public void deleteProject(String projectId, String userId) {
    if (this.projectRepo.findByIdAndOwner(projectId, userId).isPresent()) {
      this.projectRepo.deleteById(projectId);
      return;
    }
    throw new BadRequestException("No active Project with ID: " + projectId + " owned by current user");
  }

  public List<ProjectModel> getProjects(String userId) {
    return this.projectRepo.findByCollaboratorsContaining(userId);
  }

  public List<ProjectModel> getActiveProjects(String userId) {
    return this.projectRepo.findByCollaboratorsContaining(userId);
  }

  public String pullEnvContent(String projectId) {
    Optional<ProjectModel> project = this.projectRepo.findById(projectId);
    if (project.isEmpty()) {
      throw new ResourceNotFoundException("Project", projectId);
    }
    return this.operations.decryptEnvData(project.get().getDotEnvData());
  }

  public void updateEnvData(String projectId, String envData, String userId) {
    Optional<ProjectModel> project = this.projectRepo.findByIdAndOwner(projectId, userId);
    if (project.isPresent()) {
      ProjectModel projectObj = project.get();
      projectObj.setDotEnvData(this.operations.encryptEnvData(envData));
      this.projectRepo.save(projectObj);
      return;
    }
    throw new ResourceNotFoundException("Project", projectId);
  }

  public void sendInvitation(InviteUserDto request) throws Exception {
    ProjectModel project = this.projectRepo.findById(request.projectId())
        .orElseThrow(() -> new ResourceNotFoundException("Project", request.projectId()));
    UserModel user = this.usersRepo.findByEmail(request.email())
        .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

    if (project.getCollaborators().contains(user.getId())) {
      throw new BadRequestException("User is already a collaborator");
    }
    String inviteLink = String.format(
        "https://lit.bookmie.com/accept-invite?projectId=%s&userId=%s",
        project.getId(), user.getId());
    String html = EmailTemplateLoader.loadTemplate("invite.html");
    String msg = html.replace("{{inviteLink}}", inviteLink).replace("{{projectName}}", project.getProjectName());
    this.emailService.sendHtmlEmail(user.getEmail(), "Lit Envs Verification", msg);
  }

  public void addCollaborator(AddCollaboratorDto data) {
    ProjectModel project = this.projectRepo.findById(data.projectId())
        .orElseThrow(() -> new ResourceNotFoundException("Project", data.projectId()));
    if (project.getCollaborators().contains(data.userId())) {
      throw new BadRequestException("User is already a collaborator");
    }
    project.addCollaborator(data.userId());
    this.projectRepo.save(project);
  }

  public List<UserPublicDto> getCollaboratorDetails(String projectId) {
    ProjectModel project = this.projectRepo.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
    List<UserModel> users = this.usersRepo.findAllByIdIn(project.getCollaborators());
    return users.stream()
        .map(user -> new UserPublicDto(user.getId(), user.getEmail()))
        .toList();
  }

  public List<UserPublicDto> removeCollaborator(String projectId, String userId) {
    ProjectModel project = this.projectRepo.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
    if (!project.getCollaborators().contains(userId)) {
      throw new BadRequestException("User is not a collaborator");
    }
    project.getCollaborators().remove(userId);
    this.projectRepo.save(project);
    List<UserModel> users = this.usersRepo.findAllByIdIn(project.getCollaborators());
    return users.stream()
        .map(user -> new UserPublicDto(user.getId(), user.getEmail()))
        .toList();
  }
}
