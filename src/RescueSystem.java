import java.util.ArrayList;
import java.util.Scanner;

public class RescueSystem {

    private final ArrayList<RescueCase> rescueCases = new ArrayList<>();
    private final Scanner scanner;

    // IDs are generated rather than typed in, so every rescue case is guaranteed a unique one
    private int nextRescueID = 1;

    // Uses the same Scanner as main; two Scanners on System.in can each swallow input meant for the other
    public RescueSystem(Scanner scanner) {
        this.scanner = scanner;
    }

    // Captures a new rescue case from the user and adds it to the system.
    // Returns false if an invalid rescue type is chosen.
    public boolean createRescue() {
        System.out.println("Select rescue type:");
        System.out.println("1. Injured Animal");
        System.out.println("2. Orphaned Animal");
        System.out.println("3. Endangered Species");
        int rescueType = readInt("Rescue type: ");

        if (rescueType < 1 || rescueType > 3) {
            System.out.println("Invalid rescue type. No rescue case was created.");
            return false;
        }

        // Details shared by every rescue type. New cases always start as PENDING.
        int rescueID = nextRescueID++;
        String animalName = readText("Animal name: ");
        String species = readText("Species: ");
        String rescueLocation = readText("Rescue location: ");
        String assignedRanger = readText("Assigned ranger: ");
        int numberOfRescueDays = readInt("Number of rescue days: ");
        double dailyCareCost = readDouble("Daily care cost: R ");

        RescueCase rescueCase;

        if (rescueType == 1) {
            String injuryDescription = readText("Injury description: ");
            boolean surgeryRequired = readYesNo("Surgery required? (y/n): ");
            double vetTreatmentCost = readDouble("Vet treatment cost: R ");

            rescueCase = new InjuredAnimalRescue(rescueID, animalName, species, rescueLocation, assignedRanger,
                    numberOfRescueDays, dailyCareCost, RescueStatus.PENDING, surgeryRequired, injuryDescription, vetTreatmentCost);
        } else if (rescueType == 2) {
            int estimatedAgeInMonths = readInt("Estimated age in months: ");
            double feedingCost = readDouble("Feeding cost: R ");
            boolean fosterCareRequired = readYesNo("Foster care required? (y/n): ");

            rescueCase = new OrphanedAnimalRescue(rescueID, animalName, species, rescueLocation, assignedRanger,
                    numberOfRescueDays, dailyCareCost, RescueStatus.PENDING, estimatedAgeInMonths, feedingCost, fosterCareRequired);
        } else {
            String conservationClassification = readText("Conservation classification (e.g. Critically Endangered): ");
            double securityCost = readDouble("Security cost: R ");
            boolean specialistTeamRequired = readYesNo("Specialist team required? (y/n): ");

            // The priority argument is ignored; EndangeredSpeciesRescue always sets its priority to High
            rescueCase = new EndangeredSpeciesRescue(rescueID, animalName, species, rescueLocation, assignedRanger,
                    numberOfRescueDays, dailyCareCost, RescueStatus.PENDING, "High",
                    conservationClassification, securityCost, specialistTeamRequired);
        }

        rescueCases.add(rescueCase);
        System.out.println("Rescue case #" + rescueID + " created with status " + RescueStatus.PENDING + ".");
        return true;
    }

    // Finds a rescue case by ID and prints its summary
    public void searchRescueCase() {
        int rescueID = readInt("Enter rescue ID: ");
        RescueCase rescueCase = findRescueCase(rescueID);

        if (rescueCase == null) {
            System.out.println("No rescue case found with ID " + rescueID + ".");
            return;
        }

        rescueCase.generateSummary();
    }

    // Moves a rescue case to its next status by starting or completing it
    public void updateRescueCase() {
        int rescueID = readInt("Enter rescue ID: ");
        RescueCase rescueCase = findRescueCase(rescueID);

        if (rescueCase == null) {
            System.out.println("No rescue case found with ID " + rescueID + ".");
            return;
        }

        System.out.println("Current status: " + rescueCase.rescueStatus);
        System.out.println("1. Start Rescue");
        System.out.println("2. Complete Rescue");
        int choice = readInt("Select an option: ");

        // startRescue/completeRescue throw if the move isn't allowed, e.g. completing a rescue that never started
        try {
            switch (choice) {
                case 1  -> rescueCase.startRescue();
                case 2  -> rescueCase.completeRescue();
                default -> System.out.println("Invalid option. Rescue case was not updated.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    // Prints the full summary of every rescue case
    public void displayAllRescueCases() {
        if (rescueCases.isEmpty()) {
            System.out.println("No rescue cases have been recorded.");
            return;
        }

        for (RescueCase rescueCase : rescueCases) {
            rescueCase.generateSummary();
            System.out.println();
        }
    }

    // Prints totals across all rescue cases: how many are at each status and what they cost
    public void displayRescueReport() {
        if (rescueCases.isEmpty()) {
            System.out.println("No rescue cases have been recorded.");
            return;
        }

        int pendingCount = 0;
        int inProgressCount = 0;
        int completedCount = 0;
        double totalCost = 0;
        RescueCase mostExpensive = rescueCases.get(0);

        for (RescueCase rescueCase : rescueCases) {
            switch (rescueCase.rescueStatus) {
                case PENDING     -> pendingCount++;
                case IN_PROGRESS -> inProgressCount++;
                case COMPLETED   -> completedCount++;
            }

            totalCost += rescueCase.getTotalCost();

            if (rescueCase.getTotalCost() > mostExpensive.getTotalCost()) {
                mostExpensive = rescueCase;
            }
        }

        String border = "=".repeat(50);

        System.out.println(border);
        System.out.println(" RESCUE REPORT");
        System.out.println(border);
        for (RescueCase rescueCase : rescueCases) {
            rescueCase.generateSummary();
        }
        System.out.print(RescueCase.formatLine("Total Rescue Cases", rescueCases.size()));
        System.out.print(RescueCase.formatLine("Pending", pendingCount));
        System.out.print(RescueCase.formatLine("In Progress", inProgressCount));
        System.out.print(RescueCase.formatLine("Completed", completedCount));
        System.out.println("-".repeat(50));
        System.out.print(RescueCase.formatLine("Total Cost", RescueCase.formatCurrency(totalCost)));
        System.out.print(RescueCase.formatLine("Average Cost per Rescue", RescueCase.formatCurrency(totalCost / rescueCases.size())));
        System.out.print(RescueCase.formatLine("Most Expensive Rescue", "#" + mostExpensive.rescueID + " " + mostExpensive.animalName
                + " (" + RescueCase.formatCurrency(mostExpensive.getTotalCost()) + ")"));
        System.out.println(border);
    }

    // Returns the rescue case with the given ID, or null if there isn't one
    private RescueCase findRescueCase(int rescueID) {
        for (RescueCase rescueCase : rescueCases) {
            if (rescueCase.rescueID == rescueID) {
                return rescueCase;
            }
        }

        return null;
    }

    // The read methods below keep asking until the input is valid, so a typo can't crash the program

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(scanner.nextLine().trim());

                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Not a number; fall through to the message below
            }

            System.out.println("Please enter a whole number greater than 0.");
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                double value = Double.parseDouble(scanner.nextLine().trim());

                // isFinite rejects "NaN" and "Infinity", which parseDouble accepts
                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // Not a number; fall through to the message below
            }

            System.out.println("Please enter an amount of 0 or more, e.g. 1500.50");
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            }

            if (input.equals("n") || input.equals("no")) {
                return false;
            }

            System.out.println("Please enter y or n.");
        }
    }
}
