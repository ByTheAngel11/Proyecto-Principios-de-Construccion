package logic.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import logic.DTO.RepresentativeDTO;

public interface IRepresentativeDAO {
    boolean insertRepresentative(RepresentativeDTO representative) throws SQLException;

    boolean updateRepresentative(RepresentativeDTO representative) throws SQLException;

    boolean deleteRepresentative(String idRepresentative) throws SQLException;

    RepresentativeDTO searchRepresentativeById(String idRepresentative) throws SQLException;

    boolean isRepresentativeRegistered(String idRepresentative) throws SQLException;

    boolean isRepresentativeEmailRegistered(String email) throws SQLException;

    RepresentativeDTO searchRepresentativeByFullname(String names, String surnames) throws SQLException;

    String getRepresentativeNameById(String idRepresentative) throws SQLException;

    List<RepresentativeDTO> getAllRepresentatives() throws SQLException;

    List<RepresentativeDTO> getRepresentativesByDepartment(String idDepartment) throws SQLException;

    List<RepresentativeDTO> getRepresentativesByOrganization(String idOrganization) throws SQLException;
}
