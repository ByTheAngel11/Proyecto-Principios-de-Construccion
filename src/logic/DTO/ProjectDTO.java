package logic.DTO;

import java.sql.Timestamp;

public class ProjectDTO {
    private String idProject;
    private String name;
    private String description;
    private Timestamp approximateDate;
    private Timestamp startDate;
    private String idUser;

    public ProjectDTO() {
        this.idProject = "";
        this.name = "";
        this.description = "";
        this.approximateDate = null;
        this.startDate = null;
        this.idUser = "";
    }

    public ProjectDTO(String idProject, String name, String description, Timestamp approximateDate, Timestamp startDate, String idUser) {
        this.idProject = idProject;
        this.name = name;
        this.description = description;
        this.approximateDate = approximateDate;
        this.startDate = startDate;
        this.idUser = idUser;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getApproximateDate() {
        return approximateDate;
    }

    public void setApproximateDate(Timestamp approximateDate) {
        this.approximateDate = approximateDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "idProject='" + idProject + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", approximateDate=" + approximateDate +
                ", startDate=" + startDate +
                ", idUser='" + idUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ProjectDTO that = (ProjectDTO) obj;

        if (!idProject.equals(that.idProject)) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (!approximateDate.equals(that.approximateDate)) return false;
        if (!startDate.equals(that.startDate)) return false;
        return idUser.equals(that.idUser);
    }
}
