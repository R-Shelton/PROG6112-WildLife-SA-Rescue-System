import java.util.Locale;
import java.util.Objects;

public abstract class RescueCase implements RescueOperations {

    // Width of the label column in summaries; fits the longest label, "Conservation Classification"
    private static final int LABEL_WIDTH = 28;
    private static final String SUMMARY_BORDER  = "=".repeat(50);
    private static final String SECTION_DIVIDER = "-".repeat(50);

    int rescueID;
    String animalName;
    String species;
    String rescueLocation;
    String assignedRanger;
    int numberOfRescueDays;
    double dailyCareCost;
    RescueStatus rescueStatus;
    String rescuePriority;

    public RescueCase(int rescueID, String animalName, String species, String rescueLocation, String assignedRanger,
                      int numberOfRescueDays, double dailyCareCost, RescueStatus rescueStatus, String rescuePriority) {
        this.rescueID = rescueID;
        this.animalName = animalName;
        this.species = species;
        this.rescueLocation = rescueLocation;
        this.assignedRanger = assignedRanger;
        this.numberOfRescueDays = numberOfRescueDays;
        this.dailyCareCost = dailyCareCost;
        // Status drives startRescue()/completeRescue(), so a null here would only fail later and less clearly.
        this.rescueStatus = Objects.requireNonNull(rescueStatus, "rescueStatus must not be null");
        this.rescuePriority = rescuePriority;
    }

    // Daily care cost across the whole rescue period, shared by every rescue type's total cost
    protected double getCareCost() {
        return dailyCareCost * numberOfRescueDays;
    }

    // Moves this case to the next status, enforcing the lifecycle defined in RescueStatus.
    // Throws IllegalStateException if the case cannot move from its current status to next.
    protected void transitionTo(RescueStatus next) {
        if (!rescueStatus.canTransitionTo(next)) {
            throw new IllegalStateException(String.format(
                    "Rescue #%d cannot move from %s to %s.", rescueID, rescueStatus, next));
        }

        rescueStatus = next;
    }

    // Summary formatting helpers. Each rescue type builds its own summary in generateSummary(),
    // and these helpers keep the layout identical across all types.

    // Title banner followed by the details every rescue type shares.
    // rescueType is the display name of the type, e.g. "Injured Animal Rescue".
    protected String formatSummaryHeader(String rescueType) {
        return String.format("%s%n %s - CASE #%d%n%s%n", SUMMARY_BORDER, rescueType.toUpperCase(), rescueID, SUMMARY_BORDER)
                + formatLine("Animal Name", animalName)
                + formatLine("Species", species)
                + formatLine("Rescue Location", rescueLocation)
                + formatLine("Assigned Ranger", assignedRanger)
                + formatLine("Status", rescueStatus)
                + formatLine("Priority", rescuePriority);
    }

    // Divider followed by the upper-cased section heading, e.g. "COST BREAKDOWN"
    protected String formatSectionHeading(String heading) {
        return String.format("%s%n %s%n", SECTION_DIVIDER, heading.toUpperCase());
    }

    // Cost breakdown line for daily care, showing how it was calculated,
    // e.g. "5 days x R 1,200.00 = R 6,000.00"
    protected String formatCareCostLine() {
        String dayLabel = (numberOfRescueDays == 1) ? "day" : "days";

        return formatLine("Daily Care", String.format("%d %s x %s = %s",
                numberOfRescueDays, dayLabel, formatCurrency(dailyCareCost), formatCurrency(getCareCost())));
    }

    // Total cost followed by the bottom border
    protected String formatSummaryFooter() {
        return SECTION_DIVIDER + System.lineSeparator()
                + formatLine("TOTAL COST", formatCurrency(getTotalCost()))
                + SUMMARY_BORDER + System.lineSeparator();
    }

    // One aligned "label : value" line; missing values are shown as "N/A"
    protected static String formatLine(String label, Object value) {
        return String.format(" %-" + LABEL_WIDTH + "s: %s%n", label, Objects.toString(value, "N/A"));
    }

    // Formats an amount in South African Rand, e.g. "R 14,500.00".
    // The locale is fixed so the output does not change with the machine's regional settings.
    protected static String formatCurrency(double amount) {
        return String.format(Locale.ENGLISH, "R %,.2f", amount);
    }

    protected static String formatYesNo(boolean value) {
        return value ? "Yes" : "No";
    }
}
