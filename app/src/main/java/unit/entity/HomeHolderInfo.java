package unit.entity;

/**
 * Created by lei on 2018/6/6.
 * 首页holder展示的元素
 */

public class HomeHolderInfo {
    private int iconRes;
    private int titleRes;
    private boolean showRedDog;

    public HomeHolderInfo() {
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public void setTitleRes(int titleRes) {
        this.titleRes = titleRes;
    }

    public boolean isShowRedDog() {
        return showRedDog;
    }

    public void setShowRedDog(boolean showRedDog) {
        this.showRedDog = showRedDog;
    }
}
