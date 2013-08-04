object Collections {
  // Some of the best examples of higher order functions are FP classics:
  // how to filter and transform sequences of data.
  
  // We'll look at this with Scala's List class
  //   (which is different from Java's List class)
  
  case class Tweet(val author: String, val text: String)

  val tweets = List(
  	Tweet("Dave",    "Hello world!"),
  	Tweet("World",   "Hi Dave!"),
  	Tweet("Richard", "Hello world also!"),
  	Tweet("World",   "Right back at you!")
  )                                               //> tweets  : List[Collections.Tweet] = List(Tweet(Dave,Hello world!), Tweet(Wor
                                                  //| ld,Hi Dave!), Tweet(Richard,Hello world also!), Tweet(World,Right back at yo
                                                  //| u!))
  // We'll look at three of the methods of List: filter, map, and flatMap.
  
  
  // filter
  // ------
  
  // Method on List that builds a new list containing items that meet some criteria.
  
  // Hypothetical scenario. Suppose "tweets" was a Java ArrayList<Tweet>.
  // How would we filter it for tweets with the word "world" in them?
  
  // (scroll down for the solution)
  
  
  
  
  
  
   
  
  
  /*
  List<Tweet> filterForWorld(List<Tweet> tweets) {
    List<Tweet> ans = new ArrayList<Tweet>();

    for(Tweet tweet : tweets) {
      if(tweet.text.contains("world")) {
        ans.add(tweet);
      }
    }

    return ans;
  }
  */
  
    
  
  // What about filtering for tweets written by Dave?
  
    
  
  
  /*
  List<Tweet> filterForDave(List<Tweet> tweets) {
    List<Tweet> ans = new ArrayList<Tweet>();

    for(Tweet tweet : tweets) {
      if(tweet.author.equals("Dave")) {
        ans.add(tweet);
      }
    }

    return ans;
  }
  */




  // What about abstracting over these two? Only 1 line in the 9 is different!
  


  // With first class functions this is easy:
  
  /*
  List<Tweet> filter(List<Tweet> tweets, Function1<Tweet, Boolean> test) {
    List<Tweet> ans = new ArrayList<Tweet>();

    for(Tweet tweet : tweets) {
      if(test(tweet)) {
        ans.add(tweet);
      }
    }

    return ans;
  }
  */
  
  // This is exactly how the filter method on List works
  // (although it's written in Scala, not in Java).
  
  // Here's the type signature:
  //
  // class List[A] {
  //   def filter(func: A => Boolean): List[A]
  // }

  // With Scala's function syntax this becomes extremely powerful:
  tweets.filter((tweet: Tweet) => tweet.text.contains("world"))
                                                  //> res0: List[Collections.Tweet] = List(Tweet(Dave,Hello world!), Tweet(Richar
                                                  //| d,Hello world also!))
  
  // Applying some syntactic sugar makes the code even prettier:
  tweets.filter(tweet => tweet.text.contains("world"))
                                                  //> res1: List[Collections.Tweet] = List(Tweet(Dave,Hello world!), Tweet(Richar
                                                  //| d,Hello world also!))
  
  
  // map
  // ---
  
  // Transforms a list of one type of item into a list of another type of item.

  // Here's the type signature:
  //
  // class List[A] {
  //   def map(func: A => B): List[B]
  // }
  //
  // "func" is a mapping function.
  // "map" applies "func" to each item in the list and builds a list of results.
  
  // Examples:
  
  // - get the author of each tweet
  tweets.map(tweet => tweet.author)               //> res2: List[String] = List(Dave, World, Richard, World)

  // - get the length of each tweet
  tweets.map(_.text.length)                       //> res3: List[Int] = List(12, 8, 17, 18)
  
  
  
  // flatMap
  
  // What if you want to do a map over a list, but there isn't a one-to-one mapping from
  // items in the source list and items in the destination list?
  
  // Here's the type signature (simplified for the purposes of illustration):
  //
  // class List[A] {
  //   def flatMap(func: A => List[B]): List[B]
  // }
  
  // "flatMap" applies "func" to each item in the list and appends the results into a new list.
  
  // Examples:
  
  // - get a list of all the words in the tweet bodies
  tweets.map(tweet => tweet.text.split(" ").toList)
                                                  //> res4: List[List[String]] = List(List(Hello, world!), List(Hi, Dave!), List(
                                                  //| Hello, world, also!), List(Right, back, at, you!))
  
  // The Scala collections framework
  // -------------------------------
  
  // Scala has its own collections framework,
  // built from the ground up with operations like this in mind.
  
  // There are lots of data structures: mutable, immutable, lazy, eager, etc.
  // All have a consistent API consisting of many methods like these.
  
  // What works for lists also works for arrays, buffers, vectors, sets, streams,
  // and so on.
  
  // Here's an example of what you can do:
  
  // - find all words in all tweets, sorted by length:
  tweets
    .flatMap(_.text.split(" ").toList) // split into words
    .map(_.toLowerCase)                // lowercase them all
    .groupBy(_.length)                 // group by length (builds a Map)
    .toList                            // convert back to a List
    .sortBy(_._1)                      // sort by frequency
    .foreach(println)                             //> (2,List(hi, at))
                                                  //| (4,List(back, you!))
                                                  //| (5,List(hello, dave!, hello, world, also!, right))
                                                  //| (6,List(world!))
}