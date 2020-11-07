/*
    Database
*/
parent(jatin,avantika).
parent(jolly,jatin).
parent(jolly,kattappa).
parent(manisha,avantika).
parent(manisha,shivkami).
parent(bahubali,shivkami).

male(kattappa).
male(jolly).
male(bahubali).

female(shivkami).
female(avantika).

% Q1
% For X to be uncle of Y -> X has to be male and should be brother of one of Y's 
% parents i.e. he should have a common parent with one of Y's parent

uncle(X, Y) :-  
    male(X),
    parent(Z, Y),   % Z is a parent of Y 
    parent(A, Z),   % Z and X have A as a common parent
    parent(A, X).


% Q2
% For X to be Y's halfsister -> X has to be female and X and Y should have only 
% one common parent
halfsister(X, Y) :- 
    female(Y),
    parent(Z, X),   % Z is common parent of X and Y
    parent(Z, Y),   
    parent(A, X),   % X has A as other parent and Y has B as other parent
    parent(B, Y),
    A \== B,        % since we want only one common parent so Z, A and B are all different
    Z \== A,
    Z \== B.
