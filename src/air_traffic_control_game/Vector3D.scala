package air_traffic_control_game

/**
 * Represents a vector, in this game used as position and speed
 * If it's position: x, y and z are coordinates of the position
 * If speed: x and y are the growth of positions and z = 0.
 */
class Vector3D(val x: Double, val y: Double, z: Double) {
  val speed = math.hypot(x, y)
  val angle  = math.atan2(y, x)
  val height = z
  
  /**
   * create a new vector with a new angle and speed
   */  
  def changeAandS(newAngle: Double, newSpeed: Double) = {
    new Vector3D(math.cos(newAngle) * newSpeed, math.sin(newAngle) * newSpeed, z)
  }
  
  /**
   * create a new vector with a new height
   */
  def changeHeight(height: Double) = {
    new Vector3D(x, y, height)
  }
  
  /**
   * Add two vectors and return their sum vector
   */
  def + (other: Vector3D) = {
    new Vector3D(x + other.x, y + other.y, height + other.height)
  }
  
  def - (other: Vector3D) = {
    new Vector3D(x - other.x, y - other.y, height - other.height)
  }
  
  /**
   * take subtract from two vectors and returns distance as Double
   */
  def distance(other: Vector3D) = {
    math.hypot(x - other.x, y - other.y)
  }
  
  /**
   * Angle between the line formed by this and other position, and x-axis
   */
  def dAngle(other: Vector3D) = {
    math.atan2(other.y - y, x - other.x)
  }
  
  
  def == (other: Vector3D) = {
    x == other.x && y == other.y && height == other.height
  }
  
  override def toString() = {
    val direction = if(this.height != 0) " with speed: " + this.speed + " and angle: " + this.angle
    "x: " + this.x + ", y: " + this.y + ", height: " + this.height + direction 
  }
}