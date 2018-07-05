package unit.entity;

import java.util.List;

/**
 * Created by lei on 2018/7/5.
 */

public class ParReportInfo {
    private List<ReportInfo> Reports;

    public ParReportInfo() {
    }

    public List<ReportInfo> getReports() {
        return Reports;
    }

    public void setReports(List<ReportInfo> reports) {
        Reports = reports;
    }
}
