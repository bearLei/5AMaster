package unit.entity;

/**
 * Created by lei on 2018/6/22.
 */

public class Evidence {
    private String File;
    private String CreateTime;
    private int    FileType;

    public Evidence() {
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getFileType() {
        return FileType;
    }

    public void setFileType(int fileType) {
        FileType = fileType;
    }
}
