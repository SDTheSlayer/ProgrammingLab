% Distances between pairs of directly connected gates
% All the distances are in ft.
distance('G1','G5',4).
distance('G1','G5',4).
distance('G2','G5',6).
distance('G3','G5',8).
distance('G4','G5',9).
distance('G1','G6',10).
distance('G2','G6',9).
distance('G3','G6',3).
distance('G4','G6',5).
distance('G5','G7',3).
distance('G5','G10',4).
distance('G5','G11',6).
distance('G5','G12',7).
distance('G5','G6',7).
distance('G5','G8',9).
distance('G6','G8',2).
distance('G6','G12',3).
distance('G6','G11',5).
distance('G6','G10',9).
distance('G6','G7',10).
distance('G7','G10',2).
distance('G7','G11',5).
distance('G7','G12',7).
distance('G7','G8',10).
distance('G8','G9',3).
distance('G8','G12',3).
distance('G8','G11',4).
distance('G8','G10',8).
distance('G10','G15',5).
distance('G10','G11',2).
distance('G10','G12',5).
distance('G11','G15',4).
distance('G11','G13',5).
distance('G11','G12',4).
distance('G12','G13',7).
distance('G12','G14',8).
distance('G15','G13',3).
distance('G13','G14',4).
distance('G14','G17',5).
distance('G14','G18',4).
distance('G17','G18',8).

% Core unit gates are the ones prisoners will start from
coreUnit('G1').
coreUnit('G2').
coreUnit('G3').
coreUnit('G4').

% Only G17 leads to freedom for the prisoners
free('G17').

% Edges of the graph
% Since prisoner can move from A to B and vice versa
% edge between X and Y with distance D
edge(X, Y, D) :- distance(X, Y, D); distance(Y, X, D).

% To output all the possible paths from core units to G17
path(A, B,Path, Len) :-
    travel(A, B, [A], Q, Len),
    reverse(Q, Path).

travel(A, B, Visited, [B|Visited], L) :-
    edge(A, B, L).

travel(A, B, Visited, Path, L) :-
    edge(A, C, D),
    C \== B,
    not(memberchk(C, Visited)),
    travel(C, B, [C|Visited], Path, L1),
    L is L1+D.

shortest(A,B,Path,Length) :-
    % find all paths from A to B using path(A,B,P,L) and assign [P,L] tuple to Set
    setof([P,L],path(A,B,P,L),Set),
    % Set should not be empty
    Set \== [[]|[]],
    % find the tuple with minimum Length from Set
    minSet(Set,[Path,Length]).

% M will store the optimal tuple of Path and Length
minSet([First|Rest],Min) :- min(Rest,First,Min).

% If rest of set is empty i.e. we have checked the whole set
min([],M,M).
% If first element of remaining set has less length then change the minimum tuple 
min([[P,L]|R],[_,M],Min) :- L < M, min(R,[P,L],Min). 
% If first element of remaining set has greater or equal length then don;t change the minimum tuple
min([[_,L]|R],[Q,M],Min) :- L>=M, min(R, [Q, M], Min).

%---------------------------------(A)----------------------------------------
% TODO: not able to print the total possible number of paths
allPaths :-
    setof([P], allPathsUtil(P), Set),
    Set \== [[]|[]],
    printSet(Set, 1)->(
        format('Total number of possible paths are: ~w~n', [L])).
    

allPathsUtil(P) :-
    coreUnit(Source),
    free(Destination),
    path(Source, Destination, P, _).

printSet([[]|[]], _).

printSet([First|Rest], Count) :-
    format('~w: ~w~n', [Count, First]),
    NewCount is Count+1,
    printSet(Rest, NewCount).

%---------------------------------(B)----------------------------------------
% main predicate which will print the optimum path from core unit gates to freedom
optimal :-
    setof([P,L], optimalUtil(P,L),Set),
    Set \== [[]|[]],
    minSet(Set, [Path, Length]),
    printAtoB(Path),
    format('The optimal distance is:~w', [Length]).

% for each pair of coreunit or destination find the optimum path and it will be stored by set in optimal predicate
optimalUtil(P,L) :-
    coreUnit(Source),
    free(Destination),
    shortest(Source, Destination, P, L).

% print optimum path
printAtoB([A, B|L]) :-
    format('~w->~w->', [A, B]),
    printAtoB(L).

printAtoB([A|[]]) :-
    format('~w~n', [A]).

%---------------------------------(C)----------------------------------------
% to validate if given path is valid or not in terms of escaping
% Head is the first element of list and Tail is the rest of list
valid([Head|Tail]) :-
    % Head should be one of core units gate i.e. the gate from where prisoner starts.
    coreUnit(Head),
    % check the rest of path if it leads to freedom
    checkPath([Head|Tail]).

% Check if there is an edge from A to B or not where A and B are consecutive gates in path given.
checkPath([A, B|Path]) :-
    edge(A, B, _),
    % check for the rest of path 
    checkPath([B|Path]).

% when we reach last element of list just check if it is 'G17' i.e. free gate or not.
checkPath([A|[]]) :-
    free(A).