package gds;

public class DistributionStatus {
    private final String statusName;
    private final String description;

    public DistributionStatus(String statusName, String description) {
        this.statusName = statusName;
        this.description = description;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getDescription() {
        return description;
    }
}
