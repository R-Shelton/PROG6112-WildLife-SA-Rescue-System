public class EndangeredSpeciesRescue extends RescueCase {

    private String conservationClassification;
    private double securityCost;
    private boolean specialistTeamRequired;

    public EndangeredSpeciesRescue(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger, int numberOfRescueDays, double dailyCareCost, String rescueStatus, String conservationClassification, double securityCost, boolean specialistTeamRequired) {
        super(rescueID, animalName, species, rescueLocation, assignedRanger, numberOfRescueDays, dailyCareCost, rescueStatus);
        this.conservationClassification = conservationClassification;
        this.securityCost = securityCost;
        this.specialistTeamRequired = specialistTeamRequired;
    }

    @Override
    public double getTotalCost() {
        double total = this.dailyCareCost * this.numberOfRescueDays + securityCost;

        if (specialistTeamRequired) {
            total = total + 5000 ;
        }

        return total;
    }

    public String getConservationClassification() {
        return conservationClassification;
    }

    public void setConservationClassification(String conservationClassification) {
        this.conservationClassification = conservationClassification;
    }

    public double getSecurityCost() {
        return securityCost;
    }

    public void setSecurityCost(double securityCost) {
        this.securityCost = securityCost;
    }

    public boolean isSpecialistTeamRequired() {
        return specialistTeamRequired;
    }

    public void setSpecialistTeamRequired(boolean specialistTeamRequired) {
        this.specialistTeamRequired = specialistTeamRequired;
    }
}
