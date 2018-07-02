package unit.entity;

/**
 * Created by lei on 2018/7/2.
 */

public class UpLoadInfo {
    private String fileuid;
    private int type;
    private String url;

    public UpLoadInfo() {
    }

    public String getFileuid() {
        return fileuid;
    }

    public void setFileuid(String fileuid) {
        this.fileuid = fileuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
