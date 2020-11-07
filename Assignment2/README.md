Commands are assumed to be run from within Assignment2 folder provided. (Current Directory should be Assignment2)

SWI-Prolog version version 8.2.2 on Ubuntu has been used for all questions. 

All commands are to be run from within SWI-Prolog terminal ( achived after using swipl command in bash terminal).

To iterate over multiple possiblities when using X,Y style variables press `;`

If a list is partially outputed while iterating over possiblities of variables, press `w` to print the whole list.

# **1) Finding Relationship and Gender:**

To import the file enter the following in prolog shell:

`?- [q1].`

The knowledge base has been taken exactly from the question.


## Task 1: Uncle Relation

Use the follwoing for finding if X is the uncle of Y. Both can be actual predicates like kattappa or avantika. 

One or both can be set to variables to iterate over possible relations.

`?- uncle(X,Y).`

E.g. 
`?- uncle(kattappa,Y).`

`?- uncle(kattappa,avantika).`


## Task 2: Half Sister Relation

Use the follwoing for finding if X is the half-sister of Y. Both can be actual predicates like shivkami or avantika.

One or both can be set to variables to iterate over possible half-sister relations.
If X is the halfsister of Y then Y is also the half sister of X.

`?- halfsister(X, Y).`

E.g. 
`?- halfsister(shivkami,Y).`

`?- halfsister(shivkami,avantika).`

# **2) Bus Travel Planner:**
To import the file enter the following in prolog shell:

`?- [q2].`

The knowledge set created has been of Pune city (my local area). The predicate structure for bus route specified in the question has been used. It roughly encompensates the major bus stops.

The following are the bus stops:  Aundh, ShivajiNagar, Pashan, Baner, Koregaon, Hadapsar,LaxmiRoad, MagarPatta.

To find the optimal route between 2 bus stops X and Y, starting from X stop and going to Y use the following predicate:

`?- route(X, Y).`

E.g.

    ?- route('Aundh','MagarPatta').
    Optimal Distance:
    Aundh,101 => ShivajiNagar,139 => Hadapsar,1101 => Koregaon,709 => MagarPatta
    Distance=21, Time=9, Cost=32

    Optimal Time:
    Aundh,101 => ShivajiNagar,137 => Koregaon,701 => MagarPatta
    Distance=23, Time=7.5, Cost=27

    Optimal Cost:
    Aundh,101 => ShivajiNagar,133 => Koregaon,701 => MagarPatta
    Distance=26, Time=31.5, Cost=20

    true.

The number next to a stop indicates the bus number to take. Hence the last stop doesnt have a number. In case no route is possible between the two stops, the following will be outputted.

E.g.

    ?- route('Pashan','Hadapsar').
    Cannot reach Hadapsar from Pashan !!
    false.

Note: Both X and Y need to be provided to get meaningful results, hence cannot be left as variables. 


# **3) Prisoner Escape Problem:**
To import the file enter the following in prolog shell:

`?- [q3].`

The knowledge base has been taken exactly from the question. The predicate gatePath(X,Y,Distance) has been used to store a distance of Distance between gate X and Y.

## Task 1: Find all possible escape paths

To calculate all possible paths for prisoners to escape use the following:

`?- findpaths.`

This will output the total number of possible paths. The answer with the given knowledge base is 57280 paths. Duplication of gates in a path is not allowed.

All paths calculated will be stored in printablePath(X) predicate. In order to print a path simple enter:

`?- printablePath(X).`

It will start iterating over all possible escape paths. Use `;` to see next path and `w` in case full path is not displayed.

E.g.

    ?- printablePath(X).
    X = ['G1', 'G5', 'G7', 'G10', 'G12', 'G13', 'G14', 'G18', 'G17']


## Task 2: Find optimal escape path

To find the optimal escape path use the following command:

`?- optimal(X).`

This will display the optimal path as well as assign X to the optimal Path. X has to be a variable.

E.g. 

    ?- optimal(X).
    Optimum Dist is 19. The Path followed is: G3 => G6 => G12 => G14 => G17
    X = ['G3', 'G6', 'G12', 'G14', 'G17'].

## Task 3: Validate an escape path

To validate an escape path `Path` , use the following predicate. Duplicate gates are allowed.

`?- valid(Path).`

E.g. 

    ?-valid(['G1', 'G6', 'G8', 'G9', 'G8', 'G7', 'G10','G15', 'G13', 'G14', 'G18', 'G17']).
    The Input is a valid Path to escape.
    true.


