package logic.DTO;

public class UserDTO {
    private String idUser;
    private int state;
    private String numberOffStaff;
    private String names;
    private String surname;
    private String userName;
    private String password;
    private Role role;

    public UserDTO (String s) {
        this.idUser = "";
        this.state = 1;
        this.numberOffStaff = "";
        this.names = "";
        this.surname = "";
        this.userName = "";
        this.password = "";
        this.role = null;
    }

    public UserDTO(String idUser, String numberOffStaff, String names, String surname, String userName, String password, Role role) {
        this.idUser = idUser;
        this.state = 1;
        this.numberOffStaff = numberOffStaff;
        this.names = names;
        this.surname = surname;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNumberOffStaff() {
        return numberOffStaff;
    }

    public void setNumberOffStaff(String numberOffStaff) {
        this.numberOffStaff = numberOffStaff;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //TODO Métodos para realizar las acciones basadas en el rol
    public void performRoleAction1() {
        if (role != null) {
            role.performAction1();
        } else {
            System.out.println("Rol no asignado");
        }
    }

    public void performRoleAction2() {
        if (role != null) {
            role.performAction2();
        } else {
            System.out.println("Rol no asignado");
        }
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "idUser='" + idUser + '\'' +
                ", state=" + state +
                ", numberOffStaff='" + numberOffStaff + '\'' +
                ", names='" + names + '\'' +
                ", surname='" + surname + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserDTO userDTO = (UserDTO) obj;

        if (state != userDTO.state) return false;
        if (!idUser.equals(userDTO.idUser)) return false;
        if (!numberOffStaff.equals(userDTO.numberOffStaff)) return false;
        if (!names.equals(userDTO.names)) return false;
        if (!surname.equals(userDTO.surname)) return false;
        if (!userName.equals(userDTO.userName)) return false;
        if (!password.equals(userDTO.password)) return false;
        return role != null ? role.equals(userDTO.role) : userDTO.role == null;
    }
}