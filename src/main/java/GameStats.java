package src.main.java;
public class GameStats {
  private int gamesWon;
  private int gamesPlayed;
  private int roundNum;

  public GameStats(int _gamesWon, int _gamesPlayed, int _roundNum) {
    this.gamesWon = _gamesWon;
    this.gamesPlayed = _gamesPlayed;
    this.roundNum = _roundNum;
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

  public void setGamesPlayed(int _gamesPlayed) {
    this.gamesPlayed = _gamesPlayed;
  }

  public void incrementGamesPlayed() {
    this.gamesPlayed++;
  }

  public int getRoundNum() {
    return roundNum;
  }

  public void setRoundNum(int _roundNum) {
    this.roundNum = _roundNum;
  }

  public void incrementRoundNum() {
    this.roundNum++;
  }
}
