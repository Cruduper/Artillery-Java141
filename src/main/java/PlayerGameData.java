package src.main.java;

public class PlayerGameData {
  private double degrees; // final cpu degrees choice
  private double speed; // final cpu speed (in m/s) choice

  public PlayerGameData(double _degrees, double _speed) {
    this.degrees = _degrees;
    this.speed = _speed;
  }

  public PlayerGameData() { //overloaded constructor with default values
    this(0, 0); //calls the main constructor above
  }

  public double getDegrees() {
    return this.degrees;
  }

  public void setDegrees(double _degrees) {
    this.degrees = _degrees;
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setSpeed(double _speed) {
    this.speed = _speed;
  }
}
