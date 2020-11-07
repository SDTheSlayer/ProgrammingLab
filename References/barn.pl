% Dynamic variable that stores the reversed optimal route generated
:-dynamic rroute/2.      % The reversed route
 
% Knowledge base of rroute routes
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
bus(901,'Panbazar','Chandmari',19,20.5,7,8).
 

% Optimum routes for distance
updateRoute([Destination|Route], [CurTime, CurDist, CurCost], OptimizeType) :-     %Replace old path if new path more optimized for distance
    OptimizeType='Distance',
    rroute([Destination|_], [_, Dist, _]), !, CurDist < Dist,                     %check distance condition 
    retract(rroute([Destination|_],_)),
    assert(rroute([Destination|Route], [CurTime, CurDist, CurCost])).             %sanity check to ensure old path existed

% Optimum routes for time
updateRoute([Destination|Route], [CurTime, CurDist, CurCost], OptimizeType) :-     %Replace old path if new path more optimized for time
    OptimizeType='Time',
    rroute([Destination|_], [Time, _, _]), !, CurTime < Time,                     %check time condition 
    retract(rroute([Destination|_],_)),
    assert(rroute([Destination|Route], [CurTime, CurDist, CurCost])).             %sanity check to ensure old path existed

% Optimum routes for cost
updateRoute([Destination|Route], [CurTime, CurDist, CurCost], OptimizeType) :-     %Replace old path if new path more optimized for cost
    OptimizeType='Cost',
    rroute([Destination|_], [_, _, Cost]), !, CurCost < Cost,                     %check cost condition 
    retract(rroute([Destination|_],_)),
    assert(rroute([Destination|Route], [CurTime, CurDist, CurCost])).             %sanity check to ensure old path existed

updateRoute(Route, [CurTime, CurDist, CurCost], _) :-                              %If a new route, store as is
    assert(rroute(Route,[CurTime, CurDist, CurCost])).                            %Sanity check to ensure path is stored
 

findShortestRoute(Origin, Route, [CurTime, CurDist, CurCost], CurArrival, OptimizeType) :-          % findShortestRoute to destination for bus same day
    bus(Number, Origin, Destination, Departure, Arrival, Dist, Cost),                                % Traverse neighbour for shortest path
    not(memberchk([Destination, _], Route)),                                            % ensure neighbour not visited
    Departure >= CurArrival,
    updateRoute([Destination,[Origin, Number]|Route], [CurTime+Arrival-CurArrival, CurDist+Dist, CurCost+Cost], OptimizeType),       %   Update shortest route
    findShortestRoute(Destination,[[Origin, Number]|Route],[CurTime+Arrival-CurArrival, CurDist+Dist, CurCost+Cost], Arrival, OptimizeType).   %   Call function for selected neighbour

findShortestRoute(Origin, Route, [CurTime, CurDist, CurCost], CurArrival, OptimizeType) :-          % findShortestRoute to destination for bus departing next day
    bus(Number, Origin, Destination, Departure, Arrival, Dist, Cost),                                % Traverse neighbour for shortest path
    not(memberchk([Destination, _], Route)),                                            % ensure neighbour not visited
    Departure < CurArrival,
    updateRoute([Destination,[Origin, Number]|Route], [CurTime+Arrival-CurArrival+24, CurDist+Dist, CurCost+Cost], OptimizeType),       %   Update shortest route
    findShortestRoute(Destination,[[Origin, Number]|Route],[CurTime+Arrival-CurArrival+24, CurDist+Dist, CurCost+Cost], Arrival, OptimizeType).   %   Call function for selected neighbour
 
findShortestRoute(Origin, OptimizeType) :-    %Helper function that resets all paths for 
    retractall(rroute(_,_)),                  % Remove old routes
    findShortestRoute(Origin,[],[0,0,0], 0, OptimizeType).        %Call main route finder

findShortestRoute(_,_).


% Print Routes of the Optimum Routes
printRoute([]).                               %Base Condition - List is empty

printRoute([[Origin, Number] | List]) :-      %For intermediate nodes in path
    writef('%w,%w->', [Origin, Number]),
    printRoute(List).

printRoute([Origin | List]) :-                %For final node, insert non-existent bus number 000
    writef('%w,000', [Origin]),
    printRoute(List).


route(Origin, Destination) :-
  % Find the best route optimized on distance
    findShortestRoute(Origin, 'Distance'),                   
    rroute([Destination|RRouteD], [TimeD, DistD, CostD]),         % If destination achieved, extract and print results
    reverse([Destination|RRouteD], [[OriginD, NumberD] | RouteD]),      % Reverse the path as calculated in opposite order
    bus(NumberD, _, _, DepartureD, _, _, _),
    TotCostD is CostD,
    TotDistD is DistD,
    TotTimeD is TimeD-DepartureD,
    writef('Optimum Distance:\n'),
    printRoute([[OriginD, NumberD] | RouteD]),                                           % Print optimum distance path
    writef('\nDistance=%w,Time=%w,Cost=%w\n', [TotDistD, TotTimeD, TotCostD]),
  
  % Find the best route optimized on time
    findShortestRoute(Origin, 'Time'),                   
    rroute([Destination|RRouteT], [TimeT, DistT, CostT]),         % If destination achieved, extract and print results
    reverse([Destination|RRouteT], [[OriginT, NumberT] | RouteT]),      %  Reverse the path as calculated in opposite order
    bus(NumberT, _, _, DepartureT, _, _, _),
    TotCostT is CostT,
    TotDistT is DistT,
    TotTimeT is TimeT-DepartureT,
    writef('Optimum Time:\n'),
    printRoute([[OriginT, NumberT] | RouteT]),                                           % Print optimum time path
    writef('\nDistance=%w,Time=%w,Cost=%w\n', [TotDistT, TotTimeT, TotCostT]),
  
  % Find the best route optimized on cost
    findShortestRoute(Origin, 'Cost'),                  
    rroute([Destination|RRouteC], [TimeC, DistC, CostC]),         % If destination achieved, extract and print results
    reverse([Destination|RRouteC], [[OriginC, NumberC] | RouteC]),      %  Reverse the path as calculated in opposite order
    bus(NumberC, _, _, DepartureC, _, _, _),
    TotCostC is CostC,
    TotDistC is DistC,
    TotTimeC is TimeC-DepartureC,
    writef('Optimum Cost:\n'),
    printRoute([[OriginC, NumberC] | RouteC]),                                           % Print optimum cost path
    writef('\nDistance=%w,Time=%w,Cost=%w\n', [TotDistC, TotTimeC, TotCostC]),!.
  
  % If no route exists between Origin and Destination
route(Origin, Destination) :-
  writef('No route exists between %w to %w\n', [Origin, Destination]).