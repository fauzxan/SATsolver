# SATsolver
##This code helps determine if the given cnf file contains a satisfiable or unsatisfiable cnf
We have utilised the DPLL algorithm with backtracking and unit propagation to create our SAT Solver. First, we check if there are any clauses in the formula (ie. check if trivially satisfiable). After this, we check if there are any empty clauses that would make the clause list unsatisfiable. Otherwise, we find the smallest clause(ie. The clause with the least number of literals).  If the clause has only one literal, we bind its variable in the environment so that the clause is satisfied. We also check if it is a positive literal before setting the True/False values. If there is more than 1 literal, we pick an arbitrary literal from the smallest clause and bind its variable in the environment so that the clause is satisfied. We then substitute for the variable in all the other clauses and recursively call solve(). 

We reduced the run-time complexity by using the above logic of unit propagation and not utilising the brute force method. We also invoked the reduce method which sets one literal to true, if it is true, we remove the whole clause, if false we move on to the next literal. This is able to reduce the number of recursions significantly hence reducing our computational time

## Runtime report
### File: largesat.cnf
### Result: Satisfiable
### Running time: 
### Parsing process: 1.144876300 s 
### Solving process: 1.26215120 s
### Specification: Windows, a processor with Intel Core i7(10750MH), 16Gb ram @ 2933Mhz (not overclocked), NVIDIA GTX 1650 
