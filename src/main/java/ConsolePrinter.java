package src.main.java;
import java.util.*;



public class ConsolePrinter {

  public static void printWelcomeMessage() {
    System.out.print("*******************************************************************************\n\n");
    System.out.println("Welcome to Artill3ry, by CrudTech gaming division.\n");
  }

  public static void printGameStart(int gameNumber) {
    System.out.println(AnsiColors.blue() + "****************************          GAME #" + gameNumber
        + "          ****************************\n" + AnsiColors.reset());
  }

  public static void printRoundNumber(GameStats gameStats) {
    System.out.print("\n*******      " + AnsiColors.yellow() + "ROUND #" + gameStats.getRoundNum() + AnsiColors.reset() + "      *******\n");
  }

  public static void printBaseDistance(double baseDistance, String firstPlayerStr) {
    System.out.println("The distance between you and your opponent's base is: " + baseDistance + " meters away. "
        + firstPlayerStr + " will fire first.\n");
  }

  public static void printCpuChoices(double degrees, double speed) {
    System.out.printf("The CPU chooses %.1f degrees.%n", degrees);
    System.out.printf("The CPU chooses a speed of %.1f meters per second.%n", speed);
  }

  public static void printCpuTurnResult(double cpuMissileDist, double baseDistanceGap) {
    System.out.println("The CPU's shot hit " + (cpuMissileDist - baseDistanceGap) + " meters from your base.");
  }

  public static void printCpuHit() {
    System.out.println(AnsiColors.red() + "               The cpu hit your base! YOU LOSE!!!!!!!!!!!" + AnsiColors.reset());
  }

  public static void printCpuMiss() {
    System.out.println("The cpu missed your base!\n");
  }

  public static void printPlayerHit() {
    System.out.println(AnsiColors.green() + "            You've hit the cpu base! YOU WIN!!!!!!!!!!!!!!!!!!" + AnsiColors.reset());
  }

  public static void printPlayerMiss(double playerMissileDist, double baseDistanceGap) {
    System.out.println(AnsiColors.red() + "Your missile landed " + (playerMissileDist - baseDistanceGap) + " meters from the cpu base.\n" + AnsiColors.reset());
  }

  public static void printWinCongratulator(GameStats gameStats) {
    double winPercentage;
    winPercentage = (double) gameStats.getGamesWon() / (double) gameStats.getGamesPlayed();

    System.out.println(
        "\nYour win percentage is: " + AnsiColors.bold() + (winPercentage * 100) + "%" + AnsiColors.reset() + ".");

    if (winPercentage <= .75) {
      System.out.print(
          AnsiColors.red() + "Your failure vs. the shoddy AI of a novice coder should be commended!!...or not.\n\n"
              + AnsiColors.reset());
    }

    if (winPercentage < .9 && winPercentage > .75) {
      System.out.print(AnsiColors.yellow() + "Not bad...not great. Were you a middle child?\n" + AnsiColors.reset());
    }

    if (winPercentage >= .9) {
      System.out.print(
          AnsiColors.green() + "Amazing! The battle between man and machine has been decided...erm...decisively.\n\n"
              + AnsiColors.reset());
    }
  }



  //*************       PROMPTS       *************/

  public static void textColorPrompt(Scanner scan) {
    System.out.print("Would you like to enable colored text(causes text errors on some systems)? (y/n): ");
    String colorChoice = scan.nextLine();
    if (colorChoice.equalsIgnoreCase("n") || colorChoice.equalsIgnoreCase("no")) {
      AnsiColors.setEnableColors(false);
      System.out.println("\nCOLORS DISABLED");
    } else {
      System.out
          .println(AnsiColors.red() + "\nC" + AnsiColors.yellow() + "O" + AnsiColors.green() + "L" + AnsiColors.blue()
              + "O" + AnsiColors.red() + "R" + AnsiColors.yellow() + "S" + AnsiColors.reset() + " ENABLED\n");
    }
  }

  public static void gameRulesPrompt(Scanner scan) {
    String confirm;
    System.out.print("Would you like to see the rules of the game? (y/n): ");
    confirm = scan.next();
    System.out.print("\n");

    if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
      System.out.print(AnsiColors.yellow() + "***************************       Game Rules       ****************************\n" + AnsiColors.reset());
      System.out.println("The object of Artill3ry is to destroy the opponents base before the opponent ");
      System.out.println("destroys your base. At the start you are given the distance the opponents base ");
      System.out.println("is from your own. The game will randomly choose whether you or the CPU opponent ");
      System.out.println("fires first. When it is your turn, you are asked to input an angle that your ");
      System.out.println("base will fire its cannon at, and the game will tell you where your missile ");
      System.out.println("landed and whether or not you scored a hit. To win, your missile must land ");
      System.out.println("within 5 meters (+/-) of the opponent's base. If you did not win, on your next ");
      System.out.println("turn you will be able to change the angle of your shot to compensate for an over ");
      System.out.println("shot or an under shot. Be warned, the CPU opponent uses a STATE OF THE ART never ");
      System.out.println("seen before artificial intelligence algorithm, so be on your toes!!!\n\n");
      System.out.println("HIT ANY KEY, THEN ENTER TO CONTINUE...");
      confirm = scan.next();
    }
  }

  public static void playerTurnPrompt(PlayerGameData playerData, Scanner scan) {
    System.out.print("Enter an angle: ");
    playerData.setDegrees(scan.nextDouble());
    System.out.print("Enter a speed: ");
    playerData.setSpeed(scan.nextDouble());
  }

  public static boolean playAgainPrompt(Scanner scan) {
    String confirm;
    boolean exit = false;

    System.out.print("\nWould you like to play again? (y/n): ");
    confirm = scan.next();

    if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
      exit = true; // resets the while loop in main if the user wants to play again
    } else {
      exit = false; // exits the while loop in main
    }
    System.out.print("\n\n");

    return exit;
  }
}
