object VariablesAndMethods {

  // Variables defined with val (immutable) and var (mutable) keywords:
  var a: Int = 123                                //> a  : Int = 123
  val b: String = "abc"                           //> b  : String = abc

  // The types can be omitted where they can be inferred:
  val c = 234                                     //> c  : Int = 234
  var d = "bar"                                   //> d  : String = bar

  // Methods are defined with def.
  // Argument types must be stated, but the return type can be omitted.
  def add(a: Int, b: Int): Int = {
  	a + b
  }                                               //> add: (a: Int, b: Int)Int
  
  add(1, 2)                                       //> res0: Int = 3
  
  // Use of the "return" keyword is discouraged.
  // Methods return the value of the last expression in the body:
  def doStuff() = {
    1
    "abc"
    2.0
  }                                               //> doStuff: ()Double
  
  // All Scala statements are also expressions:
  def max(a: Int, b: Int) = {
    if(a > b) a else b
  }                                               //> max: (a: Int, b: Int)Int
  
  max(2, 3)                                       //> res1: Int = 3
  
  // "void" is replaced by "Unit", which is a real type with a single value, "()"
  val e = println("Side effects rule!")           //> Side effects rule!
                                                  //| e  : Unit = ()

}