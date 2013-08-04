object Options {
  
  // Null is evil. Options are a way of avoiding it.
  
  // Option is a Scala collection type with two subtypes:
  //  - Some is a class containing a single value;
  //  - None is a singleton object containing no values.
  
  // Examples:
  val a : Option[Int] = Some(1)                   //> a  : Option[Int] = Some(1)
  val b : Option[Int] = Some(2)                   //> b  : Option[Int] = Some(2)
  val c : Option[Int] = None                      //> c  : Option[Int] = None

  // We use Options to represent values that we may or may not be able to calculate:
  def divide(a: Int, b: Int) =
    if(b == 0) None else Some(a / b)              //> divide: (a: Int, b: Int)Option[Int]

  divide(10, 2)                                   //> res0: Option[Int] = Some(5)
  divide(10, 0)                                   //> res1: Option[Int] = None

  // The main difference with null is that we can't accidentally
  // treat an option like a raw value:
  
  // a + 5 // ==> compile error
  
  // Instead we have to unpack them in one of several ways:
  
  //  - using a method called "get"
  a.get                                           //> res2: Int = 1
  b.get                                           //> res3: Int = 2
  // c.get // throws NoSuchElementException
  
  //  - using a method called "getOrElse"
  a.getOrElse(999)                                //> res4: Int = 1
  b.getOrElse(999)                                //> res5: Int = 2
  c.getOrElse(999)                                //> res6: Int = 999
  
  //  - using pattern matching (we'll come on to this in a bit)
  
  // Option is one of Scala's collection types, so it has all of the methods we know and love:
  
  a.filter(_ % 2 == 0)                            //> res7: Option[Int] = None
  b.filter(_ % 2 == 0)                            //> res8: Option[Int] = Some(2)
  c.filter(_ % 2 == 0)                            //> res9: Option[Int] = None

  a.map(_ * 2)                                    //> res10: Option[Int] = Some(2)
  b.map(_ * 2)                                    //> res11: Option[Int] = Some(4)
  c.map(_ * 2)                                    //> res12: Option[Int] = None

  a.flatMap(a => b.map(b => List(a, b)))          //> res13: Option[List[Int]] = Some(List(1, 2))

  // In summary...
  //  - We create an option when we're unsure whether we'll be able to compute a value.
  //  - We use standard collection methods to manipulate it and pass it around.
  //  - Finally, we extract the value we need and deal with the possibility of a None.
  //  - JUST SAY NO TO NULL !!!

}