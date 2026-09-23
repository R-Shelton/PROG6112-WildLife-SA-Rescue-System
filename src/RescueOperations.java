public interface RescueOperations {

    // Full cost of the rescue, including any type-specific costs and fees
    public double getTotalCost();

    // Moves the case from PENDING to IN_PROGRESS.
    // Throws IllegalStateException if the case is not PENDING.
    public void startRescue();

    // Moves the case from IN_PROGRESS to COMPLETED.
    // Throws IllegalStateException if the case is not IN_PROGRESS.
    public void completeRescue();

    // Prints a formatted summary of the case to the console, including a cost breakdown
    public void generateSummary();
}
