package bt.nhdcl.maintenancemicroservice.entity;
public class CombinedMaintenanceCostResult {
    private String academyId;
    private int year;
    private int month;
    private double totalCost;

    // Default constructor
    public CombinedMaintenanceCostResult() {
    }

    // Parameterized constructor
    public CombinedMaintenanceCostResult(String academyId, int year, int month, double totalCost) {
        this.academyId = academyId;
        this.year = year;
        this.month = month;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}

