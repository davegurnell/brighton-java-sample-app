object Functions {
  
  // First class functions: functions that are also values.
  
  // How would we implement this in Java?
  
  // (scroll down for the solution)...
  
  
  
























  
  // The JVM doesn't have a native closure type, so we'll
  // probably have to represent it as an object with a single method.
   
  /*
  public interface Function2<A, B, Result> {
    public Result apply(A a, B b);
  }
  
  Funtion2<int, int, int> add = new Function2<int, int, int>() {
    public int apply(int a, int b) {
      return a + b;
    }
  }
  
  add.apply(1, 2);
  */
  
  // This is very verbose ...
   
   
   
   
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  // ... however, it's actually how functions are implemented in Scala:
  val add = new Function2[Int, Int, Int]() {
  	def apply(a: Int, b: Int) =
  		a + b
  }                                               //> add  : (Int, Int) => Int = <function2>

  add.apply(1, 2)                                 //> res0: Int = 3
  
  
  
  // Fortunately, Scala provides special syntax to define function literals:
  val add2 = (a: Int, b: Int) => a + b            //> add2  : (Int, Int) => Int = <function2>
  
  // There's syntactic sugar for function calls that works with any object that has an "apply" method:
  add2(1, 2)                                      //> res1: Int = 3
  add2.apply(1, 2)                                //> res2: Int = 3
  
  // There's even a nice "arrow syntax" for function types:
  val add3: (Int, Int) => Int = add2              //> add3  : (Int, Int) => Int = <function2>
  
  
  
  // First class functions are useful because they allow us to write "higher order" functions
  // that consume and generate other functions:
  
  val add1 = (a: Int) => a + 1                    //> add1  : Int => Int = <function1>
  val double = (a: Int) => a * 2                  //> double  : Int => Int = <function1>

  val doBoth = (a: Int => Int, b: Int => Int) =>
						     (num: Int) =>
						     	 b(a(num))
                                                  //> doBoth  : (Int => Int, Int => Int) => Int => Int = <function2>
  
  doBoth(add1, double)(10)                        //> res3: Int = 22
  doBoth(double, add1)(10)                        //> res4: Int = 21
}