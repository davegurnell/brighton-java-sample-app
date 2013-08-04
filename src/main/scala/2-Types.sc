object Types {

	// Scala type hiearachy unifies all types under one supertype, Any:
  //
	//  - Any           - grand supertype of EVERYTHING
	//
	//     - AnyVal     - all "value types"
	//        - Int     - same as Java int
	//        - Double  - same as Java double
	//        - etc...
	//
	//     - AnyRef     - same as java.lang.Object - all "reference types"
  //        - String  - same as java.lang.String
  //        - arrays  - same as Java arrays
  //        - all Java classes
  //        - all Scala classes

  // Other differences that Scala eliminates:
  
  // Value types have methods (via compiler magic - no object wrappers)
  42.toChar                                       //> res0: Char = *
  
  // == and != compare value equality across the board:
  "foo" == "foo"                                  //> res1: Boolean = true

}