package logic.DTO;

public class ProjectRequestDTO {
    private int requestId;
    private String tuiton;
    private String organizationId;
    private String representativeId;
    private String projectId;
    private String description;
    private String generalObjective;
    private String immediateObjectives;
    private String mediateObjectives;
    private String methodology;
    private String resources;
    private String activities;
    private String responsibilities;
    private int duration;
    private String scheduleDays;
    private int directUsers;
    private int indirectUsers;
    private ProjectStatus status;
    private String requestDate;

    public ProjectRequestDTO() {
        this.requestId = 0;
        this.tuiton = "";
        this.organizationId = "";
        this.representativeId = "";
        this.projectId = "";
        this.description = "";
        this.generalObjective = "";
        this.immediateObjectives = "";
        this.mediateObjectives = "";
        this.methodology = "";
        this.resources = "";
        this.activities = "";
        this.responsibilities = "";
        this.duration = 420;
        this.scheduleDays = "";
        this.directUsers = 0;
        this.indirectUsers = 0;
        this.status = ProjectStatus.pendiente;
        this.requestDate = "";
    }

    public ProjectRequestDTO(int requestId, String tuiton, String organizationId, String representativeId, String projectName, String description, String generalObjective, String immediateObjectives, String mediateObjectives, String methodology, String resources, String activities, String responsibilities, int duration, String scheduleDays, int directUsers, int indirectUsers, String status, String requestDate) {
        this.requestId = requestId;
        this.tuiton = tuiton;
        this.organizationId = organizationId;
        this.representativeId = representativeId;
        this.projectId = projectName;
        this.description = description;
        this.generalObjective = generalObjective;
        this.immediateObjectives = immediateObjectives;
        this.mediateObjectives = mediateObjectives;
        this.methodology = methodology;
        this.resources = resources;
        this.activities = activities;
        this.responsibilities = responsibilities;
        this.duration = duration;
        this.scheduleDays = scheduleDays;
        this.directUsers = directUsers;
        this.indirectUsers = indirectUsers;
        this.status = ProjectStatus.valueOf(status);
        this.requestDate = requestDate;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public String getTuiton() { return tuiton; }
    public void setTuiton(String tuiton) { this.tuiton = tuiton; }
    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
    public String getRepresentativeId() { return representativeId; }
    public void setRepresentativeId(String representativeId) { this.representativeId = representativeId; }
    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGeneralObjective() { return generalObjective; }
    public void setGeneralObjective(String generalObjective) { this.generalObjective = generalObjective; }
    public String getImmediateObjectives() { return immediateObjectives; }
    public void setImmediateObjectives(String immediateObjectives) { this.immediateObjectives = immediateObjectives; }
    public String getMediateObjectives() { return mediateObjectives; }
    public void setMediateObjectives(String mediateObjectives) { this.mediateObjectives = mediateObjectives; }
    public String getMethodology() { return methodology; }
    public void setMethodology(String methodology) { this.methodology = methodology; }
    public String getResources() { return resources; }
    public void setResources(String resources) { this.resources = resources; }
    public String getActivities() { return activities; }
    public void setActivities(String activities) { this.activities = activities; }
    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getScheduleDays() { return scheduleDays; }
    public void setScheduleDays(String scheduleDays) { this.scheduleDays = scheduleDays; }
    public int getDirectUsers() { return directUsers; }
    public void setDirectUsers(int directUsers) { this.directUsers = directUsers; }
    public int getIndirectUsers() { return indirectUsers; }
    public void setIndirectUsers(int indirectUsers) { this.indirectUsers = indirectUsers; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    @Override
    public String toString() {
        return "ProjectRequestDTO{" +
                "requestId=" + requestId +
                ", tuiton='" + tuiton + '\'' +
                ", organizationId=" + organizationId +
                ", representativeId=" + representativeId +
                ", projectName='" + projectId + '\'' +
                ", description='" + description + '\'' +
                ", generalObjective='" + generalObjective + '\'' +
                ", immediateObjectives='" + immediateObjectives + '\'' +
                ", mediateObjectives='" + mediateObjectives + '\'' +
                ", methodology='" + methodology + '\'' +
                ", resources='" + resources + '\'' +
                ", activities='" + activities + '\'' +
                ", responsibilities='" + responsibilities + '\'' +
                ", duration=" + duration +
                ", scheduleDays='" + scheduleDays + '\'' +
                ", directUsers=" + directUsers +
                ", indirectUsers=" + indirectUsers +
                ", status=" + status +
                ", requestDate='" + requestDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProjectRequestDTO)) return false;
        ProjectRequestDTO that = (ProjectRequestDTO) obj;
        return requestId == that.requestId &&
                tuiton.equals(that.tuiton) &&
                organizationId == that.organizationId &&
                representativeId == that.representativeId &&
                projectId.equals(that.projectId) &&
                ((description == null && that.description == null) || (description != null && description.equals(that.description))) &&
                generalObjective.equals(that.generalObjective) &&
                immediateObjectives.equals(that.immediateObjectives) &&
                mediateObjectives.equals(that.mediateObjectives) &&
                methodology.equals(that.methodology) &&
                resources.equals(that.resources) &&
                activities.equals(that.activities) &&
                responsibilities.equals(that.responsibilities) &&
                duration == that.duration &&
                scheduleDays.equals(that.scheduleDays) &&
                directUsers == that.directUsers &&
                indirectUsers == that.indirectUsers &&
                status == that.status &&
                ((requestDate == null && that.requestDate == null) || (requestDate != null && requestDate.equals(that.requestDate)));
    }
}