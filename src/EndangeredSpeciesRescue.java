public class EndangeredSpeciesRescue extends RescueCase {

    // Flat fee added to the total cost when a specialist team is called in
    private static final double SPECIALIST_TEAM_FEE = 8000;

    private String conservationClassification;
    private double securityCost;
    private boolean specialistTeamRequired;

    public EndangeredSpeciesRescue(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger,
                                   int numberOfRescueDays, double dailyCareCost, RescueStatus rescueStatus, String rescuePriority,
                                   String conservationClassification, double securityCost, boolean specialistTeamRequired) {

        super(rescueID, animalName, species, rescueLocation, assignedRanger, numberOfRescueDays, dailyCareCost, rescueStatus, "High");

        this.conservationClassification = conservationClassification;
        this.securityCost = securityCost;
        this.specialistTeamRequired = specialistTeamRequired;
    }

    @Override
    public double getTotalCost() {
        double total = getCareCost() + securityCost;

        if (specialistTeamRequired) {
            total = total + SPECIALIST_TEAM_FEE;
        }

        return total;
    }

    // Sends a security detail to protect the animal and moves the case to IN_PROGRESS.
    // Throws IllegalStateException if the case is not PENDING.
    @Override
    public void startRescue() {
        transitionTo(RescueStatus.IN_PROGRESS);

        System.out.printf("Rescue #%d started: security detail deployed to protect %s the %s (%s) at %s.%n",
                rescueID, animalName, species, conservationClassification, rescueLocation);

        if (specialistTeamRequired) {
            System.out.println("A specialist team has been called in.");
        }
    }

    // Closes the case once the animal is safe and moves it to COMPLETED.
    // Throws IllegalStateException if the case is not IN_PROGRESS.
    @Override
    public void completeRescue() {
        transitionTo(RescueStatus.COMPLETED);

        System.out.printf("Rescue #%d completed: %s the %s is safe and the case has been logged for conservation records. Total cost: %s%n",
                rescueID, animalName, species, formatCurrency(getTotalCost()));
    }

    // Prints the case details, the conservation details and a cost breakdown ending with the total cost
    @Override
    public void generateSummary() {
        String summary = formatSummaryHeader("Endangered Species Rescue")
                + formatSectionHeading("Rescue Details")
                + formatLine("Rescue Case ID", this.rescueID)
                + formatLine("Rescue Type", "Endangered Species")
                + formatLine("Rescue Priority", this.rescuePriority)
                + formatLine("Rescue Location", this.rescueLocation)
                + formatLine("Species", this.species)
                + formatLine("Assigned Ranger", this.assignedRanger)
                + formatLine("Current Status", this.rescueStatus)
                + formatSectionHeading("Conservation Details")
                + formatLine("Conservation Classification", this.conservationClassification)
                + formatLine("Specialist Team Required", formatYesNo(this.specialistTeamRequired))
                + formatSectionHeading("Cost Breakdown")
                + formatCareCostLine()
                + formatLine("Security", formatCurrency(this.securityCost))
                + formatLine("Specialist Team Fee", formatCurrency(this.specialistTeamRequired ? SPECIALIST_TEAM_FEE : 0))
                + formatSummaryFooter();

        System.out.print(summary);
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
