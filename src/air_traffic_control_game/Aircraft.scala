package air_traffic_control_game

import java.awt.Graphics2D
import java.io.File
import javax.imageio._

class Aircraft(
  val airline:     Airline,
  val number:      Int,
  val journey:     Journey,
  val maxFuel:     Int,
  val consumption: Int,
  val passengers:  Int,
  pos0:            Vector3D,
  speed0:          Vector3D,
  arriving:        Boolean) {

  val destination = this.journey.destination
  val origin = this.journey.origin
  val code: String = this.airline.carrierCode + this.number + "-" + this.origin.name.head + this.destination.name.head + this.journey.order
  var fuel: Int = maxFuel
  var pos: Vector3D = pos0
  var speed: Vector3D = speed0
  var usingRunwayName: String = ""

  var isFlying: Boolean = arriving
  var isWaiting: Boolean = false
  var isCrashed: Boolean = false

  // If the distance between this aircraft and the destination is smaller or equal to 250, return true otherwise false
  def isNear: Boolean = this.distance <= 250

  /**
   * returns a distance between this aircraft and destination airport
   */
  def distance: Double = {
    this.pos.distance(this.destination.pos)
  }

  // Returns whether this aircraft is inside the area which has width and height of given
  def isInside(width: Double, height: Double): Boolean = {
    this.pos.x >= 0 && this.pos.x <= width && this.pos.y >= 0 && this.pos.y <= height
  }
  
  // Returns true if journey has value Some(false), otherwise false
  def isFailed = !this.journey.succeeded.forall(b => b)


  private def dAngle: Double = {
    this.speed.speed / distance
  }

  // Stores the name of runway which this aircraft is going to use
  private def useRunway(runwayName: String): Unit = {
    this.usingRunwayName = runwayName
  }
  
  
  def success = this.journey.success()
  def failed = this.journey.failure()
  
  // Used when this aircraft goes out of the field
  def crash() = this.isCrashed = true

  /**
   * changes this aircrafts speed and angle
   */
  def changeAandS(newAngle: Double, newSpeed: Double): String = {
    this.speed = this.speed.changeAandS(newAngle, newSpeed)
    "Aircraft " + code + " changed angle and speed, new angle and speed: " + this.pos.angle + " & " + this.pos.speed
  }

  /**
   * changes height of this aircraft
   */
  def changeHeight(height: Double): String = {
    this.pos = this.pos.changeHeight(height)
    "Aircraft " + code + " changed height, new height: " + this.pos.height
  }

  /**
   * waits landing by making circle in certain distance from airport or waits taking off by stopping at the runway
   */
  def standBy(runwayName: String): String = {
    if (this.isWaiting) {
      "This aircraft is already stand-by."
    } else {
      if (this.isFlying) {
        if (this.isNear) {
          this.isWaiting = true
          "Aircraft " + code + " stand-by to land successfully."
        } else {
          "The airport is too far away."
        }
      } else {
        if (runwayName.isEmpty()) {
          "To stand-by on ground, please type runway."
        } else if (!this.origin.runwayFound(runwayName)) {
          "A runway " + runwayName + " not found."
        } else {
          if (!this.origin.isUsing(runwayName)) {
            this.isWaiting = true
            this.useRunway(runwayName)
          }
          this.origin.useRunway(runwayName)
        }
      }
    }
  }

  /**
   * Takes off from the airport if possible
   * Take-off is possible when:
   * - The aircraft is on ground
   * - The aircraft is waiting (stand-by)
   * - Given runway name is correct
   */
  def takeOff(runwayName: String): String = {
    if (!this.isFlying) {
      if (this.isWaiting) {
        if (this.usingRunwayName == runwayName) {
          this.isWaiting = false
          this.isFlying = true
          this.speed = new Vector3D(50, 50, 0)
          this.pos = new Vector3D(this.pos.x, this.pos.y, 8000)
          if (this.origin.runwayFound(runwayName)) this.success
          this.origin.removeFromRunway(runwayName)
        } else {
          "Aircraft " + code + " is not using runway " + runwayName
        }
      } else {
        "Please stand-by before taking off."
      }
    } else {
      "Flying aircraft is not able to take off."
    }
  }

  /**
   * Lands to the airport if possible.
   * Landing is possible when the aircraft is in the air and is waiting, and also the given runway must be unusing
   */
  def land(runwayName: String): String = {
    if (this.isFlying) {
      if (this.isWaiting) {
        if (this.isNear) {
          if (!this.destination.isUsing(runwayName)) {
            this.isWaiting = false
            this.isFlying = false
            this.pos = this.destination.pos
            this.speed = new Vector3D(0, 0, 0)
            this.useRunway(runwayName)
            this.journey.success()
            "The landing succeeded."
          } else {
            "The runway is currently using. Please order the aircraft currently standing-by on the ground to take-off."
          }
        } else {
          "The airport is too far away at the moment."
        }
      } else {
        "To land, the aircraft have to stand-by first."
      }

    } else {
      "Only flying aircrafts are able to land."
    }
  }

  /**
   * Moves this aircraft. When this aircraft is waiting in the air the aircraft makes circle, otherwise moves straight forward
   */
  def move(wind: Vector3D): Unit = {
    if (!this.isCrashed) {
      val cons = if (isFailed) consumption * 10 else consumption
      if (this.isFlying) {
        if (this.isWaiting) {
          val alfa = this.pos.dAngle(this.destination.pos)
          val beta = math.acos((1 - this.speed.speed * this.speed.speed / (2 * distance * distance)))
          val gamma = alfa + beta
          val newX = distance * math.cos(gamma) + this.destination.pos.x
          val newY = -distance * math.sin(gamma) + this.destination.pos.y
          this.pos = new Vector3D(newX, newY, this.pos.height)
        } else {
          pos = pos + speed + wind
        }
        fuel -= cons
      }

      if (fuel <= 0) {
        this.crash()
      }
    }
  }

  override def toString() = {
    val usingRunway = if (!this.isFlying && this.isWaiting) ", Using runway: " + this.usingRunwayName else ""
    "An aircraft of " + this.airline.name + ", code: " + this.code + ", remaining fuel: " + this.fuel + "/" + this.maxFuel +
      ", distance: " + this.distance.toInt + usingRunway
  }

  //+ " with " + this.passengers + " passengers, pos & speed: " + this.pos.toString() + " & " + this.speed.toString()
}