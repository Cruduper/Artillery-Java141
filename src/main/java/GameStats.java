package src.main.java;
public class GameStats {
  private int gamesWon;
  private int gamesPlayed;
  private int roundsPlayed;

  public GameStats(int _gamesWon, int _gamesPlayed, int _roundsPlayed) {
    this.gamesWon = _gamesWon;
    this.gamesPlayed = _gamesPlayed;
    this.roundsPlayed = _roundsPlayed;
  }

  public int getGamesWon() {
    return gamesWon;
  }

  public void setGamesWon(int gamesWon) {
    this.gamesWon = gamesWon;
  }

  public void incrementGamesWon() {
    this.gamesWon++;
  }

  public int getGamesPlayed() {
    return gamesPlayed;
  }

  public void setGamesPlayed(int gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
  }

  public void incrementGamesPlayed() {
    this.gamesPlayed++;
  }

  public int getRoundsPlayed() {
    return roundsPlayed;
  }

  public void setRoundsPlayed(int roundsPlayed) {
    this.roundsPlayed = roundsPlayed;
  }

  public void incrementRoundsPlayed() {
    this.roundsPlayed++;
  }
}
