package unit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import unit.entity.RolePower;

/**
 * Created by lei on 2018/6/4.
 * 用户登录后返回的基础信息
 */

public class UserInfo implements Serializable{
    private String UserName;
    private String RealName;
    private String CurrentRole;
    private int CurrentPersonType;
    private String Photo;
    private String DomainUID;
    private String DomainName;
    private String SchoolLogo;
    private ArrayList<RolePower> RolePowers;
    private String Token;

    public UserInfo() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getCurrentRole() {
        return CurrentRole;
    }

    public void setCurrentRole(String currentRole) {
        CurrentRole = currentRole;
    }

    public int getCurrentPersonType() {
        return CurrentPersonType;
    }

    public void setCurrentPersonType(int currentPersonType) {
        CurrentPersonType = currentPersonType;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getDomainUID() {
        return DomainUID;
    }

    public void setDomainUID(String domainUID) {
        DomainUID = domainUID;
    }

    public String getDomainName() {
        return DomainName;
    }

    public void setDomainName(String domainName) {
        DomainName = domainName;
    }

    public String getSchoolLogo() {
        return SchoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        SchoolLogo = schoolLogo;
    }

    public List<RolePower> getRolePowers() {
        return RolePowers;
    }

    public void setRolePowers(ArrayList<RolePower> rolePowers) {
        RolePowers = rolePowers;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
