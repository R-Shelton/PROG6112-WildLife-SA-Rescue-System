import java.util.Scanner;

public class main {

    static void main() {
        RescueSystem rescueSystem = new RescueSystem();
        int selectedChoice;
        Scanner scanner = new Scanner(System.in);


        do {
            System.out.println("======================================");
            System.out.println("WILDLIFE RESCUE OPERATIONS SYSTEM");
            System.out.println("======================================");

            System.out.println("1. Create Rescue Case");
            System.out.println("2. Search Rescue Case");
            System.out.println("3. Update Rescue Case");
            System.out.println("4. Display All Rescue Cases");
            System.out.println("5. Rescue Report");
            System.out.println("6. Exit");

            System.out.println("Select an option:");

            selectedChoice = scanner.nextInt();

            switch(selectedChoice) {
                case 1:
                    rescueSystem.createRescue();
                    break;
                case 2:
                    rescueSystem.searchRescueCase();
                    break;
                case 3:
                    rescueSystem.updateRescueCase();
                    break;
                case 4:
                    rescueSystem.displayAllRescueCases();
                    break;
                case 5:
                    rescueSystem.displayRescueReport();
                    break;
                case 6:
                    System.out.println("Thank you! Come Again!");
                    break;
            }

        } while (selectedChoice != 6);


    }
}
