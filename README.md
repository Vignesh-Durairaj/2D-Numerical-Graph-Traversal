# 2D Traversal of a numerical graph
A Java based solution for a challenging scenario where to traverse a lengthy and also deepest skiing route referring a digital 2D map of numerals. Refer this [this page](http://geeks.redmart.com/2015/01/07/skiing-in-singapore-a-coding-diversion/).  

* Programming Language used : `Java` - version 8
* `Map.txt` has been parsed into a two dimensional array of integer with
  * Heavy use of `Java 8 Streams`
  * Implementation of functional interfaces using `Lambdas`


**Solution Approach**
---

1. Used Java Stream to convert the input file into a two dimensional array of `int`
2. Below are the validations used
   1. check for the first row and validates the file contains the correct row and column of data
   2. check for numbers and throws exception in case of any non-numbers encountered
3. Created an object name `SkiPoint` that contains its elevation value, current vertical point and horizontal point
4. Iterating over each and every value in the two-dimensional `int` array to get the longest path covered from each `SkiPoint`
5. Getting a list of `SkiPoint` object from the above step, they all are collated into a list of lists
6. From this state the longest of all will be filtered and then the steepest of them will be filtered
7. Finally printing out the length covered and the depth covered along with the elevations points covered (with its _height_ and _width_ axis]

**Getting the longest path for each node**

* For each node it traverse to the adjacent node which is lying lower than the current position and add them in a list
* It travels to all possible combinations for each node it visits (as in **DEPTH FIRST TRAVERSAL**)
* Each of the traversal path is checked for the length and depth covered. In case of a tie for equal length and equal depth, then the steepest end point was chosen (since the traversal list will be added from the last `SkiPoint` being visited.
* In case there is a tie between few traversal path in terms of both length and depth of traversal, then both will be printed as a result.

**Running the application**
---

Built the application using the script

```shell
$ mvn clean install
```

Once the JAR file has been built, the application can be ran as below

```shell
$ cd <go/to/jar/path>
$ java com.vikhi.skiing.SkiingExecutor <map_file.txt>
```

It has a dependency of `Log4j` and should include this jar either as a part of Maven run time or in other runtime environment. 


**Future**

The above code base has a huge room for improvement and these will be added en-course of time.

> Look for the simplest and the shortest solution for any problem, rather than complicating it further
