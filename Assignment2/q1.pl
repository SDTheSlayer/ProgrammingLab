%%% Question 1 PLL 2020 Assignment 2 %%% 
%%% Shivang Dalal 17010186 %%% 

%% Knowledge base as provided by the question. No modifications have been made. %%

% parent Predicates
parent(jolly,jatin).
parent(jatin,avantika).
parent(manisha,shivkami).
parent(bahubali,shivkami).
parent(manisha,avantika).
parent(jolly,kattappa).

% female Predicates
female(avantika).
female(shivkami).

% male Predicates
male(bahubali).
male(kattappa).
male(jolly).


%%  Task 1: uncle Predicate. %%
%% Can be used to both confirm uncle relationship or find pairs of uncle,nephew %%

% Utility fucntion to check if two children have atleast one common parent. 
% Not used to find parents or siblings.
checkSibling(Child1,Child2):-
    parent(Parent,Child1),
    parent(Parent,Child2),
    Child1 \= Child2,!.

% Predicate to check the uncle relation. First confirms/finds a Male uncle,
% then finds a parent of the newphew or Parent child pairs and  
% finally checks for a sibling relation between uncle and parent. 
uncle(Uncle,Nephew):-
    male(Uncle),
    parent(Parent,Nephew),
    checkSibling(Parent,Uncle).



%%  Task 2: halfsister Predicate. %%
%% Can be used to both confirm halfsister relationship or find pairs of halfsisters %%

% Utility function to check if two children have a common parent. 
% Is also used for finding siblings and common parents given one child.
findSibling(Child1,Child2,Parent):-
    parent(Parent,Child1),
    parent(Parent,Child2),
    Child1 \= Child2.

% Predicate to check the halfsister relation. First confirms/finds a female sister,
% Then either finds a sibling or confrims the sibling relation.
% finally checks if there exists a unique parent for each sibling apart from the common Parent . 
halfsister( HalfSister,Sibling):-
    female(HalfSister),
    findSibling(HalfSister,Sibling,CommonParent),
    parent(Parent1,HalfSister),
    parent(Parent2,Sibling),
    CommonParent\=Parent1,
    CommonParent\=Parent2,
    Parent1 \= Parent2.
