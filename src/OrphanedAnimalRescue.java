public class OrphanedAnimalRescue extends RescueCase {

    private int estimatedAgeInMonths;
    private double feedingCost;
    private boolean fosterCareRequired;

    public OrphanedAnimalRescue(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger, int numberOfRescueDays, double dailyCareCost, String rescueStatus) {
        super(rescueID, animalName, species, rescueLocation, assignedRanger, numberOfRescueDays, dailyCareCost, rescueStatus);
    }

    @Override
    public double getTotalCost() {
        double total = this.dailyCareCost * this.numberOfRescueDays + feedingCost;

        if (fosterCareRequired) {
            total = total + 2500 ;
        }

        return total;
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
