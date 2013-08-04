object Classes {

  // We're going to skip a lot of syntax and jump
  // straight to the "fast start" option - the "case class":
  case class Tweet(var author: String, var text: String)
  
  // Adding the "case" keyword to a class generates a heap of stuff for us:
  
  // - a constructor and a toString method
  val a = new Tweet("Dave", "Hello world!")       //> a  : Classes.Tweet = Tweet(Dave,Hello world!)
  
  // - getters and setters for each field
  a.author = "Richard"
  println(a.author + ": " + a.text)               //> Richard: Hello world!
  
  // - equals and hashCode methods
  a == new Tweet("Richard", "Hello world!")       //> res0: Boolean = true

  // - a function-style factory method
  val b = Tweet("World", "Hi both!")              //> b  : Classes.Tweet = Tweet(World,Hi both!)

  // - an extractor for pattern matching (we'll see this later)
   
}