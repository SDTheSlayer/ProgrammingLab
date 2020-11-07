%%% Question 3 PLL 2020 Assignment 2 %%% 
%%% Shivang Dalal 17010186 %%% 

% Dynamic predicate, which stores reverse of optimal paths to particular gate.
:-dynamic(savedPath/2).
% Dynamic predicate, which stores all possible paths to Escape.
:-dynamic(printablePath/1).

%% Knowledge base of Gate distances %%
gatePath('G8','G9',3).
gatePath('G1','G5',4).
gatePath('G1','G6',10).
gatePath('G2','G5',6).
gatePath('G14','G18',4).
gatePath('G4','G6',5).
gatePath('G5','G7',3).
gatePath('G11','G13',5).
gatePath('G5','G10',4).
gatePath('G14','G17',5).
gatePath('G2','G6',9).
gatePath('G5','G11',6).
gatePath('G11','G15',4).
gatePath('G13','G14',4).
gatePath('G5','G8',9).
gatePath('G12','G13',7).
gatePath('G10','G12',5).
gatePath('G12','G14',8).
gatePath('G5','G12',7).
gatePath('G6','G8',2).
gatePath('G6','G12',3).
gatePath('G5','G6',7).
gatePath('G6','G11',5).
gatePath('G8','G12',3).
gatePath('G4','G5',9).
gatePath('G6','G10',9).
gatePath('G11','G12',4).
gatePath('G6','G7',10).
gatePath('G7','G10',2).
gatePath('G7','G11',5).
gatePath('G7','G12',7).
gatePath('G3','G5',8).
gatePath('G3','G6',3).
gatePath('G7','G8',10).
gatePath('G10','G11',2).
gatePath('G17','G18',8).
gatePath('G8','G11',4).
gatePath('G8','G10',8).
gatePath('G10','G15',5).
gatePath('G15','G13',3).

% Starting Core unit gates
coreUnit('G1').
coreUnit('G2').
coreUnit('G3').
coreUnit('G4').

% Escape Gate
escape('G17').

%% Utility predicate to make gatePath bidirectional%%
edge(X, Y, C):- gatePath(X, Y, C).
edge(X, Y, C):- gatePath(Y, X, C).


%% Utility predicate to print a escape path%%

outputPath([End|[]]):- 
    writef('%w',[End]).

outputPath([Start|Rest]):- 
    writef('%w => ',[Start]),outputPath(Rest).

outputPath([]).

%% Utility Function for comparision of Cost of two paths %%
% First based on distance
comp(TotalDist,SavedDist,_,_):-
    TotalDist < SavedDist.

%If same distance then shorter number of gates traversed 
comp(TotalDist,SavedDist,TotalPathLength,SavedPathLength):-
    TotalDist = SavedDist,TotalPathLength<SavedPathLength.



%%  Task A: Find all possible paths to escape (No repetition of gates allowed). %%

% If the current gate can be escaped from store the path
findAllPaths([End|Rest]):- 
    escape(End),
    reverse([End|Rest], Path),
    assert(printablePath(Path)),false.

% If the current gate can't be escaped from go to all gates which can be reached from current gate and not already visited.
findAllPaths([End|Rest]):-     
    edge(End, Next, _), 		    
    not(memberchk(Next, Rest)),  
    findAllPaths([Next,End|Rest]).	  


% Starts searching for paths from all starting core units
startAllPaths:-
    coreUnit(Start),
    findAllPaths([Start]).

startAllPaths().


% User predicate to find all possible paths to escape.
% Outputs the total number of paths and stores all paths as printablePath predicates.
findpaths:-
    retractall(savedPath([_|_],_)),
    retractall(printablePath([_|_])),
    startAllPaths,
    aggregate_all(count, printablePath(_), Count),
    writef("Total Number of possible paths = %w\n",[Count]),
    write("To print paths run the predicate printablePath(X). X will output possible Paths.").



%%  Task B: Find the optimal path to escape. %%

%  Compares the current path with the saved path to the End gate using the comp function and updates saved path if necessary.
updateSavedPath([End|Path], TotalDist) :-     
    savedPath([End|SavedPath], SavedDist), !,
    length(SavedPath,SavedPathLength),
    length(Path,TotalPathLength),
	comp(TotalDist,SavedDist,TotalPathLength,SavedPathLength),                   
    retractall(savedPath([End|_],_)),
    assert(savedPath([End|Path], TotalDist)).      

% If no previous path to End exists, directly save the path.
updateSavedPath([End|Path], TotalDist) :-                             
    assert(savedPath([End|Path], TotalDist)). 

% Uses DFS based approach using recursion to find and save optimal path to all reachable gates from Start gate.
findShortestPath(Dist,Path,Start):-

    % Iterates over all possible gates reachable from current gate. 
    edge(Start, Next, CurDis), 	

    % Ensure that the new gate is not already in the path.    
    not(memberchk(Next, Path)),
    TotalDist is Dist+CurDis, 

    % Update the optimal path to this Next gate if necessary.
    updateSavedPath([Next|Path], TotalDist), 

    % Recursive step to find all paths from the next gate.
    findShortestPath(TotalDist, [Next|Path],Next).


% Starts searching for optimal paths from all starting core units
findShortestPath:-
    coreUnit(Start),
    findShortestPath(0,[Start],Start).

findShortestPath.

% After finding optimal paths to all gates, Prints the optimal path to Escape. Also returns via Path variable.
optimal(Path):-
    retractall(savedPath([_|_],_)),
    findShortestPath,
    escape(End),
    savedPath([End|Rest], Cost),
    reverse([End|Rest], Path),
    writef('Optimum Dist is %w. The Path followed is: ',[Cost]),
    outputPath(Path),nl,!.


%%  Task C: Check if an inputted path is valid (can contain repetitions of gates). %%

% Check if the next gate is reachable from the current.
checkValidPath([Start,Next|End]):-
    edge(Start,Next,_),
    checkValidPath([Next|End]).

% Ensure the last gate in the list can be escaped from.
checkValidPath([Start|[]]):- escape(Start).

% Ensure the starting gate is in the core unit, and then validate the rest of path.
valid([Start|End]):- 
    coreUnit(Start),
    checkValidPath([Start|End]),!,
    writef("The Input is a valid Path to escape.\n").

% The path is not valid 
valid(_):-
     writef("Input Path is not a valid path to escape!!"),false.
