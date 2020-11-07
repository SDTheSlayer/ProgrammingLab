% paths is of form (City, [optimal list of vertices from City to source], cost/distance/duration)
:- dynamic(pathsDistance/3).
:- dynamic(pathsDuration/3).
:- dynamic(pathsCost/3).

% Bus (Number, Origin, Destination Place, Departure Time, Arrival Time, Distance, Cost)
bus(1, 'Amingaon', 'Jalukbari', 8, 8.5, 4, 3).
bus(2, 'Amingaon', 'Chandmari', 9, 9.5, 3, 5).
bus(3, 'Jalukbari', 'Panbazar', 9.5, 10, 3, 4).
bus(4, 'Panbazar', 'Chandmari', 10.5, 11, 2, 10).
bus(6, 'Panbazar', 'Paltanbazar', 11, 11.25, 1, 2).
bus(7, 'Chandmari', 'Maligaon', 13, 14, 6, 7).
bus(8, 'Maligaon', 'Lokhra', 14, 14.5, 5, 8).
bus(9, 'Amingaon', 'Paltanbazar', 16, 17.5, 7, 10).
bus(10, 'Chandmari', 'Paltanbazar', 17, 17.5, 3, 6).

% Instantiate all the edggs on the basis of cost, distance and time 
% Cost edges having only bus number, to, from place and cost
cost_edges(Bus_number, Source, Destination, Cost) :-
    bus(Bus_number, Source, Destination, _, _, _, Cost).

% distance edges having only bus number, to, from place and distance
distance_edges(Bus_number, Source, Destination, Distance) :-
    bus(Bus_number, Source, Destination, _, _, Distance, _).
    
% duration edges having only bus number, to, from place and duration
duration_edges(Bus_number, Source, Destination, Duration) :-
    bus(Bus_number, Source, Destination, Departure, Arrival, _, _),
    Duration is Arrival - Departure.
    
% Main predicate used to find to all the optimum solutions
route(Source, Destination) :- 
    findShortestPathByDistance(Source, Destination),
    findShortestPathByCost(Source, Destination),
    findShortestPathByDuration(Source, Destination).

%-----------------------------DISTANCE------------------------------------------------
% start walking from Source and keep track of optimal path to every other city
% as soon as we reach Destination break and print path.
findShortestPathByDistance(Source, Destination) :-
    retractall(pathsDistance(_, _, _)),
    moveForDistance(Source),
    pathsDistance(Destination, _, _)
    ->(writeln("Optimum Distance:"),printPathDistance(Destination)); writeln("No path found.").
    
% Instantiate moveForDistance() predicate with appropriate parameters described below
moveForDistance(Source) :-
    moveForDistance(Source, [Source], [Source], 0).

% Base case for this predicate
moveForDistance(_).

% Visited contains the vertices already seen from source
% For all the neighbour edges first check if other side vertex is already visited or not
% if not then see if visiting it leads to shortest path
moveForDistance(From, Visited, Path, Distance) :-
    distance_edges(_, From, To, D),
    not(memberchk(To, Visited)),
    NewDistance is D+Distance,
    shortestPathToThisNeighbourByDistance(To, [To|Path], NewDistance),
    moveForDistance(To, [To|Visited], [To|Path], NewDistance).

% checks if NewPath is shortest path to this City from source
% if yes then place it in pathsDistance clause
shortestPathToThisNeighbourByDistance(City, NewPath, Distance) :-
    pathsDistance(City, _, D),
    % If Distance>D then cut (!) fails and we don't backtrack to pathsDistance().
    !, Distance<D, 
    retract(pathsDistance(City, _, _)),
    assert(pathsDistance(City, NewPath, Distance)).

% In above condition if no path existed to this City from source then simply add NewPath
shortestPathToThisNeighbourByDistance(City, NewPath, Distance) :-
    assert(pathsDistance(City, NewPath, Distance)).

% predicate to print the optimum path on the basis of distance
printPathDistance(Destination) :-
    pathsDistance(Destination, ReversePath, _),
    reverse(ReversePath, Path),
    printAtoB(Path, 0, 0, 0).

%-----------------------------------------------------------------------------

%-------------------------------------COST--------------------------------------
% start walking from Source and keep track of optimal path to every other city
% as soon as we reach Destination break and print path.
findShortestPathByCost(Source, Destination) :-
    retractall(pathsCost(_, _, _)),
    moveForCost(Source),
    pathsCost(Destination, _, _)
    ->(writeln("Optimum Cost:"),printPathCost(Destination)).
    
% Instantiate moveForCost() predicate with appropriate parameters described below
moveForCost(Source) :-
    moveForCost(Source, [Source], [Source], 0).

moveForCost(_).

% Visited contains the vertices already seen from source
% For all the neighbour edges first check if other side vertex is already visited or not
% if not then see if visiting it leads to path with lowest cost
moveForCost(From, Visited, Path, Cost) :-
    cost_edges(_, From, To, C),
    not(memberchk(To, Visited)),
    NewCost is C+Cost,
    shortestPathToThisNeighbourByCost(To, [To|Path], NewCost),
    moveForCost(To, [To|Visited], [To|Path], NewCost).

% checks if NewPath is optimum (cost) to this City from source
% if yes then place it in pathsCost clause
shortestPathToThisNeighbourByCost(City, NewPath, Cost) :-
    pathsCost(City, _, C),
    !, Cost<C, 
    retract(pathsCost(City, _, _)),
    assert(pathsCost(City, NewPath, Cost)).

% In above condition if no path existed to this City from source then simply add NewPath
shortestPathToThisNeighbourByCost(City, NewPath, Cost) :-
    assert(pathsCost(City, NewPath, Cost)).

% predicate to print the optimum path on the basis of cost
printPathCost(Destination) :-
    pathsCost(Destination, ReversePath, _),
    reverse(ReversePath, Path),
    printAtoB(Path, 0, 0, 0).


%-----------------------------------------------------------------------------

%-------------------------------------TIME--------------------------------------
% start walking from Source and keep track of optimal path to every other city
% as soon as we reach Destination break and print path.
findShortestPathByDuration(Source, Destination) :-
    retractall(pathsDuration(_, _, _)),
    moveForDuration(Source),
    pathsDuration(Destination, _, _)
    ->(writeln("Optimum Time:"),printPathDuration(Destination)).
    
% Instantiate moceForDuration() predicate with appropriate parameters described below
moveForDuration(Source) :-
    moveForDuration(Source, [Source], [Source], 0).

moveForDuration(_).

% Visited contains the vertices already seen from source
% For all the neighbour edges first check if other side vertex is already visited or not
% if not then see if visiting it leads to shortest path
moveForDuration(From, Visited, Path, Duration) :-
    duration_edges(_, From, To, D),
    not(memberchk(To, Visited)),
    NewDuration is D+Duration,
    shortestPathToThisNeighbourByDuration(To, [To|Path], NewDuration),
    moveForDuration(To, [To|Visited], [To|Path], NewDuration).

% checks if NewPath is optimum (duration) to this City from source
% if yes then place it in pathsDuration clause
shortestPathToThisNeighbourByDuration(City, NewPath, Duration) :-
    pathsDuration(City, _, D),
    !, Duration<D, 
    retract(pathsDuration(City, _, _)),
    assert(pathsDuration(City, NewPath, Duration)).

% In above condition if no path existed to this City from source then simply add NewPath
shortestPathToThisNeighbourByDuration(City, NewPath, Duration) :-
    assert(pathsDuration(City, NewPath, Duration)).

% predicate to print the optimum path on the basis of duration
printPathDuration(Destination) :-
    pathsDuration(Destination, ReversePath, _),
    reverse(ReversePath, Path),
    printAtoB(Path, 0, 0, 0).

%-------------------------------------------------------------------------------------
% Util function to print optimum path and cost/time/duration associated with it
printAtoB([A,B|T], Cost, Time, Distance) :-
    distance_edges(N, A, B, D),         % get D as distance between A and B places
    cost_edges(N, A, B, C),             % get C as cost between A and B places   
    duration_edges(N, A, B, Duration),  % get Duration  between A and B places
    NewDistance is D+Distance,          % compute distance, cost  and duration so far
    NewCost is Cost + C,
    NewTime is Time + Duration,
    format('~w,~w->', [A, N]),          % print place A and the bus number we shoud take from here to reach B
    printAtoB([B|T], NewCost, NewTime, NewDistance).    % recursively call same predicate from place B

% base case when we reach our destination
printAtoB([A|[]], Cost, Time, Distance):- format('~w~n Distance: ~w, Cost: ~w, Time: ~w~n', [A,Distance, Cost, Time]).

