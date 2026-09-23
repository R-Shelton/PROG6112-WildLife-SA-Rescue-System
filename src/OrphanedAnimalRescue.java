public class OrphanedAnimalRescue extends RescueCase {

    // Flat fee added to the total cost when the animal is placed with a foster carer
    private static final double FOSTER_CARE_FEE = 2500;

    private int estimatedAgeInMonths;
    private double feedingCost;
    private boolean fosterCareRequired;

    public OrphanedAnimalRescue(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger,
                                int numberOfRescueDays, double dailyCareCost, RescueStatus rescueStatus, int estimatedAgeInMonths,
                                double feedingCost, boolean fosterCareRequired) {

        super(rescueID, animalName, species, rescueLocation, assignedRanger, numberOfRescueDays, dailyCareCost, rescueStatus, "Medium");

        this.estimatedAgeInMonths = estimatedAgeInMonths;
        this.feedingCost = feedingCost;
        this.fosterCareRequired = fosterCareRequired;
    }

    @Override
    public double getTotalCost() {
        double total = getCareCost() + feedingCost;

        if (fosterCareRequired) {
            total = total + FOSTER_CARE_FEE;
        }

        return total;
    }

    // Sends the assigned ranger to collect the animal and moves the case to IN_PROGRESS.
    // Throws IllegalStateException if the case is not PENDING.
    @Override
    public void startRescue() {
        transitionTo(RescueStatus.IN_PROGRESS);

        System.out.printf("Rescue #%d started: %s is collecting %s the %s (approx. %s old) from %s.%n",
                rescueID, assignedRanger, animalName, species, formatAge(), rescueLocation);

        if (fosterCareRequired) {
            System.out.println("A foster carer has been arranged.");
        } else {
            System.out.println("The animal will be hand-reared at the rescue centre.");
        }
    }

    // Closes the case once the animal has been placed and moves it to COMPLETED.
    // Throws IllegalStateException if the case is not IN_PROGRESS.
    @Override
    public void completeRescue() {
        transitionTo(RescueStatus.COMPLETED);

        String outcome = fosterCareRequired
                ? "has been placed with a foster carer"
                : "has been hand-reared and is ready for rehabilitation";

        System.out.printf("Rescue #%d completed: %s the %s %s. Total cost: %s%n",
                rescueID, animalName, species, outcome, formatCurrency(getTotalCost()));
    }

    // Prints the case details, the care details and a cost breakdown ending with the total cost
    @Override
    public void generateSummary() {
        String summary = formatSummaryHeader("Orphaned Animal Rescue")
                + formatSectionHeading("Rescue Details")
                + formatLine("Rescue Case ID", this.rescueID)
                + formatLine("Rescue Type", "Endangered Species")
                + formatLine("Rescue Priority", this.rescuePriority)
                + formatLine("Rescue Location", this.rescueLocation)
                + formatLine("Species", this.species)
                + formatLine("Assigned Ranger", this.assignedRanger)
                + formatLine("Current Status", this.rescueStatus)
                + formatSectionHeading("Care Details")
                + formatLine("Estimated Age", formatAge())
                + formatLine("Foster Care Required", formatYesNo(fosterCareRequired))
                + formatSectionHeading("Cost Breakdown")
                + formatCareCostLine()
                + formatLine("Feeding", formatCurrency(feedingCost))
                + formatLine("Foster Care Fee", formatCurrency(fosterCareRequired ? FOSTER_CARE_FEE : 0))
                + formatSummaryFooter();

        System.out.print(summary);
    }

    // Estimated age in readable form, e.g. "1 month" or "8 months"
    private String formatAge() {
        return estimatedAgeInMonths + ((estimatedAgeInMonths == 1) ? " month" : " months");
    }

    public int getEstimatedAgeInMonths() {
        return estimatedAgeInMonths;
    }

    public void setEstimatedAgeInMonths(int estimatedAgeInMonths) {
        this.estimatedAgeInMonths = estimatedAgeInMonths;
    }

    public double getFeedingCost() {
        return feedingCost;
    }

    public void setFeedingCost(double feedingCost) {
        this.feedingCost = feedingCost;
    }

    public boolean isFosterCareRequired() {
        return fosterCareRequired;
    }

    public void setFosterCareRequired(boolean fosterCareRequired) {
        this.fosterCareRequired = fosterCareRequired;
    }
}
