package tweetzor2.core

import com.untyped.location.Ngr
import scala.Option.option2Iterable

// Database of postcodes. `databaseUrl` must be a path to a CSV file on the classpath.
// The CSV file must be in [OS Codepoint](http://www.ordnancesurvey.co.uk/oswebsite/products/code-point/index.html) format.
class PostcodeDatabase(val databaseUrl: String) {

  // Find the closest postcode to `here`, or `None` if nothing is closer than `proximiyCutoff`.
  def nearestPostcode(here: Ngr): Option[String] = {
    ngrRecords.
      flatMap(proximityTo(here) _). // find records that are *"pretty close"*;
      sortBy(rec => rec.proximity). // sort by proximity;
      headOption.                   // grab the closest record;
      map(_.postcode)               // return its postcode.
  }
    
  // Database records
  // ----------------
  
  // A record in the postcode database.     
  private case class NgrRecord(val ngr: Ngr, val postcode: String)
  
  // Proximity of an `NgrRecord` to a location.
  // Used to compare records for proximity and discard them if they're too far away. 
  private case class ProximityRecord(val proximity: Long, val postcode: String)

  // List of all records in the database, loaded from CSV and cached in memory.
  // Declared as `lazy` to make the database load on demand.
  private lazy val ngrRecords: List[NgrRecord] = {
    val in = getClass.getResourceAsStream(databaseUrl)
    try {
      io.Source.fromInputStream(in).getLines.flatMap(parseNgrRecord _).toList
    } catch {
      case _ : Exception => Nil
    } finally {
      in.close()
    }
  }

  // Proximity
  // ---------
    
  // Constant to determine how close a postcode needs to be before we consider it "nearby".
  // We look at it if the square of the distance (in easting/northing units) is less than this number. 
  private val proximityCutoff = 10000
    
  // Return a `ProximityRecord` for this `there` relative to `here` if it is close enough.
  private def proximityTo(here: Ngr)(there: NgrRecord): Option[ProximityRecord] = {
    val east = (there.ngr.easting - here.easting).toLong
    val north = (there.ngr.northing - here.northing).toLong
    val squareDist = east * east + north * north
    if(squareDist < proximityCutoff) {
      Some(ProximityRecord(-squareDist, there.postcode))
    } else {
      None
    }
  }
  
  // CSV parsing
  // -----------
    
  private def parseNgrRecord(line: String): Option[NgrRecord] = {
    Some(line.split(",").toList).collectFirst {
      case Quoted(postcode) :: _ :: AsInt(easting) :: AsInt(northing) :: _ =>
        NgrRecord(Ngr(easting, northing), postcode)
    }
  }

  // Extractor for quoted strings
  private object Quoted {
    val regex = "\"([^\"]*)\"".r
    def unapply(in: String): Option[String] =
      regex.findFirstIn(in)
  }

  // Extractor for integers
  private object AsInt {
    def unapply(in: String): Option[Int] =
      try { Some(in.toInt) } catch { case _ : NumberFormatException => None } 
  }
  
}