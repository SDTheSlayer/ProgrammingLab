%%% Question 2 PLL 2020 Assignment 2 %%% 
%%% Shivang Dalal 17010186 %%% 


% Dynamic predicate, which stores reverse optimal bus routes based on the optimization type.
:-dynamic savedRoute/5. 
 
%% Knowledge base of bus routes %%
bus(100,'Amingaon','Jalukbari',14,15,10,10).
bus(101,'Jalukbari','Paltanbazaar',15,17,4,3).
bus(102,'Jalukbari','Paltanbazaar',14.5,17,4,2).
bus(102,'Paltanbazaar','Fancybazaar',18,23,6,9).
bus(120,'Jalukbari','Panbazar',15,17,10,8).
bus(121,'Jalukbari','Panbazar',15,18.5,9,9).
bus(122,'Jalukbari','Panbazar',15,19,8,10).
bus(255,'Jalukbari','Maligaon',15,16,2,2).
bus(256,'Maligaon','Panbazar',16,16.5,1,8).
bus(301,'Maligaon','Lokhra',16,17,7,1).
bus(302,'Lokhra','Panbazar',17,18,8,3).
bus(900,'Panbazar','Chandmari',16.5,19.5,7,8).
bus(901,'Panbazar','Chandmari',19.5,21,7,8).

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
    writef('\nDistance=%w, Time=%w, Cost=%w\n', [Dist,TotTime,Cost]),!.
  
% Fails if no route to destination exists.
findShortestRoute(Start, End,_) :-
  writef('Cannot reach %w from %w !!\n', [End,Start ]),false.



%  Main Predicate which is used for computing the routes based on all three optimizing parameters.
%  However Both variables Start and End must be given for proper functioning.
route(Start, End) :-
	findShortestRoute(Start, End, 'Distance'), 
	findShortestRoute(Start, End, 'Time'), 
	findShortestRoute(Start, End, 'Cost').