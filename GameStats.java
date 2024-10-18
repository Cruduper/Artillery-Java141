public class GameStats {
  private int gamesWon;
  private int gamesPlayed;
  private int roundsPlayed;

  public GameStats() {
    this.gamesWon = 0;
    this.gamesPlayed = 0;
    this.roundsPlayed = 1;
  }

  public int getGamesWon() {
    return gamesWon;
  }

  public void setGamesWon(int gamesWon) {
    this.gamesWon = gamesWon;
  }

  public int getGamesPlayed() {
    return gamesPlayed;
  }

  public void setGamesPlayed(int gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
  }

  public int getRoundsPlayed() {
    return roundsPlayed;
  }

  public void setRoundsPlayed(int roundsPlayed) {
    this.roundsPlayed = roundsPlayed;
  }
}
