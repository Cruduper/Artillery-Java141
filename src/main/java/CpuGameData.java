package src.main.java;

public class CpuGameData {
  		private double degrees;	//final cpu degrees choice
			private double speed;	//final cpu speed (in m/s) choice

          // example: minBound = 25, maxOffset = 20, then maxBound would be 45
      private double minDegreeBound;
      private double maxDegreeOffset;
      private double minSpeedBound;
      private double maxSpeedOffset;


      public CpuGameData(double _minDegBound, double _maxDegOffset, double _minSpdBound, double _maxSpdOffset, double _degrees, double _speed) {
        this.minDegreeBound = _minDegBound;
        this.maxDegreeOffset = _maxDegOffset;
        this.minSpeedBound = _minSpdBound;
        this.maxSpeedOffset = _maxSpdOffset;
        this.degrees = _degrees;
        this.speed = _speed;
      }

      public CpuGameData() { //overloaded constructor with default values
        this(25, 20, 55, 35, 0, 0); //calls the main constructor above
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

      public void setMinDegreeBound(double _minDegBound) {
        this.minDegreeBound = _minDegBound;
      }

      public double getMinDegreeBound() {
        return this.minDegreeBound;
      }
      
      public void setMaxDegreeOffset(double _maxDegOffset) {
        this.maxDegreeOffset = _maxDegOffset;
      }

      public double getMaxDegreeOffset() {
        return this.maxDegreeOffset;
      }
      
      public void setMinSpeedBound(double _minSpdBound) {
        this.minSpeedBound = _minSpdBound;
      }

      public double getMinSpeedBound() {
        return this.minSpeedBound;
      }
      
      public void setMaxSpeedOffset(double _maxSpdOffset) {
        this.maxSpeedOffset = _maxSpdOffset;
      }

      public double getMaxSpeedOffset() {
        return this.maxSpeedOffset;
      }
}
