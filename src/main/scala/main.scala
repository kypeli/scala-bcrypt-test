import java.security.SecureRandom

import org.springframework.security.crypto.bcrypt.BCrypt

object BCryptPerfTest {
  val debug = false
  val iterations = 100

  def main(args: Array[String]) = {
    val passwordGenStartTime = System.currentTimeMillis
    val appStartTime = passwordGenStartTime
    println(s"Generating $iterations passwords and hashing them with 'Bcrypt.hashpw()'...")

    val passwords =
      for {
        i <- 1 to iterations
        password = generatePassword(length=32)
      } yield Password(password, BCrypt.hashpw(password, BCrypt.gensalt()))
    val passwordGenStopTime = System.currentTimeMillis
    println("Done.\n")

    println("Checking password hashes with 'BCrypt.checkpw()'...")
    val hashCheckStartTime = System.currentTimeMillis
    passwords.foreach {
      password => debug(password.plainText + " " + password.hash)
      if (!BCrypt.checkpw(password.plainText, password.hash)) {
        println(s"ERROR: Password hash does not match!")
      }
    }
    val hashCheckStopTime = System.currentTimeMillis
    val appStopTime = hashCheckStopTime
    println("Done.")

    val passwordGenTime = passwordGenStopTime - passwordGenStartTime
    val passwordGenAveTime = passwordGenTime / iterations
    val hashCheckTime = hashCheckStopTime - hashCheckStartTime
    val hashCheckAveTime = hashCheckTime / iterations
    val totalTime = appStopTime - appStartTime
    println(s"""
      |Generating $iterations password hashes took: $passwordGenTime ms (on average $passwordGenAveTime ms)
      |Checking $iterations password hashes took: $hashCheckTime ms (on average $hashCheckAveTime ms)
      |Total execution time: $totalTime ms
      """.stripMargin)
  }

  private def generatePassword(length: Int): String = {
    val random = new scala.util.Random(new java.security.SecureRandom())
    val alphabet = "abcdefghijklmnopqrstuvwxyz0123456789^!#%&/()_-"
    Stream.continually(random.nextInt(alphabet.size))
      .map(alphabet)
      .take(length)
      .mkString
  }

  private def debug(m: String): Unit = {
    if (debug) println(s"DEBUG: $m")
  }

  case class Password(plainText: String, hash: String)
}
