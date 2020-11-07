:-dynamic
	% A reversed path according to Time, Dist or Cost
	path_rev/3.

% Bus (Number, Origin, Destination Place, Departure Time, Arrival Time, Distance, Cost)
bus(100,'Amingaon','Jalukbari',14,15,10,10).
bus(101,'Jalukbari','Paltanbazaar',15,17,4,3).
bus(102,'Jalukbari','Paltanbazaar',14.5,17,4,2).
bus(102,'Paltanbazaar','Fancybazaar',18,23,6,9).
bus(120,'Jalukbari','Panbazar',15,17,10,8).
bus(121,'Jalukbari','Panbazar',15,18.5,9,9).
bus(122,'Jalukbari','Panbazar',15,19,8,10).
bus(255,'Jalukbari','Maligaon',15,16,11,2).
bus(256,'Maligaon','Panbazar',16,16.5,5,8).
bus(301,'Maligaon','Lokhra',16,17,7,1).
bus(302,'Lokhra','Panbazar',17,18,8,3).
bus(900,'Panbazar','Chandmari',16.5,19.5,7,8).
bus(901,'Panbazar','Chandmari',19,20.5,7,8).

% Source and Dest would be instantiated
edge(Source, Dest, Time, 'time') :-
	bus(_, Source, Dest, T1, T2, _, _), 
	Time is T2-T1.

edge(Source, Dest, Dist, 'dist') :-
	bus(_, Source, Dest, _, _, Dist, _).

edge(Source, Dest, Cost, 'cost') :-
	bus(_, Source, Dest, _, _, _, Cost).

% edge(Source, Dest, Cost, Type).
path(From, To, Dist, Type) :- edge(To, From, Dist, Type).
path(From, To, Dist, Type) :- edge(From, To, Dist, Type).

% path < stored path? replace it
shorterPath([H|Path], Dist, Type) :-		       
	% match target node [H|_]
	path_rev([H|T], D, Type), !, Dist < D, T is T,
	retract(path_rev([H|_], _, Type)), 
	%% writef('(%w < %w) %w is closer than %w\n', [Dist, D, [H|Path], [H|T]]), 
	assert(path_rev([H|Path], Dist, Type)).

% Otherwise store a new path
shorterPath(Path, Dist, Type) :-		       
	%% writef('New path:%w\n', [Path]), 
	assert(path_rev(Path, Dist, Type)).
 
% traverse all reachable nodes
traverse(From, Path, Dist, Type) :-		    
	% For each neighbor
	path(From, T, D, Type), 		    
	% which is unvisited
	not(memberchk(T, Path)), 	    
	Dist1 is Dist+D,
	% Update shortest path and distance
	shorterPath([T, From|Path], Dist1, Type), 
	% Then traverse the neighbor
	traverse(T, [From|Path], Dist1, Type).	    
 
traverse(From, Type) :-
	% Remove solutions
	retractall(path_rev(_, _, Type)),           
	% Traverse from origin
	traverse(From, [], 0, Type).              

traverse(_, _).
 
route(From, To) :-
	routeType(From, To, 'time'), 
	routeType(From, To, 'dist'), 
	routeType(From, To, 'cost').

routeType(From, To, Type) :-
	% Find all distances
	traverse(From, Type),                   
	% If the target was reached
	path_rev([To|Rest], Dist, Type)
	-> (
	  % Report the path and distance
	  % Distance is round(Dist), 
	  reverse([To|Rest], Path),      
	  writef('Optimum path is %w with %w = %w\n', [Path, Type, Dist])
	  )
	; writef('There is no route from %w to %w\n', [From, To])
	.
