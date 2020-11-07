:-dynamic(reversedPath/2).
:-dynamic(path/1).


weightedEdge('G1','G5',4).
weightedEdge('G2','G5',6).
weightedEdge('G3','G5',8).
weightedEdge('G4','G5',9).
weightedEdge('G1','G6',10).
weightedEdge('G2','G6',9).
weightedEdge('G3','G6',3).
weightedEdge('G4','G6',5).
weightedEdge('G5','G7',3).
weightedEdge('G5','G10',4).
weightedEdge('G5','G11',6).
weightedEdge('G5','G12',7).
weightedEdge('G5','G6',7).
weightedEdge('G5','G8',9).
weightedEdge('G6','G8',2).
weightedEdge('G6','G12',3).
weightedEdge('G6','G11',5).
weightedEdge('G6','G10',9).
weightedEdge('G6','G7',10).
weightedEdge('G7','G10',2).
weightedEdge('G7','G11',5).
weightedEdge('G7','G12',7).
weightedEdge('G7','G8',10).
weightedEdge('G8','G9',3).
weightedEdge('G8','G12',3).
weightedEdge('G8','G11',4).
weightedEdge('G8','G10',8).
weightedEdge('G10','G15',5).
weightedEdge('G10','G11',2).
weightedEdge('G10','G12',5).
weightedEdge('G11','G15',4).
weightedEdge('G11','G13',5).
weightedEdge('G11','G12',4).
weightedEdge('G12','G13',7).
weightedEdge('G12','G14',8).
weightedEdge('G15','G13',3).
weightedEdge('G13','G14',4).
weightedEdge('G14','G17',5).
weightedEdge('G14','G18',4).
weightedEdge('G17','G18',8).


begin('G1').
begin('G2').
begin('G3').
begin('G4').
end('G17').






edge(X, Y):- weightedEdge(X, Y, _).
edge(X, Y):- weightedEdge(Y, X, _).

weightedEdgeBidirectional(X, Y, C):- weightedEdge(X, Y, C).
weightedEdgeBidirectional(X, Y, C):- weightedEdge(Y, X, C).


path([Head|[]]):- end(Head).
path([Head, Second|Tail]):- edge(Head, Second), path([Second|Tail]).
valid([Head|Tail]):- begin(Head), path([Head|Tail]), !, writef("Given Path is Valid"). 
valid(_):- writef("Given Path is Invalid").


shortestPath([Head|Tail], Dist) :-reversedPath([Head|_], D), !, Dist =< D, retractall(reversedPath([Head|_],_)),assert(reversedPath([Head|Tail], Dist)).

shortestPath([Head|Tail], Dist) :-assert(reversedPath([Head|Tail], Dist)).


nodeTraversal(Node, Path, Distance) :-		    
        weightedEdgeBidirectional(Node, Neighbour, DistanceEdge), 		    
        not(memberchk(Neighbour, Path)),
        NewDistance is Distance+DistanceEdge, 
        shortestPath([Neighbour|Path], NewDistance), 
        nodeTraversal(Neighbour, [Neighbour|Path], NewDistance).	    

remove:- retractall(reversedPath([_|_],_)).

printList([Head|[]]):- writef('%w',[Head]).
printList([Head|Tail]):- writef('%w->',[Head]),printList(Tail).

optimal:-
        retractall(reversedPath([_|_],_)),
        \+ nodeTraversal('G1', ['G1'], 0),
        \+ nodeTraversal('G2', ['G2'], 0),
        \+ nodeTraversal('G3', ['G3'], 0),
        \+ nodeTraversal('G4', ['G4'], 0),
        reversedPath(['G17'|Rest], Cost),
        reverse(['G17'|Rest], Path),
        writef('Optimum Dist is %w\n',[Cost]),
        printList(Path),nl.


allPaths('G17', Path):- assert(path(Path)),false.

allPaths(Node, Path):- 
        Node \= 'G17',	    
        weightedEdgeBidirectional(Node, Neighbour, _), 		    
        not(memberchk(Neighbour, Path)),  
        allPaths(Neighbour, [Neighbour|Path]).	    


findpaths:-
        retractall(reversedPath([_|_],_)),
        retractall(path([_|_])),
        \+allPaths('G1', ['G1']),
        \+allPaths('G2', ['G2']),
        \+allPaths('G3', ['G3']),
        \+allPaths('G4', ['G4']),
        aggregate_all(count, path(_), Count),writeln(Count),retractall(path([_|_])).

