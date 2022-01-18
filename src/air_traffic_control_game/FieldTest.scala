package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class FieldTest {
  val wind = new Vector3D(15, 15, 0)
  val width = 800
  val height = 500
  val helsinki = new Airport("Helsinki-Vantaa", width, height, true)
  val tokyo = new Airport("Tokyo-Narita", 0, 0, false)
  val j1 = {
    val destination = helsinki
    val origin = tokyo
    new Journey(2, origin, destination)
  }
  val j2 = {
    val origin = helsinki
    val destination = tokyo
    new Journey(1, origin, destination)
  }
  val maxFuel = 10000
  val consumption = 20
  val passengers = 400
  val pos01 = new Vector3D(300, 200, 10000)
  val pos02 = new Vector3D(400, 250, 0)
  val speed01 = new Vector3D(50, 20, 0)
  val speed02 = new Vector3D(0, 0, 0)
  val airline = new Airline("Finnair", "AY")
  val number = 350
  val a1 = new Aircraft(airline, number, j1, maxFuel, consumption, passengers, pos01, speed01, true)
  val a2 = new Aircraft(airline, number, j2, maxFuel, consumption, passengers, pos02, speed02, false)
  
  val field = new Field(helsinki, wind, height / 2, width / 2)
  
  @Test def testVector = {
    assertTrue("The vector containing aircrafts should be empty.", field.aircrafts.isEmpty)
    field.addAircraft(a1)
    field.addAircraft(a2)
    assertTrue("The vector should contain aircrafts.", field.aircrafts.head == a1)
    assertTrue("The vector should contain aircrafts.", field.aircrafts(1) == a2)
    field.removeAircraft(a1)
    assertTrue("The head of vector should be changed.", field.aircrafts.head == a2)
    field.removeAircraft(a2)
    assertTrue("The vector should be empty.", field.aircrafts.isEmpty)
  }
  
  @Test def testArrivalAndDeparture = {
    field.addAircraft(a1)
    field.addAircraft(a2)
    assertTrue("The vector Arrivals should contain an aircraft " + a1.code, field.arrivals.head == a1)
    assertTrue("The vector Departures should contain an aircraft " + a2.code, field.departures.head == a2)
  }
  
}