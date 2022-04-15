import java.util.*;

public class project3artillery
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		boolean i = true;
		int[] numbers = new int[7];
		
		numbers[0] = 0;  //this counts how many games the player has won
		numbers[1] = 0;	 //this counts how many games the player has played.
		numbers[2] = 1;  //counts the number of rounds played in any game
		
		game_rules(scan);	//this function will display the rules for the game only when the program starts
		
		while ( i == true )
			i = game_code(i, numbers, scan);	//the actual code that runs the game!

		win_congratulator(numbers[0], numbers[1]);	//function that prints win% and assessment of users play based on win%
	}
	
	public static boolean game_code(boolean i, int[] numbers, Scanner scan)
	{
		Random ran = new Random();
		double X;	//this is the distance between the two bases. You said to call it "X" in the project description
		boolean win = false, lose = false;
		int order;
		String yesno;
		double[] cpu_choices = new double[2], aim_change = new double[4];
		
		
		cpu_choices[0] = 0;	//giving this indexes values so the compiler doesn't freak out. I'm doing it here
		cpu_choices[1] = 0;	//so that I don't have to do it twice (in both cpu_first and human_first)
		
		aim_change[0] = 0;  //changes the cpu's minimum allowable choice for degrees
		aim_change[1] = 0;  //changes the modifier for cpu's maximum allowable choice for degrees. max = (min + aim_change[1])
		aim_change[2] = 0;  //changes the cpu's minimum allowable choice for speed
		aim_change[3] = 0;  //changes the modifier for cpu's maximum allowable choice for speed. max = (min + aim_change[3])
		
		numbers[1]++;	
		//adds one to the game number so that the correct game# will display on screen every time the user asks to go again
		//and for the later win% calculation. Displaying this also can help in debugging!
		System.out.println("\n");
		System.out.println("*****************++++++++++GAME #" + numbers[1] + "++++++++++++++++++****************************\n");
		
		
		X = rand_dist(ran);	// calculates a random distance between 350 and 700
		System.out.println("The distance between you and your opponents base is: " + X + " meters away.");
		
		order = 2;	// this initializes order so the compiler won't freak out
		numbers[2] = 1; // this sets the current round to #0, so that the player_order function will work
		order = player_order(ran);	//this function picks a starting player(cpu or human) at random
		
		if ( order == 1 )		// arbitrarily says that gives the order = 1 the distinction of having cpu go first
			cpu_first(ran, X, scan, numbers, cpu_choices, aim_change);	// if cpu is first this function will execute
		else
			human_first(ran, X, scan, numbers, cpu_choices, aim_change);	// if user is first this function will execute
		
		System.out.print("\nWould you like to play again? (y/n): ");
		yesno = scan.next();
		System.out.println("");
		
		if ( yesno.equalsIgnoreCase("yes") || yesno.equalsIgnoreCase("y") )
			i = true;	// resets the while loop in main if the user wants to play again
		else
			i = false;	// exits the while loop in main if they user does not want to play again
		
		win = false;		// resets the win and lose booleans in case a new game is played
		lose = false;	
		
		return i;		// tells the while loop in main whether or not to play again based on the if-else statement above
	}
	
	public static int rand_dist(Random ran) 
	{
		double dist;
		dist = 350 + ran.nextDouble() * 350; //creates a randome distance between bases that is between 350 and 700
		dist = Math.floor(dist);	//makes the distance an even integer so it's easier to look at for the user
		
		return (int) dist;
	}
	
	public static int player_order(Random ran)
	{
		double cpu_hum;
		int order_no;
		
		cpu_hum = rand_generator(ran);	//generates a random double between 0 and 1
		
		if ( cpu_hum > .5 )	//this if-else statement splits the random double into 2 parts which will later correspond with which player
			order_no = 0;	//goes first in the first round of play
		else
			order_no = 1;	
	
		return order_no;	
	}// end player_order
			
	public static void cpu_first(Random ran, double X, Scanner scan, int[] numbers, double[] cpu_choices, double[] aim_change)
	{
		double cpu_hits, player_hits;
		boolean win, lose;
		
		System.out.println("\nThe CPU fires first!");
		
		win = false;	//initializes values for these booleans for the compiler
		lose = false;
		while( win == false && lose == false )	//the following code will run until win or loss criteria is met
		{
			cpu_hits = cpu_turn(ran, X, numbers, cpu_choices, aim_change);	// cpu_hits gives a value for how far away the cpu's missile lands
											// from the players base
			if ( Math.abs(cpu_hits - X) <= 5 )	// if the cpu's missile lands within 5 meters of the player's base, the cpu wins
			{					
				System.out.println("               The cpu hit your base! YOU LOSE!!!!!!!!!!!");
				lose = true;	// stops while loop in the case of a loss
			}
			else
			{
				System.out.println("The cpu missed your base!\n\n");
				lose = false;			// continues while loop if cpu doesn't win
			}					
			
			if ( lose == false )	// if the cpu doesn't hit the player's base during a round, the player gets a turn
			{	
				player_hits = player_turn(scan, X);	// player_hits give a vlue for how far away the player's missile lands
				
				if ( Math.abs(player_hits - X) <= 5 )	//if the player's missile lands within 5 meters of the cpu's base, the player wins
				{
					System.out.println("            You've hit the cpu base! YOU WIN!!!!!!!!!!!!!!!!!!");
					win = true;	// stops while loop
					numbers[0]++;	// in the event of a win, win count goes up by 1
				}
				else
				{
					System.out.println("You missed the cpu base!\n\n");
					win = false;	// continues while loop if player doesn't win
				}
			}
		}// end while
	}// end cpu_first
		
	public static void human_first(Random ran, double X, Scanner scan, int[] numbers, double[] cpu_choices, double[] aim_change)
	{
		double cpu_hits, player_hits;
		boolean win, lose;
		
		System.out.println("\nYOU get to fire first!\n");
		
		win = false;	//initializes values for these booleans for the compiler
		lose = false;
		while( win == false && lose == false )	//the following code will run until win or loss criteria is met
		{
			player_hits = player_turn(scan, X);	// player_hits give a vlue for how far away the player's missile lands
			
			if ( Math.abs(player_hits - X) <= 5 )	//if the player's missile lands within 5 meters of the cpu's base, the player wins
			{
				System.out.println("            You've hit the cpu base! YOU WIN!!!!!!!!!!!!!!!!!!");
				win = true;	// stops while loop
				numbers[0]++;	// in the event of a win, win count goes up by 1
			}
			else
			{
				System.out.println("You missed the cpu base!\n\n");
				win = false;	// continues while loop if player doesn't win
			}
			
			if ( win == false )	// if the player doesn't hit the cpu's base during a round, the cpu gets a turn
			{
				cpu_hits = cpu_turn(ran, X, numbers, cpu_choices, aim_change);	// cpu_hits gives a value for how far away the cpu's missile 
												// lands from the players base
				if ( Math.abs(cpu_hits - X) <= 5 )	// if the cpu's missile lands within 5 meters of the player's base, the cpu wins
				{
					System.out.println("               The cpu hit your base! YOU LOSE!!!!!!!!!!!");
					lose = true;	// stops while loop in the case of a loss
				}
				
				else
				{
					System.out.println("The cpu missed your base!\n\n");
					lose = false;	// if the cpu doesn't hit the player's base, the loop restarts
				}		
			}   					
		}// end while
	}// end human_first
		
	public static double cpu_turn(Random ran, double X, int[] numbers, double[] cpu_choices, double[] aim_change)
	{
		double cpu_missile_hit;
		
		if ( numbers[2] == 1 )	//in the first round of the game, these min, and max modifier boundaries are given for the cpu	
		{
			aim_change[0] = 25;  //changes the cpu's minimum allowable choice for degrees
			aim_change[1] = 20;  //changes the modifier for cpu's maximum allowable choice for degrees. max = (min + aim_change[1])
			aim_change[2] = 55;  //changes the cpu's minimum allowable choice for speed
			aim_change[3] = 35;  //changes the modifier for cpu's maximum allowable choice for speed. max = (min + aim_change[3])
		
			cpu_choices[0] = 0;	//final # of degrees the cpu chooses
			cpu_choices[1] = 0;	//final # of speed (in m/s) the cpu chooses
		}
			
		cpu_choices[0] = aim_change[0] + rand_generator(ran) * aim_change[1]; // degrees = min + ( random double between 0-1 ) * max modifier
		System.out.print("The cpu chooses " + (int)Math.floor( cpu_choices[0] ) + " degrees.\n");
		cpu_choices[1] = aim_change[2] + rand_generator(ran) * aim_change[3]; //speed = min + ( random double between 0-1 ) * max modifier
		System.out.println("The cpu chooses a speed of " + (int)Math.floor( cpu_choices[1] ) + " meters per second.");
	
		cpu_missile_hit = hit_calc(cpu_choices[0], cpu_choices[1]); // calculates the distance the missile travels
		cpu_missile_hit = Math.floor(cpu_missile_hit); // this makes the calculation slightly inaccurate, but is easier to look at
		
		System.out.println("The cpu's shot hit " + (cpu_missile_hit - X) + " meters from your base.");
		
		
		/* below is the "STATE OF THE ART" AI algorithm. For the first round, values of the mins and the max modifiers are given.
		   after that, this code changes those values based on whether or not the cpu undershoots or overshoots the player base. It
		   continues to change based on over or undershooting during subsequent rounds of play. */
			
		if ( cpu_missile_hit - X > 0 )	// if executes when the cpu overshoots the player base
		{
			aim_change[1] = cpu_choices[0] - aim_change[0]; // augments max angle modifier so that max angle on next turn = last angle choice
			aim_change[3] = cpu_choices[1] - aim_change[2]; // augments max speed modifier so that max speed on next turn = last speed choice
			
		}
		
		if ( cpu_missile_hit - X < 0 )	// if executes when the cpu undershoots the player base
		{
			aim_change[0] = cpu_choices[0]; // new degree min for next round = last degree choice 
			aim_change[2] = cpu_choices[1]; // new speed min for next round = last speed choice
			
			aim_change[1] = aim_change[1] - cpu_choices[0] + aim_change[0]; // this augments the max angle modifier to account for the min change
			aim_change[3] = aim_change[3] - cpu_choices[1] + aim_change[2]; // this augments the max speed modifier to account for the min change
		}
			
		numbers[2]++;	// makes the number of rounds go up, so that the givens for min, and max modifiers won't execute after first round of play
		return cpu_missile_hit;
	}// end cpu_turn
	
	public static double player_turn(Scanner scan, double X)
	{
		double plyr_deg_choice, plyr_speed_choice, plyr_missile_hit;
		
		System.out.print("Enter an angle: ");
		plyr_deg_choice = scan.nextDouble();	// scans input for an angle in degrees
		System.out.print("Enter a speed: ");
		plyr_speed_choice = scan.nextDouble();	// scans input for a spped
		
		plyr_missile_hit = hit_calc(plyr_deg_choice, plyr_speed_choice);	// uses the scanned angle and speed to calculate a distance
		plyr_missile_hit = Math.floor(plyr_missile_hit);	// this makes the calculation slightly inaccurate, but is easier to look at
		
		System.out.println("Your missile landed " + (plyr_missile_hit - X) + " meters from the cpu base.");
		
		return plyr_missile_hit;
	}
	
	public static double hit_calc(double degrees, double speed)
	{
		double missile_dist, radians;
		
		radians = Math.toRadians(degrees);	// changes the user input degrees into radians for use in the next equation
		missile_dist = ( speed * speed * Math.sin(2 * radians) ) / 9.8;	// takes input for angle and speed and calculates the distance
										// that the missile travels
		return missile_dist;
	}
	
	public static void game_rules(Scanner scan)
	{
		String confirm;
		
		System.out.print("*******************************************************************************\n\n");
		System.out.println("Welcome to Artill3ry, by CrudTech gaming division.\n");
		System.out.print("Would you like to see the rules of the game? (y/n): ");	// asks user if they'd like to see how to play the game
		confirm = scan.next();	
		System.out.print("\n\n");
			
		if ( confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y") )	//if user input is 'yes' or 'y', the rules of the game are showed
		{
			System.out.print("****************++++++++++Game Rules+++++++++++++++++***************************\n");
			System.out.print("The object of Artill3ry is to destroy the opponents base before the opponent    ");
			System.out.print("destroys your base. At the start you are given the distance the opponents base  ");
			System.out.print("is from your own. The game will randomly choose whether you or the CPU opponent ");
			System.out.print("fires first. When it is your turn, you are asked to input an angle that your    ");
			System.out.print("base will fire its cannon at, and the game will tell you where your missile     ");
			System.out.print("landed and whether or not you scored a hit. To win, your missile must land      ");
			System.out.print("within 5 meters (+/-) of the opponent's base. If you did not win, on your next  ");
			System.out.print("turn you will be able to change the angle of your shot to compensate for an over");
			System.out.print("shot or an under shot. Be warned, the CPU opponent uses a STATE OF THE ART never");
			System.out.print("seen before artificial intelligence algorithm, so be on your toes!!!\n\n");
			System.out.print("HIT ANY KEY, THEN ENTER TO CONTINUE...");
			confirm = scan.next();
		}
	}// end game_rules
	
	public static double rand_generator(Random ran)
	{
		double decider;
		decider = ran.nextDouble();	// generates random double between 0-1
		
		return decider;		// returns that random double for uses in other functions
	}
	
	public static void win_congratulator(double playerwins, int game_no)
	{
		double win_percentage;			// creating a win% variable cause it's easier to read than "playerwins/game_no" a bunch of times
		win_percentage = playerwins / game_no;	
		
		System.out.println("Your win percentage is: " + win_percentage * 100 + "%.");
		
		if ( win_percentage <= .75 )	// if win% is less than or equal to 75, user is chastised
			System.out.print("Your failure vs. the shoddy AI of a novice coder should be commended!!...or not.\n");
		if ( win_percentage < .9 && win_percentage > .75 )	// if win% is between 75 and 90, the user is neither chastised or congratulated
			System.out.print("Not bad...not great. Were you a middle child?\n");
		if ( win_percentage >= .9 )	// if win% is 90 or better, the user is congratulated
			System.out.print("Amazing! The battle between man and machine has been decided...erm...decisively.\n");
	}
}// end class




