package air_traffic_control_game

/**
 * Represents an airport
 * @param name is the name of the airport
 * @param width and height is the width and height of the map (and field)
 * @param isCentre: if the airport is in the centre of the map or not
 */
class Airport(val name: String, val width: Double, val height: Double, isCentre: Boolean) {
  val pos: Vector3D = new Vector3D(if(isCentre) width / 2 else 0, if(isCentre) height / 2 else 0, 0)
  
  // Stores runways in the airport
  var runways: Vector[Runway] = Vector[Runway]()

  // Returns the runway wrapped in Option if the runway named given string exists, otherwise returns None
  def findRunway(runwayName: String): Option[Runway] = {
    this.runways.find(_.name.toLowerCase() == runwayName)
  }

  // Returns true if there is a runway named given string
  def runwayFound(runwayName: String): Boolean = {
    this.findRunway(runwayName).isDefined
  }

  // Returns true if the runway named given string is using
  def isUsing(runwayName: String): Boolean = {
    val runway = this.findRunway(runwayName)
    runway.isDefined && runway.forall(_.isUsing)
  }

  // Uses runway, if it's not using already
  def useRunway(runwayName: String): String = {
    if (this.isUsing(runwayName)) {
      "This runway is currently using."
    } else {
      val runway = this.findRunway(runwayName).get
      runway.use()
      "The aircraft is going to use the runway " + runway.name
    }
  }

  // Called when taking off and "removes" the aircraft from the runway
  def removeFromRunway(runwayName: String): String = {
    if (this.runwayFound(runwayName)) {
      this.findRunway(runwayName).foreach(_.remove())
      "The take-off succeeded."
    } else {
      "A runway " + runwayName + " not found."
    }
  }

  // Adds new runway to the list
  def setRunway(runway: Runway) = {
    runways = runways :+ runway
  }

  def ==(other: Airport) = {
    this.name == other.name && this.pos == other.pos
  }

  override def toString() = this.name + " located at " + this.pos.toString() + ", runways:\n" + runways.mkString("\n")
}