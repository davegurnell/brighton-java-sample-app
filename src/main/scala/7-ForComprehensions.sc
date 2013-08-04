object ForComprehensions {

  // Question: how do we add two optional Ints together?
  
  val optA : Option[Int] = Some(123)              //> optA  : Option[Int] = Some(123)
  val optB : Option[Int] = Some(234)              //> optB  : Option[Int] = Some(234)
  
  // We can't just use "plus":
  
  // optA + optB // compile error
  
  
  
  // We need to define the behaviour of our code when one or both of the options is None:
  //
  //  optA | optB | result
  // ------+------+--------
  //  None | None |
  //  Some | Some |
  //  Some | None |
  //  Some | Some |
  
  
  
  // (scroll down for code)
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  // Answer: using flatMap and map !!!
  optA.flatMap(a => optB.map(b => a + b))         //> res0: Option[Int] = Some(357)
  
  // Splitting this across multiple lines for clarity...
  optA.flatMap { a =>
    optB.map { b =>
      a + b
    }
  }                                               //> res1: Option[Int] = Some(357)
  
  // What about adding three optional Ints?
  
  
  
  
    
  
  
  
  
  
  
  val optC : Option[Int] = Some(345)              //> optC  : Option[Int] = Some(345)
  
  optA.flatMap { a =>
    optB.flatMap { b =>
      optC.map { c =>
        a + b + c
      }
    }
  }                                               //> res2: Option[Int] = Some(702)
  
  
  
  
  
  
  // This is getting unwieldy!
  
  // However, there is a pattern emerging:
  //   flatMap ... flatMap ... flatMap ... map
  
  // It turns out that this pattern is really common...
  //  - not only when composing options;
  //  - but also when iterating over lists and sequences.
  
  // Because of this, Scala has a special syntax for it: "for comprehensions".
  // Despite the name, these have very little to do with Java for loops:
  for {
    a <- optA
    b <- optB
    c <- optC
  } yield a + b + c                               //> res3: Option[Int] = Some(702)

  // The semantics are exactly the same as the above but the syntax is much nicer.
  
  // We can also use for to iterate over lists and ranges and other sequences:
  for {
    a <- List(1, 2, 3)
    b <- 1 to 3
  } yield a * b                                   //> res4: List[Int] = List(1, 2, 3, 2, 4, 6, 3, 6, 9)
  
  // In fact, we can use them with any class that has a map and flatMap method.
  
  // For comprehensions are used all over the place in Scala,
  // to comprehend all sorts of types of structures.
  //
  // (Fan favorite - Futures - non-blocking sequences of asynchronous computations !!)

}