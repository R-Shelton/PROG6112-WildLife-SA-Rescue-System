// Lifecycle states of a rescue case.
// A case only ever moves forward: PENDING -> IN_PROGRESS -> COMPLETED.
public enum RescueStatus {

    // The case has been logged, but no rescue team has been sent out yet
    PENDING("Pending"),

    // A rescue team is actively working the case
    IN_PROGRESS("In Progress"),

    // The rescue is finished and the case is closed
    COMPLETED("Completed");

    private final String displayName;

    RescueStatus(String displayName) {
        this.displayName = displayName;
    }

    // Returns true if next is the next step in the lifecycle
    public boolean canTransitionTo(RescueStatus next) {
        return switch (this) {
            case PENDING     -> next == IN_PROGRESS;
            case IN_PROGRESS -> next == COMPLETED;
            case COMPLETED   -> false;
        };
    }

    // Human-readable name, e.g. "In Progress", for console output
    @Override
    public String toString() {
        return displayName;
    }
}
