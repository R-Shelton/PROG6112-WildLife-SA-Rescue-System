import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the three rescue types: total cost calculations, rescue priorities and
// status updates (PENDING -> IN_PROGRESS -> COMPLETED).
// RescueCase has no getters for its status or priority, so these tests read the package-private
// fields directly. That works because the tests and the classes share the default package.
class RescueCaseTest {

    // Doubles can pick up tiny rounding errors, so costs are compared to within a tenth of a cent
    private static final double DELTA = 0.001;

    // Each builder below returns a new PENDING case with round numbers, so the expected totals
    // can be checked by hand.

    // Injured lion: 5 days x R1,000 care (R5,000) + R2,000 vet treatment = R7,000 before any surgery fee
    private InjuredAnimalRescue injuredLion(boolean surgeryRequired) {
        return new InjuredAnimalRescue(1, "Leo", "Lion", "Kruger National Park", "Sipho",
                5, 1000, RescueStatus.PENDING, surgeryRequired, "Fractured leg", 2000);
    }

    // Orphaned elephant: 10 days x R500 care (R5,000) + R300 feeding = R5,300 before any foster care fee
    private OrphanedAnimalRescue orphanedElephant(boolean fosterCareRequired) {
        return new OrphanedAnimalRescue(2, "Ellie", "Elephant", "Addo Elephant Park", "Thandi",
                10, 500, RescueStatus.PENDING, 6, 300, fosterCareRequired);
    }

    // Endangered rhino: 3 days x R2,000 care (R6,000) + R10,000 security = R16,000 before any specialist fee.
    // "Low" is passed as the priority on purpose: the class should ignore it and use High.
    private EndangeredSpeciesRescue endangeredRhino(boolean specialistTeamRequired) {
        return new EndangeredSpeciesRescue(3, "Nandi", "Black Rhino", "Hluhluwe-iMfolozi Park", "Bongani",
                3, 2000, RescueStatus.PENDING, "Low", "Critically Endangered", 10000, specialistTeamRequired);
    }

    // One PENDING case of each type, so the status tests cover every subclass's startRescue/completeRescue
    private RescueCase[] oneOfEachType() {
        return new RescueCase[] { injuredLion(false), orphanedElephant(false), endangeredRhino(false) };
    }

    // ---- Rescue cost calculations ----

    @Test
    void injuredRescueCostIsCarePlusVetTreatment() {
        assertEquals(7000, injuredLion(false).getTotalCost(), DELTA);
    }

    @Test
    void injuredRescueCostAddsSurgeryFeeWhenSurgeryIsRequired() {
        assertEquals(12000, injuredLion(true).getTotalCost(), DELTA); // R7,000 + R5,000 surgery fee
    }

    @Test
    void orphanedRescueCostIsCarePlusFeeding() {
        assertEquals(5300, orphanedElephant(false).getTotalCost(), DELTA);
    }

    @Test
    void orphanedRescueCostAddsFosterCareFeeWhenFosterCareIsRequired() {
        assertEquals(7800, orphanedElephant(true).getTotalCost(), DELTA); // R5,300 + R2,500 foster care fee
    }

    @Test
    void endangeredRescueCostIsCarePlusSecurity() {
        assertEquals(16000, endangeredRhino(false).getTotalCost(), DELTA);
    }

    @Test
    void endangeredRescueCostAddsSpecialistTeamFeeWhenSpecialistTeamIsRequired() {
        assertEquals(24000, endangeredRhino(true).getTotalCost(), DELTA); // R16,000 + R8,000 specialist team fee
    }

    @Test
    void rescueCostHandlesAmountsWithCents() {
        // 3 days x R1,250.75 = R3,752.25, plus R99.99 vet treatment
        InjuredAnimalRescue rescue = new InjuredAnimalRescue(4, "Zuri", "Cheetah", "Pilanesberg", "Lerato",
                3, 1250.75, RescueStatus.PENDING, false, "Snare wound", 99.99);

        assertEquals(3852.24, rescue.getTotalCost(), DELTA);
    }

    @Test
    void rescueCostIsRecalculatedWhenCostDetailsChange() {
        InjuredAnimalRescue rescue = injuredLion(false);

        rescue.setSurgeryRequired(true);
        rescue.setVetTreatmentCost(3000);

        assertEquals(13000, rescue.getTotalCost(), DELTA); // R5,000 care + R3,000 vet + R5,000 surgery fee
    }

    // ---- Rescue priority ----
    // Priority is set by the rescue type, not by the details of the case.

    @Test
    void injuredRescueIsAlwaysHighPriority() {
        assertEquals("High", injuredLion(false).rescuePriority);
        assertEquals("High", injuredLion(true).rescuePriority);
    }

    @Test
    void orphanedRescueIsAlwaysMediumPriority() {
        assertEquals("Medium", orphanedElephant(false).rescuePriority);
        assertEquals("Medium", orphanedElephant(true).rescuePriority);
    }

    @Test
    void endangeredRescueIsAlwaysHighPriorityWhateverPriorityIsPassedIn() {
        assertEquals("High", endangeredRhino(false).rescuePriority);
        assertEquals("High", endangeredRhino(true).rescuePriority);
    }

    // ---- Rescue status updates ----

    @Test
    void startRescueMovesAPendingCaseToInProgress() {
        for (RescueCase rescue : oneOfEachType()) {
            rescue.startRescue();

            assertEquals(RescueStatus.IN_PROGRESS, rescue.rescueStatus, rescue.getClass().getName());
        }
    }

    @Test
    void completeRescueMovesAnInProgressCaseToCompleted() {
        for (RescueCase rescue : oneOfEachType()) {
            rescue.startRescue();
            rescue.completeRescue();

            assertEquals(RescueStatus.COMPLETED, rescue.rescueStatus, rescue.getClass().getName());
        }
    }

    @Test
    void cannotCompleteARescueThatHasNotStarted() {
        for (RescueCase rescue : oneOfEachType()) {
            assertThrows(IllegalStateException.class, rescue::completeRescue, rescue.getClass().getName());

            // A refused update must leave the status as it was
            assertEquals(RescueStatus.PENDING, rescue.rescueStatus, rescue.getClass().getName());
        }
    }

    @Test
    void cannotStartARescueThatIsAlreadyInProgress() {
        for (RescueCase rescue : oneOfEachType()) {
            rescue.startRescue();

            assertThrows(IllegalStateException.class, rescue::startRescue, rescue.getClass().getName());
            assertEquals(RescueStatus.IN_PROGRESS, rescue.rescueStatus, rescue.getClass().getName());
        }
    }

    @Test
    void completedRescueCannotBeStartedOrCompletedAgain() {
        for (RescueCase rescue : oneOfEachType()) {
            rescue.startRescue();
            rescue.completeRescue();

            assertThrows(IllegalStateException.class, rescue::startRescue, rescue.getClass().getName());
            assertThrows(IllegalStateException.class, rescue::completeRescue, rescue.getClass().getName());
            assertEquals(RescueStatus.COMPLETED, rescue.rescueStatus, rescue.getClass().getName());
        }
    }

    @Test
    void refusedStatusUpdateExplainsWhy() {
        // RescueSystem shows this message to the user, so it needs to say what went wrong
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> injuredLion(false).completeRescue());

        assertEquals("Rescue #1 cannot move from Pending to Completed.", e.getMessage());
    }

    @Test
    void rescueCaseMustBeCreatedWithAStatus() {
        assertThrows(NullPointerException.class, () -> new InjuredAnimalRescue(1, "Leo", "Lion",
                "Kruger National Park", "Sipho", 5, 1000, null, false, "Fractured leg", 2000));
    }

    @Test
    void statusCanOnlyMoveForwardOneStepAtATime() {
        assertTrue(RescueStatus.PENDING.canTransitionTo(RescueStatus.IN_PROGRESS));
        assertFalse(RescueStatus.PENDING.canTransitionTo(RescueStatus.PENDING));
        assertFalse(RescueStatus.PENDING.canTransitionTo(RescueStatus.COMPLETED));

        assertTrue(RescueStatus.IN_PROGRESS.canTransitionTo(RescueStatus.COMPLETED));
        assertFalse(RescueStatus.IN_PROGRESS.canTransitionTo(RescueStatus.PENDING));
        assertFalse(RescueStatus.IN_PROGRESS.canTransitionTo(RescueStatus.IN_PROGRESS));

        assertFalse(RescueStatus.COMPLETED.canTransitionTo(RescueStatus.PENDING));
        assertFalse(RescueStatus.COMPLETED.canTransitionTo(RescueStatus.IN_PROGRESS));
        assertFalse(RescueStatus.COMPLETED.canTransitionTo(RescueStatus.COMPLETED));
    }
}
