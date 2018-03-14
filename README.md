# log-analyser

## Technology Stack -
* Java 8
* Maven

## Steps to execute the code -

1. Extract the pan-takehome.zip file.
2. Execute the following commands:

```
cd pan-takehome 
```

3. Add your input file to this directory
4. Execute the following command:
```
java -jar loganalyser.jar <input_file> [delimiter]

java -jar loganalyser.jar log_file4.txt 
124,3,1508723554000 
```
## Assumptions -

1. For same user, if there are multiple entries in log file with same timestamp, then we will ignore the duplicates
```
123:1509000000000
123:1509000000000
```
In above example, at timestamp **1509000000000,** we will consider only one visit by the user 123 and ignore duplicate visits.

2. Timestamps in the log file are in milliseconds.

## Examples-

Following are cases handled by the code:

**Case 1** Valid Input File
```
123, 1508637152000
123, 1508637153000
123, 1508637154000 
123, 1508724551000
124, 1508723551000 
125, 1508723553000
124, 1508723554000
```

**Case 2** Some entries corrupted (Empty lines, non-numeric characters, non-positive timestamps)
```
123, -1508637152000
123, 1508637153000
123, 1508637154000
123, 1508724551000
124, sd
Afhh

124, -1508723552000
124, 1508723554000
```

**Case 3** Input file with a different delimiter

```
123:1508637152000
123:1508637153000
123:1508637154000
125:1508723552000
123:1508724551000
124:1508723551000

```
**Case 4** Empty input file

```
Output:
The input log file is either empty or all entries are in incorrect format.

```

**Case 5  ** File doesn&#39;t exist

```
Output:
Given input file doesn't exist. Please check the path.

```

# Package Level Class Diagrams -

1. [Class Diagram for module **comparator.**] (images/Capture3.PNG)

2. [Class Diagram for package **module**](images/Capture.PNG)

3. [Class Diagram for the entire project](images/Capture2.PNG)
 
## Code Analysis:

1. We are using BufferedReader along with Java Stream APIs to read the input log file. Advantage of this approach is that complete file is not loaded into memory and also depending on the number of cores available, use of Streams performs parallel processing of the task.
2. We are initially creating a map of users and their unique timestamps, followed by merging these timestamps into sessions. Using _UserSessionEntry_ POJO which keeps track of a user id, session and the number of visits in this session.
Using a TreeSet for storing the timestamps in sorted order.

```
Time Complexity of  creatinguser-timestamps map - **O(u \* N log N).** 
where N - average number of timestamps per userand u - number of unique users 
```

3. We have defined a custom Comparator for UserSessionEntry class to do comparisons on the basis of number of visits and the last visited timestamp.
4. Using this comparator along with a Max-Priority Queue, all the UserSessionEntry objects obtained in the Step2 are added to the PQ.
```
Time Complexity of  creating the Max-Priority Queue -  **O(m)**
where m - total number of sessions,and, m <= N
```

5. The top element in the Priority queue is the user with maximum visits.

## Possible Improvements &amp; Scalability:

1. Currently, the codebase just handles input files with timestamp in milliseconds. With small changes in the code, we can take input from user indicating the units of timestamps (milliseconds, seconds) present in the log file.
2. In the current implementation, we are not making any assumptions about the order of the log entries in the input file (i.e timestamps may appear in log file in unsorted fashion). Due to this, we have to keep track of all the sessions per user throughout the file processing. This approach is inefficient for streaming log scenarios and may lead to out of memory error.

We can handle this case by following a **heuristic**** approach **. Assumption is, given** a time window**_,_ say, 30 minutes or 1 hour, in which if a session is in continuation, that time stamp will appear. In other words, if timestamp which can continue an already open session doesn&#39;t appear in the given time window, we will close that session and process it.

The advantage of the above approach is that per user we need to keep track of just the currently opened sessions and last known session with maximum visits. This also saves us lot of memory and should be preferred approach is streaming log cases.
3. We can modify the code to take the session timeout limit as an input from the user instead of hardcoding the time as 1 hour.
4. We can also follow a **batch-processing approach.**


1. For huge log files, read a chunk of input, which can fit into the memory. Create a User-Session map for this chunk by merging timestamps into sessions as explained above.
2. Repeat this process until whole input file is processed.
3. Now, we have a User-Session map which may contain some overlapping session. We will merge these overlapping sessions per user.
4. Using the merged User-Session map, find the user with maximum visits.

This approach is similar to **map-reduce paradigm** , where

Steps A-B are **mapper tasks**  and

Steps C-D are **reducer tasks.**

We can use this technique to scale our program for crunching huge log files on clusters with multiple machines.

**Best approach will be a combination of heuristics and map-reduce paradigm for streaming log files.**
