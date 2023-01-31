import java.util.*;

public class Main {

    // Only works on terminals that support ANSI codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    // Change these values into constants with names so that they are easier to
    // use/understand
    static final int NORMALPRICE = 2500;
    static final int FRONTROWPRICE = 5000;
    static final int EMPTY = 0;
    static final int TAKEN = 1;
    static final int SELECTED = 2;
    static final String TAKENCHR = ANSI_RED + "#" + ANSI_RESET;
    static final String SELECTEDCHR = ANSI_GREEN + "₱" + ANSI_RESET;
    static final String EMPTYFRONTCHR = ANSI_YELLOW + "Q" + ANSI_RESET;
    static final String EMPTYCHR = "O";
    static final String CONREVLOGO = ANSI_CYAN +
            "   ______                          \n" +
            "  / ____/___  ____  ________ _   __\n" +
            " / /   / __ \\/ __ \\/ ___/ _ \\ | / /\n" +
            "/ /___/ /_/ / / / / /  /  __/ |/ / \n" +
            "\\____/\\____/_/ /_/_/   \\___/|___/  \n" +
            ANSI_RESET;
    static final String DIVIDER = ANSI_CYAN + "------------------------" + ANSI_RESET;

    static final int ROWS = 5;
    static final int SEATSPERROW = 5;

    // Place these variables outside the main function so that
    // They can be accessed by other methods
    static int[] seats = new int[ROWS * SEATSPERROW];
    static int totalCost = 0;

    public static void main(String[] args) {
        // Initialize variables for user input
        Scanner sc = new Scanner(System.in);
        int input;

        // Print out Conrev logo
        System.out.println(CONREVLOGO);

        // label the loop, so I can break out of the loop inside a switch statement
        loop: while (true) {
            // Show options to user
            System.out.println(DIVIDER);
            System.out.println("1. View available" + (totalCost != 0 ? "/selected" : "") + " seats");
            System.out.println("2. Select a seat to reserve" + (totalCost != 0 ? "/deselect" : ""));

            // Only show these options when user has selected a seat
            if (totalCost != 0) {
                System.out.println("\nThe current total cost of the seats is " + totalCost + " ₱.");
                System.out.println("3. Confirm Transaction");
                System.out.println("4. Cancel Transaction");
            }

            System.out.println("0. Exit");

            // Get user input
            input = getInt(sc);

            switch (input) {
                // If the user inputs 1 then print the current state of the seats
                case 1:
                    printSeats(seats);
                    break;

                // If the user inputs 2 then prompt the user for seat selection
                case 2:
                    selectSeat(sc);
                    break;

                // If the user inputs 3 and the user has selected a seat
                // Then prompt the user for transaction confirmation
                case 3:
                    if (totalCost != 0)
                        confirmTransaction(sc);
                    break;

                // If the user inputs 4 and the user has selected a seat
                // Then prompt the user for transaction cancellation
                case 4:
                    if (totalCost != 0)
                        cancelTransaction(sc);
                    break;

                // If the user inputs 0 then end the program
                case 0:
                    System.out.println("Thank you for using Conrev!");
                    break loop;

            }
        }
    }

    public static void printSeats(int[] seats) {
        System.out.println(DIVIDER);
        // Shows where the front is
        System.out.println(" ".repeat(SEATSPERROW / 2 + 4) + "FRONT");
        System.out.print("SEAT  ");

        // Prints out all the seat letters
        for (int i = 0; i < SEATSPERROW; ++i) {
            System.out.print(Character.toString('A' + i));
        }
        System.out.println();

        // Prints out each row, front to back
        for (int x = 0; x < ROWS; ++x) {
            System.out.print("ROW " + (x + 1) + " ");
            for (int y = 0; y < SEATSPERROW; ++y) {

                // Checks if the seat is either front row empty, empty, taken, or selected
                // Prints out the appropriate character
                if (seats[x * ROWS + y] == TAKEN) {
                    System.out.print(TAKENCHR);
                } else if (seats[x * ROWS + y] == SELECTED) {
                    System.out.print(SELECTEDCHR);

                } else {
                    if (x == 0)
                        System.out.print(EMPTYFRONTCHR);
                    else
                        System.out.print(EMPTYCHR);
                }

            }
            System.out.println();
        }
        System.out.println(TAKENCHR + " - Seat is taken");
        System.out.println(SELECTEDCHR + " - Selected seat");
        System.out.println(EMPTYFRONTCHR + " - Front row seat 5000 ₱");
        System.out.println(EMPTYCHR + " - Regular seat 2500 ₱");
    }

    public static void selectSeat(Scanner sc) {
        System.out.println(DIVIDER);
        // Initialize variables for seat input
        int row;
        int seat;
        int input;

        // Get row number input and make sure the user enters valid row number
        do {
            row = getInt(sc, "Enter row number (1 -> " + ROWS + "): ") - 1;
            if (row >= ROWS || row < 0)
                System.out.println(ANSI_RED + "Enter valid row numbers" + ANSI_RESET);
        } while (row >= ROWS || row < 0);

        // Get seat letter input and make sure the user enters valid seat letter
        do {
            seat = getLetter(sc, "Enter seat letter (A -> " + Character.toString('A' + SEATSPERROW - 1) + "): ") - 'A';
            if (seat >= SEATSPERROW || seat < 0)
                System.out.println(ANSI_RED + "Enter valid seat letters" + ANSI_RESET);
        } while (seat >= SEATSPERROW || seat < 0);

        System.out.println(DIVIDER);

        // If the selected seat is empty, proceed
        // If the seat is not empty, go back to main screen
        if (seats[row * SEATSPERROW + seat] == EMPTY) {
            // Check if the seat is a front row seat
            // Then show the price of the seat
            if (row == 0)
                System.out
                        .println("The seat that you chose is a " + ANSI_YELLOW + "front row" + ANSI_RESET + " seat.\n" +
                                "The seat costs " + FRONTROWPRICE + " ₱");
            else
                System.out.println("The seat that you chose is a regular seat.\n" +
                        "The seat costs " + NORMALPRICE + " ₱");

            // Show options
            // Make sure user enters valid option
            input = confirmationPrompt(sc, "1. Reserve seat\n" +
                    "2. Go back");

            if (input == 1) {
                // Turn the selected seat into SELECTED instead of EMPTY
                seats[row * SEATSPERROW + seat] = SELECTED;
                // Add amount to totalCost depending on whether seat is front row
                if (row == 0)
                    totalCost += FRONTROWPRICE;
                else
                    totalCost += NORMALPRICE;
                System.out.println(DIVIDER);
                System.out.println(ANSI_GREEN + "Seat selected successfully!" + ANSI_RESET);
            } else {
                System.out.println(DIVIDER);
                System.out.println("Seat reservation cancelled");
            }

        } else if (seats[row * SEATSPERROW + seat] == SELECTED) {
            // If the seat is already selected
            // Deselect it and subtract the appropriate price to totalCost
            seats[row * SEATSPERROW + seat] = EMPTY;
            if (row == 0)
                totalCost -= FRONTROWPRICE;
            else
                totalCost -= NORMALPRICE;
            System.out.println(ANSI_GREEN + "Seat deselected!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "This seat is already reserved." + ANSI_RESET);
        }
    }

    public static void confirmTransaction(Scanner sc) {
        System.out.println(DIVIDER);
        int input;

        // Count the amount of selected seats
        long seatCount = Arrays.stream(seats).filter(seat -> seat == SELECTED).count();

        // Show current transaction state
        System.out.println(ANSI_GREEN + "Transaction Confirmation:" + ANSI_RESET);
        System.out.println("You have selected a total of " + seatCount + " seats.");
        System.out.println("The total cost of the seats is " + totalCost + " ₱.");
        // Ask for confirmation and show options
        // Make sure user enters valid option
        input = confirmationPrompt(sc, """
                Are you sure you want to confirm transaction?
                1. Yes
                2. Go back
                """);

        if (input == 1) {
            // Reset the transaction
            totalCost = 0;

            // Turns all the selected seats into taken
            seats = Arrays.stream(seats).map(seat -> seat == SELECTED ? TAKEN : seat).toArray();

            System.out.println(ANSI_GREEN + "Transaction Confirmed" + ANSI_RESET);
            System.out.println("Thank you for using " + ANSI_CYAN + "Conrev" + ANSI_RESET + "!");
        } else {
            System.out.println(ANSI_RED + "Confirmation cancelled" + ANSI_RESET);
        }
    }

    public static void cancelTransaction(Scanner sc) {
        System.out.println(DIVIDER);
        // Initialize input variable

        int input;

        // Confirm cancellation and show options
        // Make sure user enters valid option
        input = confirmationPrompt(sc, """
                Cancel current transaction?
                1. Cancel transaction
                2. Go back""");

        if (input == 1) {
            // Reset the transaction
            totalCost = 0;

            // Deselect all the selected seats
            seats = Arrays.stream(seats).map(seat -> seat == SELECTED ? EMPTY : seat).toArray();

            System.out.println(ANSI_GREEN + "Transaction Cancelled" + ANSI_RESET);
        }
    }

    private static int getInt(Scanner sc) {
        // With this the method still works even if you don't specify prompt.
        return getInt(sc, "");
    }

    private static int getInt(Scanner sc, String prompt) {
        // This method repeats until the user inputs a valid number
        String input;
        while (true) {
            // Trim ignores any whitespace before and after input
            System.out.print(prompt);
            input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Enter a valid number" + ANSI_RESET);
            }
        }
    }

    private static int confirmationPrompt(Scanner sc, String prompt) {
        // binaryPrompt shows the prompt message and makes sure user enters either 1 or
        // 2
        int input;
        do {
            System.out.println(prompt);
            input = getInt(sc);
        } while (input != 1 && input != 2);
        return input;
    }

    private static char getLetter(Scanner sc, String prompt) {
        // This method repeats until the user inputs a valid letter
        String inputChar;

        while (true) {

            // Trim ignores any whitespace before and after input
            System.out.print(prompt);
            inputChar = sc.nextLine().trim();

            // If the input is greater than one character then
            // Tell user to only input 1 letter and re-prompt
            if (inputChar.length() == 1) {

                inputChar = inputChar.toUpperCase();

                // Checks if the character is a letter
                if (inputChar.charAt(0) < 'A' || inputChar.charAt(0) > 'Z')
                    System.out.println(ANSI_RED + "Enter a valid letter" + ANSI_RESET);
                else
                    return inputChar.charAt(0);

            } else
                System.out.println(ANSI_RED + "Enter only 1 letter" + ANSI_RESET);

        }
    }
}
