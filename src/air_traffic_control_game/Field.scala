package air_traffic_control_game

/*
 * Represents a field where all the aircrafts moves
 * @param airport: is the airport in the centre of the field
 * @param wind: blowing wind in the field. Affects on the movements of aircrafts
 * @param north and west: height and width / 2
 */
class Field(val airport: Airport, val wind: Vector3D, val north: Double, val west: Double) {
  val windSpeed = this.wind.speed
  val windDirection = this.wind.angle
  val centre = this.airport.pos
  
  // Stores every aircrafts in the field. Only flying or outgoing aircrafts are included
  var aircrafts: Vector[Aircraft] = Vector[Aircraft]()
  
  // Aircrafts that are arriving to the airport
  def arrivals = this.aircrafts.filter(_.destination == this.airport)
  
  // Aircrafts that are outgoing
  def departures = this.aircrafts.filterNot(_.destination == this.airport)
  
  // Adds given aircraft to the list
  def addAircraft(aircraft: Aircraft): Unit = {
    this.aircrafts = this.aircrafts :+ aircraft
  }
  
  // Removes given aircraft from the list
  def removeAircraft(aircraft: Aircraft): Unit = {
    this.aircrafts = this.aircrafts.filterNot(_ == aircraft)
  }
  
  override def toString() = 
    "The field area of " + this.north * 2 + "x" + this.west * 2 + ", airport: " + this.airport.name + ", wind(speed & dir): "
    + this.windSpeed + " & " + this.windDirection
}