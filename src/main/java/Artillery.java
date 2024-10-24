package src.main.java;
import java.util.*;
import src.main.java.GameStats;
import src.main.java.CpuGameData;
import src.main.java.ConsolePrinter;
import src.main.java.AnsiColors;





public class Artillery {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		boolean i = true;
    GameStats gameStats = new GameStats(0,0,1);

    ConsolePrinter.printWelcomeMessage();

    ConsolePrinter.textColorPrompt(scan); //asks to turn colored text off/on 
		ConsolePrinter.gameRulesPrompt(scan);	//shows game rules only on program start
		
		while ( i == true )
			i = gameCode(gameStats, scan);	

		ConsolePrinter.printWinCongratulator(gameStats);	//prints win% and assessment of play based on win%
	}
	


	public static boolean gameCode(GameStats gameStats, Scanner scan) {
		Random randObj = new Random();
    boolean isCpuFirst;
    boolean exit = false;
    String firstPlayerStr = "";
		double baseDistanceGap = randomDistance(randObj); // calculates a random distance between 350 and 700
    
		gameStats.incrementGamesPlayed();	
		ConsolePrinter.printGameStart(gameStats.getGamesPlayed());
		
    isCpuFirst = determinePlayerOrder(randObj);
    firstPlayerStr = isCpuFirst ? "The CPU" : "YOU";
		ConsolePrinter.printBaseDistance(baseDistanceGap, firstPlayerStr);
		gameStats.setRoundNum(1); //sets the current round to #1, so that the player_order function will work
			
		singleGame(isCpuFirst, randObj, baseDistanceGap, scan, gameStats);	

		exit = ConsolePrinter.playAgainPrompt(scan);

		return exit;
	}
			
	public static void singleGame(boolean isCpuFirst, Random randObj, double baseDistanceGap, Scanner scan, GameStats gameStats) {
		boolean isPlayerWinner; 
    boolean isCpuWinner;
    CpuGameData cpuData = new CpuGameData();
		
		isPlayerWinner = false;	
		isCpuWinner = false;

		while( isPlayerWinner == false && isCpuWinner == false ) { //runs until win or loss criteria is met
      gameStats.incrementRoundNum();
      ConsolePrinter.printRoundNumber(gameStats);

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
		}
	}// end singleGame
		
		
	public static boolean cpuTurn(Random randObj, double baseDistanceGap, CpuGameData cpuData) {
		double cpuMissileDist;
    boolean returnBool;
		
        // example: minBound = 25, maxOffset = 20, then maxBound would be 45, so CPU will pick a value somewhere between 25 and 45
		cpuData.setDegrees(cpuData.getMinDegreeBound() + randObj.nextDouble() * cpuData.getMaxDegreeOffset()); 
		System.out.print("The CPU chooses " + (int)Math.floor( cpuData.getDegrees() ) + " degrees.\n");
		cpuData.setSpeed(cpuData.getMinSpeedBound() + randObj.nextDouble() * cpuData.getMaxSpeedOffset()); 
		System.out.println("The CPU chooses a speed of " + (int)Math.floor( cpuData.getSpeed() ) + " meters per second.");
	
		cpuMissileDist = missileTravelDistance(cpuData.getDegrees(), cpuData.getSpeed()); 
		cpuMissileDist = Math.floor(cpuMissileDist); //rounds result for easier viewability
    ConsolePrinter.printCpuTurnResult(cpuMissileDist, baseDistanceGap);
		
    if ( Math.abs(cpuMissileDist - baseDistanceGap) <= 5 )	//win condition -- missile within 5m of player base
    {					
      ConsolePrinter.printCpuHit();
      returnBool = true;
    }
    else
    {
      ConsolePrinter.printCpuMiss();
      returnBool = false;
    }		

    recalculateCpuBounds(cpuData, baseDistanceGap, cpuMissileDist);

    return returnBool;
	}// end cpu_turn

  public static void recalculateCpuBounds(CpuGameData cpuData, double baseDistanceGap, double cpuMissileDist) {
    /*
     * below is the, ahem, "STATE OF THE ART" AI algorithm. For the first round,
     * values of the mins and the max offsets are given. After that, those values 
     * based each round based on whether or not the cpu undershoots or overshoots 
     * the player base. 
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
    PlayerGameData playerData = new PlayerGameData();
		double plyrDegChoice, plyrSpeedChoice, playerMissileDist;
    boolean returnBool = false;
		
    ConsolePrinter.playerTurnPrompt(playerData, scan);
    plyrDegChoice = playerData.getDegrees();
    plyrSpeedChoice = playerData.getSpeed();
		
		playerMissileDist = missileTravelDistance(plyrDegChoice, plyrSpeedChoice);	//calculates distance the missile travels
		playerMissileDist = Math.floor(playerMissileDist);	//rounds result for easier viewability
		
    if (Math.abs(playerMissileDist - baseDistanceGap) <= 5) { // win condition -- missile within 5m of cpu base                                                        
      ConsolePrinter.printPlayerHit();
      returnBool = true;
    } else {
      ConsolePrinter.printPlayerMiss(playerMissileDist, baseDistanceGap);
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
		dist = 350 + randObj.nextDouble() * 350; //creates a random distance between bases between 350-700
		dist = Math.floor(dist);	//makes the distance an even integer for viewability
		return (int) dist;
	}
	
	public static boolean determinePlayerOrder(Random randObj) {
		double cpu_hum;
		boolean isCpuFirst;
		
		cpu_hum = randObj.nextDouble();	//generates a random double between 0 and 1
		
		if ( cpu_hum > .5 ) {
			isCpuFirst = true;	//cpu will go first
    } else {
			isCpuFirst = false;	//human will go first
    }
	
		return isCpuFirst;	
	}
}//end class




