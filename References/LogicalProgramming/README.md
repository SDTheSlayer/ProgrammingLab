**1) Finding Relationship and Gender:**

To find relationships first run:

`$ swipl -s Q1.pl`

***UNCLE:***

Use this predicate to check if X is the uncle of Y or not.

`?- uncle(X, Y).`

E.g.: `?- uncle(kattapa, avantika)`


***HALFSISTER:***

Use this predicate to check if X is the halfsister of Y or not.

`?- halfsister(X, Y).`

E.g.: `?- uncle(manisha, avantika)`



**2) Bus Travel Planner:**

The Bus route map used is:
![Bus Map](BusMap.png?raw=true "Bus Map")

To find optimum path between two places (X and Y) use:

`$ swipl -s Q2.pl`

`?- route(X, Y).`

E.g.: `?- route('Amigaon', 'Paltanbazar').`


**3) Bus Travel Planner:**

To run the program use:

`$ swipl -s Q3.pl`

***A) All paths:***

To find all the paths for a prisoner to become free use:

`?- allPaths.`

It will output the index and the path and indexing is 1 based so there are a total of 57280 paths.

***B) Optimal path:***

To find the optimal path:

`?- optimal.`

***C) Validity:***

To check the validity of given path X (given as a list of gates) use: 

`valid(X).`

E.g.: `?- valid (['G1', 'G6', 'G8', 'G9', 'G8', 'G7', 'G10', 'G15', 'G13', 'G14', 'G18', 'G17'])`