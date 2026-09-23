public class InjuredAnimalRescue extends RescueCase {

    // Flat fee added to the total cost when the animal needs surgery
    private static final double SURGERY_FEE = 5000;

    private boolean surgeryRequired;
    private String injuryDescription;
    private double vetTreatmentCost;

    public InjuredAnimalRescue(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger,
                               int numberOfRescueDays, double dailyCareCost, RescueStatus rescueStatus, boolean surgeryRequired, String injuryDescription, double vetTreatmentCost) {

        super(rescueID, animalName, species, rescueLocation, assignedRanger, numberOfRescueDays, dailyCareCost, rescueStatus, "High");

        this.surgeryRequired = surgeryRequired;
        this.injuryDescription = injuryDescription;
        this.vetTreatmentCost = vetTreatmentCost;
    }

    @Override
    public double getTotalCost() {
        double total = getCareCost() + vetTreatmentCost;

        if (surgeryRequired) {
            total = total + SURGERY_FEE;
        }

        return total;
    }

    // Sends the veterinary team out and moves the case to IN_PROGRESS.
    // Throws IllegalStateException if the case is not PENDING.
    @Override
    public void startRescue() {
        transitionTo(RescueStatus.IN_PROGRESS);

        System.out.printf("Rescue #%d started: veterinary team dispatched to %s to treat %s the %s (%s).%n",
                rescueID, rescueLocation, animalName, species, injuryDescription);

        if (surgeryRequired) {
            System.out.println("Surgery has been scheduled.");
        }
    }

    // Closes the case once treatment is done and moves it to COMPLETED.
    // Throws IllegalStateException if the case is not IN_PROGRESS.
    @Override
    public void completeRescue() {
        transitionTo(RescueStatus.COMPLETED);

        System.out.printf("Rescue #%d completed: %s the %s has been treated and is ready for release. Total cost: %s%n",
                rescueID, animalName, species, formatCurrency(getTotalCost()));
    }

    // Prints the case details, the injury details and a cost breakdown ending with the total cost
    @Override
    public void generateSummary() {
        String summary = formatSummaryHeader("Injured Animal Rescue")
                + formatSectionHeading("Rescue Details")
                + formatLine("Rescue Case ID", this.rescueID)
                + formatLine("Rescue Type", "Endangered Species")
                + formatLine("Rescue Priority", this.rescuePriority)
                + formatLine("Rescue Location", this.rescueLocation)
                + formatLine("Species", this.species)
                + formatLine("Assigned Ranger", this.assignedRanger)
                + formatLine("Current Status", this.rescueStatus)
                + formatSectionHeading("Injury Details")
                + formatLine("Injury Description", injuryDescription)
                + formatLine("Surgery Required", formatYesNo(surgeryRequired))
                + formatSectionHeading("Cost Breakdown")
                + formatCareCostLine()
                + formatLine("Vet Treatment", formatCurrency(vetTreatmentCost))
                + formatLine("Surgery Fee", formatCurrency(surgeryRequired ? SURGERY_FEE : 0))
                + formatSummaryFooter();

        System.out.print(summary);
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
