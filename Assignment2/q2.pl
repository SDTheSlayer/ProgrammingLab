%%% Question 2 PLL 2020 Assignment 2 %%% 
%%% Shivang Dalal 17010186 %%% 


% Dynamic predicate, which stores reverse optimal bus routes based on the optimization type.
:-dynamic savedRoute/5. 
 
%% Knowledge base of bus routes %%
bus(101,'Aundh','ShivajiNagar',12,14,8,10).
bus(111,'ShivajiNagar','Pashan',15,17,4,3).
bus(112,'ShivajiNagar','Pashan',14.5,16.5,4,5).
bus(132,'ShivajiNagar','Koregaon',15,18.5,10,11).
bus(133,'ShivajiNagar','Koregaon',15.5,19,11,5).
bus(137,'ShivajiNagar','Koregaon',15,17,8,12).
bus(139,'ShivajiNagar','Hadapsar',15,16,4,5).
bus(152,'Pashan','Baner',18,19,2,10).
bus(154,'Pashan','LaxmiRoad',12,18,22,12).
bus(701,'Koregaon','MagarPatta',17.5,19.5,7,5).
bus(709,'Koregaon','MagarPatta',19.5,21,6,8).
bus(1101,'Hadapsar','Koregaon',16,18,3,9).
bus(1202,'Hadapsar','LaxmiRoad',16,17,7,5).
bus(1305,'LaxmiRoad','Koregaon',17,18,8,12).


%% Utility Functions to to choose the metric for optimizing%%
 
findOptimizeCost(_,Distance,_,OptimizeType, OptimizeCost):-
	OptimizeType='Distance',
	OptimizeCost=Distance.

findOptimizeCost(Time,_,_,OptimizeType, OptimizeCost):-
	OptimizeType='Time',
	OptimizeCost=Time.

findOptimizeCost(_,_,Cost,OptimizeType, OptimizeCost):-
	OptimizeType='Cost',
	OptimizeCost=Cost.


% Utility Function for comparision of Cost of two paths 

% IF the optimiting metric is smaller than choose the current path
comp(CurOptimizeCost, OptimizeCost, _,_,_,_,_,_):-
	CurOptimizeCost < OptimizeCost.

% Else IF some metrics are same and other are smaller then too choose current path
comp(_, _, CurTime,CurDistance,CurCost,Time,Distance,Cost):-
	CurTime=<Time,CurDistance=<Distance,CurCost=<Cost.


%% Utility Function for Finding total travel time%%

% IF the departure of the bus is on the same day.
findTime(Departure,CurArrival,CurTime,Arrival,Time):-
	Departure >= CurArrival,
	Time is CurTime+Arrival-CurArrival.

% IF the departure of the bus is on the next day.
findTime(Departure,CurArrival,CurTime,Arrival,Time):-
	Departure < CurArrival,
	Time is CurTime+Arrival-CurArrival+24. 


%% Utility function to print a bus route%%

outputRoute([[Start, Number] | Route]) :-      
	writef('%w,%w => ', [Start, Number]),
	outputRoute(Route).

outputRoute([Start | Route]) :-              
	writef('%w', [Start]),
	outputRoute(Route).

outputRoute([]). 


%% Predicate to update optimal routes to a destination from the source based on the Optimizing cost.%%

% Compares the current route with the saved route to the Destination using the comp function and updates if necessary.
updateSavedRoute([End|Route], CurTime, CurDistance, CurCost, CurOptimizeCost) :-  
	savedRoute([End|_], Time, Distance, Cost,OptimizeCost), !, 
	comp(CurOptimizeCost, OptimizeCost, CurTime,CurDistance,CurCost,Time,Distance,Cost),                   
    retractall(savedRoute([End|_],_,_,_,_)),
    assert(savedRoute([End|Route], CurTime, CurDistance, CurCost,CurOptimizeCost)).           

% If no previous route to Destination exists, directly save the route.
updateSavedRoute(Route, Time, Distance, Cost, OptimizeCost) :-                             
	assert(savedRoute(Route,Time, Distance, Cost,OptimizeCost)).     



% Uses DFS based approach using recursion to find and save optimal routes to all destinations from Start.
findRoute(Start, Route, CurTime, CurDistance, CurCost ,CurArrival, OptimizeType) :- 

	% Iterates over all possible buses from current stop  
	bus(Number, Start, End, Departure, Arrival, Distance, Cost), 

	% If this is the starting stop subtract the inital departure time Then update the total time
	(CurTime=0->Time is -Departure;Time is CurTime), 
	findTime(Departure,CurArrival,Time,Arrival,NewTime),             

	% Ensure that the new destination is not already in the route and accoridingly update all values and cost
	not(memberchk([End, _], Route)),	                            
	NewDistance is CurDistance+Distance,
	NewCost is CurCost+Cost,
	findOptimizeCost(NewTime,NewDistance,NewCost,OptimizeType, OptimizeCost),

	% Update the optimal route to this destination if necessary.
	updateSavedRoute([End,[Start, Number]|Route], NewTime, NewDistance, NewCost, OptimizeCost),

	% Recursive step to find all routes from the new destination .   
    findRoute(End,[[Start, Number]|Route],NewTime, NewDistance, NewCost, Arrival, OptimizeType).   
 

% Remove all saved paths and start the Route findign algorithm
findRoute(Start, OptimizeType) :-   
    retractall(savedRoute([_|_],_,_,_,_)),                  
	findRoute(Start,[],0,0,0, 0, OptimizeType).  

% Ensures no call fails, to enure further computation
findRoute(_,_).

               

% After finding optimal paths to all destinations, Prints the optimal path to given End stop.
findShortestRoute(Start, End, OptimizeType) :-
    findRoute(Start, OptimizeType),                  
    savedRoute([End|Rest], Time, Dist, Cost,_),       
    reverse([End|Rest], [[Origin, Number]| Route]),    
    bus(Number, Origin, _,_, _, _, _),
    TotTime is Time,
    writef('Optimal %w:\n',[OptimizeType]),
    outputRoute([[Origin, Number] | Route]),                                        
    writef('\nDistance=%w, Time=%w, Cost=%w\n\n', [Dist,TotTime,Cost]),!.
  
% Fails if no route to destination exists.
findShortestRoute(Start, End,_) :-
  writef('Cannot reach %w from %w !!\n', [End,Start ]),false.



%  Main Predicate which is used for computing the routes based on all three optimizing parameters.
%  However Both variables Start and End must be given for proper functioning.
route(Start, End) :-
	findShortestRoute(Start, End, 'Distance'), 
	findShortestRoute(Start, End, 'Time'), 
	findShortestRoute(Start, End, 'Cost').