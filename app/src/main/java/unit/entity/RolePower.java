package unit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2018/6/4.
 * 角色信息
 */

public class RolePower {
    private String RoleUID;
    private String RoleName;
    private int PersonnelType;
    private ArrayList<Power> Powers;
    public RolePower() {

    }

    public String getRoleUID() {
        return RoleUID;
    }

    public void setRoleUID(String roleUID) {
        RoleUID = roleUID;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public int getPersonnelType() {
        return PersonnelType;
    }

    public void setPersonnelType(int personnelType) {
        PersonnelType = personnelType;
    }

    public List<Power> getPowers() {
        return Powers;
    }

    public void setPowers(ArrayList<Power> powers) {
        Powers = powers;
    }
}
