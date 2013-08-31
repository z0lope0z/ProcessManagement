#IronChef
-----------------------

Process management Machine Problem for cs140

## General Requirements

* [Java 1.6](http://docs.oracle.com/javase/7/docs/webnotes/install/)
* [IntelliJ](http://www.jetbrains.com/idea/download/)
or
* [Eclipse](http://www.eclipse.org/downloads/)

## Configuration
You can modify the recipes list in the recipes/ folder

### tinola.txt Example
```
cook 2
mix 2
chop 2
```

You can modify the tasklists and the time that they come in inside the tasklist.txt file
The first line contains the type of Process Scheduler

```
FCFS - First Come First Serve
SJF - Shortest Job First
PRIORITY - Priority
RR <time_quantum> <context_switch> - Round Robin
```

###tasklist.txt Example
```
RR 2 2
adobo1 0
adobo3 3
```
