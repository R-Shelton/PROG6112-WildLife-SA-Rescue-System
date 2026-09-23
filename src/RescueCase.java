public abstract class RescueCase implements RescueOperations {

    int rescueID = 0;
    String animalName = null;
    String species = null;
    String rescueLocation = null;
    String assignedRanger = null;
    int numberOfRescueDays = 0;
    double dailyCareCost = 0.0;
    String rescueStatus = null;
    String rescuePriority = null;

    public RescueCase(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger,
                      int numberOfRescueDays, double dailyCareCost, String rescueStatus, String rescuePriority) {
        this.rescueID = rescueID;
        this.animalName = animalName;
        this.species = species;
        this.rescueLocation = rescueLocation;
        this.assignedRanger = assignedRanger;
        this.numberOfRescueDays = numberOfRescueDays;
        this.dailyCareCost = dailyCareCost;
        this.rescueStatus = rescueStatus;
        this.rescuePriority = rescuePriority;
    }
}
