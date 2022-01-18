package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class VectorTest {
  
  val x1 = 50
  val y1 = 100
  val z1 = 0
  val x2 = 100
  val y2 = 20
  val z2 = 1000
  
  val original = new Vector3D(x1, y1, z1)
  val second = new Vector3D(x2, y2, z2)
  val copyOfOriginal = new Vector3D(x1, y1, z1)
  
  @Test def testSum = {
    val sum = original + second
    val expected = new Vector3D(150, 120, 1000)
    assertTrue("Incorrect sum, was " + sum + ", but expected " + expected, sum == expected)
  }
  
  @Test def testDistace = {
    val dis = original.distance(second)
    assertTrue("Incorrect distance", dis.toInt == 94)
  }
  
  @Test def testAngle = {
    val angle = second.dAngle(original)
    val expected = 1
    assertTrue("Incorrect angle, was " + angle.toInt + ", expected " + expected, angle.toInt == 1)
  }
  
  @Test def testEquals = {
    val eqa = original == copyOfOriginal
    assertTrue("Identical vectors were not equal.", eqa)
  }
  
}