public class InjuredAnimalRescue extends RescueCase {

    private boolean surgeryRequired;
    private String injuryDescription;
    private double vetTreatmentCost;

    public InjuredAnimalRescue(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger, int numberOfRescueDays, double dailyCareCost, String rescueStatus, boolean surgeryRequired, String injuryDescription, double vetTreatmentCost) {
        super(rescueID, animalName, species, rescueLocation, assignedRanger, numberOfRescueDays, dailyCareCost, rescueStatus);
        this.surgeryRequired = surgeryRequired;
        this.injuryDescription = injuryDescription;
        this.vetTreatmentCost = vetTreatmentCost;
    }

    @Override
    public double getTotalCost() {
        double total = this.dailyCareCost * this.numberOfRescueDays + vetTreatmentCost;

        if (surgeryRequired) {
            total = total + 5000 ;
        }

        return total;
    }

    public boolean isSurgeryRequired() {
        return surgeryRequired;
    }

    public String getInjuryDescription() {
        return injuryDescription;
    }

    public double getVetTreatmentCost() {
        return vetTreatmentCost;
    }

    public void setSurgeryRequired(boolean surgeryRequired) {
        this.surgeryRequired = surgeryRequired;
    }

    public void setInjuryDescription(String injuryDescription) {
        this.injuryDescription = injuryDescription;
    }

    public void setVetTreatmentCost(double vetTreatmentCost) {
        this.vetTreatmentCost = vetTreatmentCost;
    }
}
