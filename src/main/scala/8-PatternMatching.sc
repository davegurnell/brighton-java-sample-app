object PatternMatching {

  // Pattern matching is a way to disect deep data structures and extract the values you need.
  
  // It uses a special pattern DSL that is part of Scala, but is different from regular Scala syntax
  // (even though it looks similar).
  
  // Capabilities are:
  //  - quickly pull apart (potentially nested) data structures;
  //  - see if certain fields match expected values;
  //  - bind certain fields to variables.
  
  // Example data:
  case class Tweet(val author: String, val text: String)

  val tweets = List(
  	Tweet("Dave",    "Hello world!"),
  	Tweet("World",   "Hi Dave!"),
  	Tweet("Richard", "Hello world also!"),
  	Tweet("World",   "Right back at you!")
  )                                               //> tweets  : List[PatternMatching.Tweet] = List(Tweet(Dave,Hello world!), Tweet
                                                  //| (World,Hi Dave!), Tweet(Richard,Hello world also!), Tweet(World,Right back a
                                                  //| t you!))
  
  // Examples:
  //  - find the text in the second tweet:
  tweets match {
    case List(_, Tweet(_, text), _, _) =>
      text
  }                                               //> res0: String = Hi Dave!

  //  - ascertain that the third tweet is from Richard:
  tweets match {
    case List(_, _, Tweet("Richard", _), _) =>
      true
  }                                               //> res1: Boolean = true
  
  //  - find the names of the first two authors in a list of ANY LENGTH!!!
  tweets match {
    case Tweet(name1, _) :: Tweet(name2, _) :: _ =>
      Some(name1 + " => " + name2)
    
    case _ => None
  }                                               //> res2: Option[String] = Some(Dave => World)
  
  // Syntax is:
  //
  // EXPRESSION match {
  //   case PATTERN => EXPRESSION ...
  //   case PATTERN => EXPRESSION ...
  //   case PATTERN => EXPRESSION ...
  //   ...
  // }
  //
  // It's like a switch statement with no fall-through from expression to expression.
  
  // How to define patterns:
  //  - there are predefined ones for many collections:
  //    List(...) Seq(...) Map(...) etc
  //  - you get one every time you define a case class
  //  - you can define your own "custom extractors" using specially named methods: "unapply" and "unapplySeq".
  
}