package unit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2018/6/4.
 * 角色能力
 */

public class Power {
    private String Title;
    private ArrayList<Menu> Menus;

    public Power() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<Menu> getMenus() {
        return Menus;
    }

    public void setMenus(ArrayList<Menu> menus) {
        Menus = menus;
    }
}
