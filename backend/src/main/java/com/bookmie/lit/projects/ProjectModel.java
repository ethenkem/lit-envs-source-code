package com.bookmie.lit.projects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.Nullable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Document(collection = "projects")
public class ProjectModel {
  @Id
  private String id;

  @Field(name = "project_name")
  @Indexed(unique = true)
  private String projectName;

  private String describtion;

  @Field(name = "dot_env_data")
  @Nullable
  private String dotEnvData;

  @Field(name = "owner")
  private String owner;
  //
  @Field(name = "created_on")
  @CreatedDate
  private Instant createdOn;

  @Field(name = "last_updated")
  private Instant lastUpdated;

  @Field(name = "member_ids")
  private List<String> members = new ArrayList<String>();

  public ProjectModel(String projectName, String describtion, String owner) {
    this.projectName = projectName;
    this.describtion = describtion;
    this.owner = owner;
  }

  public String getId() {
    return id;
  }

  public List<String> getMembers() {
    return members;
  }

  public String getDotEnvData() {
    return dotEnvData;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setDotEnvData(String dotEnvData) {
    this.dotEnvData = dotEnvData;
  }

}
