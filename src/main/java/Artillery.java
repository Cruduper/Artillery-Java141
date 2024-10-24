package src.main.java;
import java.util.*;
import src.main.java.GameStats;
import src.main.java.CpuGameData;
import src.main.java.AnsiColors;





public class Artillery {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		boolean i = true;
    GameStats gameStats = new GameStats(0,0,1);

    System.out.print("*******************************************************************************\n\n");
    System.out.println("Welcome to Artill3ry, by CrudTech gaming division.\n");

    textColorizer(scan); //asks to turn colored text off/on 
		gameRules(scan);	//shows game rules only on program start
		
		while ( i == true )
			i = gameCode(gameStats, scan);	

		winCongratulator(gameStats);	//prints win% and assessment of play based on win%
	}
	


	public static boolean gameCode(GameStats gameStats, Scanner scan) {
		Random randObj = new Random();
    boolean isCpuFirst;
    boolean exit = false;
    String firstPlayerStr = "";
		double baseDistanceGap;
    String confirm;
		
		gameStats.incrementGamesPlayed();	
		System.out.println("\n");
		System.out.println(AnsiColors.blue() + "****************************          GAME #" + gameStats.getGamesPlayed() + "          ****************************\n" + AnsiColors.reset());
		
		
		baseDistanceGap = randomDistance(randObj);	// calculates a random distance between 350 and 700
    isCpuFirst = determinePlayerOrder(randObj);
    if (isCpuFirst) {
      firstPlayerStr = "The CPU";
    } else {
      firstPlayerStr = "YOU";
    }

		System.out.println("The distance between you and your opponent's base is: " + baseDistanceGap + " meters away. " + firstPlayerStr + " will fire first.\n");
		
		gameStats.setRoundNum(1); //sets the current round to #1, so that the player_order function will work
			
		singleGame(isCpuFirst, randObj, baseDistanceGap, scan, gameStats);	

		
		System.out.print("\nWould you like to play again? (y/n): ");
    confirm = scan.next();
		
		if ( confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y") )
    {
			exit = true;	//resets the while loop in main if the user wants to play again
    } else {
			exit = false;	//exits the while loop in main
    }
		System.out.print("\n\n");
  
		return exit;		//tells while loop in main whether or not to play again 
	}
			
	public static void singleGame(boolean isCpuFirst, Random randObj, double baseDistanceGap, Scanner scan, GameStats gameStats) {
		boolean isPlayerWinner; 
    boolean isCpuWinner;
    CpuGameData cpuData = new CpuGameData();
		
		isPlayerWinner = false;	
		isCpuWinner = false;

		while( isPlayerWinner == false && isCpuWinner == false ) { //runs until win or loss criteria is met

      gameStats.incrementRoundNum();
      System.out.print("\n*******      " + AnsiColors.yellow() + "ROUND #" + gameStats.getRoundNum() + AnsiColors.reset()
          + "      *******\n");

      if (isCpuFirst) {
        isCpuWinner = cpuTurn(randObj, baseDistanceGap, cpuData);	
        
        if ( isCpuWinner == false )	// if the cpu doesn't hit, player gets a turn
        {	
          isPlayerWinner = playerTurn(scan, baseDistanceGap);	
          if (isPlayerWinner) {
            gameStats.incrementGamesWon();
          }
        }
      } else {
        isPlayerWinner = playerTurn(scan, baseDistanceGap);

        if (isPlayerWinner == false) // if the cpu doesn't hit, player gets a turn
        {
          isCpuWinner = cpuTurn(randObj, baseDistanceGap, cpuData);
        } else {
          gameStats.incrementGamesWon();
        }
      }
		}// end while
	}// end cpu_first
		
		
	public static boolean cpuTurn(Random randObj, double baseDistanceGap, CpuGameData cpuData) {
		double cpuMissileDist;
    boolean returnBool;
		
        // example: minBound = 25, maxOffset = 20, then maxBound would be 45, so CPU will pick a value somewhere between 25 and 45
		cpuData.setDegrees(cpuData.getMinDegreeBound() + randObj.nextDouble() * cpuData.getMaxDegreeOffset()); 
		System.out.print("The CPU chooses " + (int)Math.floor( cpuData.getDegrees() ) + " degrees.\n");
		cpuData.setSpeed(cpuData.getMinSpeedBound() + randObj.nextDouble() * cpuData.getMaxSpeedOffset()); 
		System.out.println("The CPU chooses a speed of " + (int)Math.floor( cpuData.getSpeed() ) + " meters per second.");
	
		cpuMissileDist = missileTravelDistance(cpuData.getDegrees(), cpuData.getSpeed()); //calculates the distance the missile travels
		cpuMissileDist = Math.floor(cpuMissileDist); //rounds result for easier viewability
		
		System.out.println("The CPU's shot hit " + (cpuMissileDist - baseDistanceGap) + " meters from your base.");
		
    if ( Math.abs(cpuMissileDist - baseDistanceGap) <= 5 )	//win condition -- missile within 5m of player base
    {					
      System.out.println(AnsiColors.red() + "               The cpu hit your base! YOU LOSE!!!!!!!!!!!" + AnsiColors.reset());
      returnBool = true;
    }
    else
    {
      System.out.println("The cpu missed your base!\n");
      returnBool = false;
    }		

    recalculateCpuBounds(cpuData, baseDistanceGap, cpuMissileDist);

    return returnBool;
	}// end cpu_turn

  public static void recalculateCpuBounds(CpuGameData cpuData, double baseDistanceGap, double cpuMissileDist) {
    /*
     * below is the, ahem, "STATE OF THE ART" AI algorithm. For the first round,
     * values of the mins and the max modifiers are given. After that, this code
     * changes those values based on whether or not the cpu undershoots or
     * overshoots the player base. It continues to change based on over or
     * undershooting during subsequent rounds of play.
     */

    if (cpuMissileDist - baseDistanceGap > 0) { // cpu overshoots the player base
      cpuData.setMaxDegreeOffset(cpuData.getDegrees() - cpuData.getMinDegreeBound()); // max degrees on next turn = previous angle choice
      cpuData.setMaxSpeedOffset(cpuData.getSpeed() - cpuData.getMinSpeedBound()); // max speed on next turn = previous speed choice
    }

    if (cpuMissileDist - baseDistanceGap < 0) { // cpu undershoots the player base
      double oldMinDegreeBound = cpuData.getMinDegreeBound();
      double oldMinSpeedBound = cpuData.getMinSpeedBound();
      double degreeDelta = 0;
      double speedDelta = 0;
      cpuData.setMinDegreeBound(cpuData.getDegrees()); // minDegreeBound for next round = this round's degree choice
      cpuData.setMinSpeedBound(cpuData.getSpeed()); // minSpeedBound for next round = this round's speed choice
      degreeDelta = cpuData.getMinDegreeBound() - oldMinDegreeBound;
      speedDelta = cpuData.getMinSpeedBound() - oldMinSpeedBound;

      cpuData.setMaxDegreeOffset(cpuData.getMaxDegreeOffset() - degreeDelta); // keeps max bound the same after the min bound is offset
      cpuData.setMaxSpeedOffset(cpuData.getMaxSpeedOffset() - speedDelta); // keeps max bound the same after the min bound is offset
    }
  }
	
	public static boolean playerTurn(Scanner scan, double baseDistanceGap) {
		double plyrDegChoice, plyrSpeedChoice, playerMissileDist;
    boolean returnBool = false;
		
		System.out.print("Enter an angle: ");
		plyrDegChoice = scan.nextDouble();
		System.out.print("Enter a speed: ");
		plyrSpeedChoice = scan.nextDouble();
		
		playerMissileDist = missileTravelDistance(plyrDegChoice, plyrSpeedChoice);	//calculates distance the missile travels
		playerMissileDist = Math.floor(playerMissileDist);	//rounds result for easier viewability
		
    if (Math.abs(playerMissileDist - baseDistanceGap) <= 5) { // win condition -- missile within 5m of cpu base                                                        
      System.out.println(AnsiColors.green() + "            You've hit the cpu base! YOU WIN!!!!!!!!!!!!!!!!!!" + AnsiColors.reset());
      returnBool = true;
    } else {
      System.out.println(AnsiColors.red()
          + "Your missile landed " + (playerMissileDist - baseDistanceGap) + " meters from the cpu base.\n"
          + AnsiColors.reset());
      returnBool =  false;
    }

    return returnBool;
	}
	
	public static double missileTravelDistance(double degrees, double speed) {
    double radians;
		radians = Math.toRadians(degrees);
		return ( speed * speed * Math.sin(2 * radians) ) / 9.8;	
	}

  public static int randomDistance(Random randObj) {
		double dist;
		dist = 350 + randObj.nextDouble() * 350; //creates a random distance between bases that is between 350 and 700
		dist = Math.floor(dist);	//makes the distance an even integer for viewability
		
		return (int) dist;
	}
	
	public static boolean determinePlayerOrder(Random randObj) {
		double cpu_hum;
		boolean isCpuFirst;
		
		cpu_hum = randObj.nextDouble();	//generates a random double between 0 and 1
		
		if ( cpu_hum > .5 )
			isCpuFirst = true;	//cpu will go first
		else
			isCpuFirst = false;	//human will go first
	
		return isCpuFirst;	
	}
	
	public static void gameRules(Scanner scan) {
		String confirm;
		System.out.print("Would you like to see the rules of the game? (y/n): ");	
		confirm = scan.next();	
		System.out.print("\n");
			
		if ( confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y") )
		{
			System.out.print(AnsiColors.yellow() + 
                           "***************************       Game Rules       ****************************\n" + AnsiColors.reset());
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
	}//end game_rules

  public static void textColorizer(Scanner scan){
    System.out.print("Would you like to enable colored text(causes text errors on some systems)? (y/n): ");
    String colorChoice = scan.nextLine();
    if (colorChoice.equalsIgnoreCase("n") || colorChoice.equalsIgnoreCase("no")) {
      AnsiColors.setEnableColors(false);
      System.out.println("\nCOLORS DISABLED");
    } else {
      System.out.println(AnsiColors.red() + "\nC" + AnsiColors.yellow() + "O" + AnsiColors.green() + "L" + AnsiColors.blue() + "O" + AnsiColors.red() + "R" + AnsiColors.yellow() + "S" + AnsiColors.reset() + " ENABLED\n");
    }
  }
	
	public static void winCongratulator(
  GameStats gameStats) {
		double winPercentage;
		winPercentage = (double)gameStats.getGamesWon() / (double)gameStats.getGamesPlayed();	
		
		System.out.println("\nYour win percentage is: " + winPercentage * 100 + "%.");
		
		if ( winPercentage <= .5 ) {
			System.out.print(AnsiColors.red() + "Your failure vs. the shoddy AI of a novice coder should be commended!!...or not.\n\n" + AnsiColors.reset());
    }

		if ( winPercentage < .9 && winPercentage > .5 ) {
			System.out.print(AnsiColors.yellow() + "Not bad...not great. Were you a middle child?\n" + AnsiColors.reset());
    }

		if ( winPercentage >= .9 )	{
			System.out.print(AnsiColors.green() + "Amazing! The battle between man and machine has been decided...erm...decisively.\n\n" + AnsiColors.reset());
    }
	}
}//end class




