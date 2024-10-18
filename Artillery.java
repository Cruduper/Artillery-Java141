import java.util.*;

public class Artillery {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		boolean i = true;
		int[] numbers = new int[7];
		
		numbers[0] = 0;  //counts # games player has won
		numbers[1] = 0;	 //counts # games player has played.
		numbers[2] = 1;  //counts # rounds played in any game
		
		game_rules(scan);	//shows game rules only on program start
		
		while ( i == true )
			i = game_code(i, numbers, scan);	

		win_congratulator(numbers[0], numbers[1]);	//prints win% and assessment of play based on win%
	}
	
	public static boolean game_code(boolean i, int[] numbers, Scanner scan) {
		Random ran = new Random();
		double X;	//distance between the two bases.
		int order;
		String yesno;
		double[] cpu_choices = new double[2], aim_change = new double[4];
		
		
		cpu_choices[0] = 0;	
		cpu_choices[1] = 0;	
		
		aim_change[0] = 0;  //changes the cpu's minimum allowable choice for degrees
		aim_change[1] = 0;  //changes the modifier for cpu's maximum allowable choice for degrees. max = (min + aim_change[1])
		aim_change[2] = 0;  //changes the cpu's minimum allowable choice for speed
		aim_change[3] = 0;  //changes the modifier for cpu's maximum allowable choice for speed. max = (min + aim_change[3])
		
		numbers[1]++;	/*increments games played count*/
		System.out.println("\n");
		System.out.println("*****************++++++++++GAME #" + numbers[1] + "++++++++++++++++++****************************\n");
		
		
		X = rand_dist(ran);	// calculates a random distance between 350 and 700
		System.out.println("The distance between you and your opponents base is: " + X + " meters away.");
		
		order = 2;	//initializes order so the compiler won't freak out
		numbers[2] = 1; //sets the current round to #0, so that the player_order function will work
		order = player_order(ran);	
		
		if ( order == 1 )		
			cpu_first(ran, X, scan, numbers, cpu_choices, aim_change);	
		else
			human_first(ran, X, scan, numbers, cpu_choices, aim_change);	
		
		System.out.print("\nWould you like to play again? (y/n): ");
		yesno = scan.next();
		System.out.println("");
		
		if ( yesno.equalsIgnoreCase("yes") || yesno.equalsIgnoreCase("y") )
			i = true;	//resets the while loop in main if the user wants to play again
		else
			i = false;	//exits the while loop in main
		
		return i;		//tells while loop in main whether or not to play again 
	}
	
	public static int rand_dist(Random ran) {
		double dist;
		dist = 350 + ran.nextDouble() * 350; //creates a random distance between bases that is between 350 and 700
		dist = Math.floor(dist);	//makes the distance an even integer for viewability
		
		return (int) dist;
	}
	
	public static int player_order(Random ran) {
		double cpu_hum;
		int order_no;
		
		cpu_hum = rand_generator(ran);	//generates a random double between 0 and 1
		
		if ( cpu_hum > .5 )
			order_no = 0;	//cpu will go first
		else
			order_no = 1;	//human will go first
	
		return order_no;	
	}
			
	public static void cpu_first(Random ran, double X, Scanner scan, int[] numbers, double[] cpu_choices, double[] aim_change) {
		double cpu_hits, player_hits;
		boolean win, lose;
		
		System.out.println("\nThe CPU fires first!");
		
		win = false;	
		lose = false;
		while( win == false && lose == false )	//runs until win or loss criteria is met
		{
			cpu_hits = cpu_turn(ran, X, numbers, cpu_choices, aim_change);	// cpu_hits = how far the cpu's missile lands from player's base
			if ( Math.abs(cpu_hits - X) <= 5 )	// if cpu's missile lands within 5m of the player's base, cpu wins
			{					
				System.out.println("               The cpu hit your base! YOU LOSE!!!!!!!!!!!");
				lose = true;
			}
			else
			{
				System.out.println("The cpu missed your base!\n\n");
			}					
			
			if ( lose == false )	// if the cpu doesn't hit, player gets a turn
			{	
				player_hits = player_turn(scan, X);	// player_hits = how far away the player's missile lands
				
				if ( Math.abs(player_hits - X) <= 5 )	//if player's missile lands within 5m of the cpu's base, player wins
				{
					System.out.println("            You've hit the cpu base! YOU WIN!!!!!!!!!!!!!!!!!!");
					win = true;	//stops while loop
					numbers[0]++;	//during a win, win count goes up by 1
				}
				else
				{
					System.out.println("You missed the cpu base!\n\n");
				}
			}
		}// end while
	}// end cpu_first
		
	public static void human_first(Random ran, double X, Scanner scan, int[] numbers, double[] cpu_choices, double[] aim_change) {
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
			}
			
			if ( win == false )	
			{
				cpu_hits = cpu_turn(ran, X, numbers, cpu_choices, aim_change);	// cpu_hits gives distance the cpu's missile lands from player's base
				if ( Math.abs(cpu_hits - X) <= 5 )	// cpu wins if its missile lands within 5m of player's base
				{
					System.out.println("               The cpu hit your base! YOU LOSE!!!!!!!!!!!");
					lose = true;	// stops while loop
				} else {
					System.out.println("The cpu missed your base!\n\n");
				}		
			}   					
		}// end while
	}// end human_first
		
	public static double cpu_turn(Random ran, double X, int[] numbers, double[] cpu_choices, double[] aim_change) {
		double cpu_missile_hit;
		
		if ( numbers[2] == 1 ) { //first round of the game, min/max boundaries given to cpu
			aim_change[0] = 25;  //changes minimum allowable choice for degrees
			aim_change[1] = 20;  //changes maximum allowable choice for degrees. max = (min + aim_change[1])
			aim_change[2] = 55;  //changes minimum allowable choice for speed
			aim_change[3] = 35;  //changes maximum allowable choice for speed. max = (min + aim_change[3])
		
			cpu_choices[0] = 0;	//final cpu degrees choice
			cpu_choices[1] = 0;	//final cpu speed (in m/s) choice
		}
			
		cpu_choices[0] = aim_change[0] + rand_generator(ran) * aim_change[1]; //degrees = min + ( random double between 0-1 ) * max modifier
		System.out.print("The cpu chooses " + (int)Math.floor( cpu_choices[0] ) + " degrees.\n");
		cpu_choices[1] = aim_change[2] + rand_generator(ran) * aim_change[3]; //speed = min + ( random double between 0-1 ) * max modifier
		System.out.println("The cpu chooses a speed of " + (int)Math.floor( cpu_choices[1] ) + " meters per second.");
	
		cpu_missile_hit = hit_calc(cpu_choices[0], cpu_choices[1]); //calculates the distance the missile travels
		cpu_missile_hit = Math.floor(cpu_missile_hit); //rounds result for easier viewability
		
		System.out.println("The cpu's shot hit " + (cpu_missile_hit - X) + " meters from your base.");
		
		
		/* 
    below is the "STATE OF THE ART" AI algorithm. For the first round, values of the mins and the max modifiers are given. After that, this code changes those values based on whether or not the cpu undershoots or overshoots the player base. It continues to change based on over or undershooting during subsequent rounds of play. 
    */
			
		if ( cpu_missile_hit - X > 0 )	{ //cpu overshoots the player base
			aim_change[1] = cpu_choices[0] - aim_change[0]; //augments max angle modifier so that max angle on next turn = last angle choice
			aim_change[3] = cpu_choices[1] - aim_change[2]; //augments max speed modifier so that max speed on next turn = last speed choice
			
		}
		
		if ( cpu_missile_hit - X < 0 )	//if executes when the cpu undershoots the player base
		{
			aim_change[0] = cpu_choices[0]; //new degree min for next round = last degree choice 
			aim_change[2] = cpu_choices[1]; //new speed min for next round = last speed choice
			
			aim_change[1] = aim_change[1] - cpu_choices[0] + aim_change[0]; //this augments the max angle modifier to account for the min change
			aim_change[3] = aim_change[3] - cpu_choices[1] + aim_change[2]; //this augments the max speed modifier to account for the min change
		}
			
		numbers[2]++;	//makes the number of rounds go up, so that the givens for min, and max modifiers won't execute after first round of play
		return cpu_missile_hit;
	}// end cpu_turn
	
	public static double player_turn(Scanner scan, double X)
	{
		double plyr_deg_choice, plyr_speed_choice, plyr_missile_hit;
		
		System.out.print("Enter an angle: ");
		plyr_deg_choice = scan.nextDouble();
		System.out.print("Enter a speed: ");
		plyr_speed_choice = scan.nextDouble();
		
		plyr_missile_hit = hit_calc(plyr_deg_choice, plyr_speed_choice);	// calculates the distance the missile travels
		plyr_missile_hit = Math.floor(plyr_missile_hit);	// rounds result for easier viewability
		
		System.out.println("Your missile landed " + (plyr_missile_hit - X) + " meters from the cpu base.");
		
		return plyr_missile_hit;
	}
	
	public static double hit_calc(double degrees, double speed)
	{
		double missile_dist, radians;
		
		radians = Math.toRadians(degrees);
		missile_dist = ( speed * speed * Math.sin(2 * radians) ) / 9.8;	
		return missile_dist;
	}
	
	public static void game_rules(Scanner scan)
	{
		String confirm;
		
		System.out.print("*******************************************************************************\n\n");
		System.out.println("Welcome to Artill3ry, by CrudTech gaming division.\n");
		System.out.print("Would you like to see the rules of the game? (y/n): ");	
		confirm = scan.next();	
		System.out.print("\n\n");
			
		if ( confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y") )
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
	}//end game_rules
	
	public static double rand_generator(Random ran)
	{
		double decider;
		decider = ran.nextDouble();	//generates random double between 0-1
		
		return decider;
	}
	
	public static void win_congratulator(double playerwins, int game_no)
	{
		double win_percentage;
		win_percentage = playerwins / game_no;	
		
		System.out.println("Your win percentage is: " + win_percentage * 100 + "%.");
		
		if ( win_percentage <= .75 )
			System.out.print("Your failure vs. the shoddy AI of a novice coder should be commended!!...or not.\n");
		if ( win_percentage < .9 && win_percentage > .75 )
			System.out.print("Not bad...not great. Were you a middle child?\n");
		if ( win_percentage >= .9 )	
			System.out.print("Amazing! The battle between man and machine has been decided...erm...decisively.\n");
	}
}// end class




