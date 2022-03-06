package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.
        ImList<Clause> myAnswer = formula.getClauses();
        Environment env = new Environment();
        return solve(myAnswer, env);}
    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.

        // If there are no clauses, the formula is trivially satisfiable;
        if (clauses.isEmpty()) {
            return env;
        }
        Clause smallest = clauses.first();//returns first
        for (Clause yay : clauses) {
            // If there is an empty clause, the clause list is unsatisfiable -- fail and backtrack.
            // Use an empty clause evaluated to FALSE based on the variable binding in the environment.
            if (yay.isEmpty()) {
                return null;
            }
            // Otherwise, find the smallest clause (by number of literals)
            // If the clause has only one literal, bind its variable in the environment so that the
            // clause is satisfied.
            if (yay.size() < smallest.size()) { //return literals.size();
                smallest = yay;
            }
            if (smallest.isUnit()) { //is 1

                smallest = yay;
                break; //hm do i break
            }
        }
        // Pick an arbitrary literal from the smallest clause
        // Environment newEnv = new Environment();
        Literal literal = smallest.chooseLiteral();
        Variable var = literal.getVariable(); //variable associated with this literal

        // If the clause has only one literal,// bind its variable in the environment so that the clause is Satisfied,
        //            // substitute for the variable in all the other clauses and recursively call solve()
        if (smallest.isUnit()) {
            Environment newEnv = new Environment();
            if (literal instanceof PosLiteral) {
                env = env.putTrue(var);

                newEnv = solve(substitute(clauses, literal), env);
            }// true if 1 exists
            else{
                env = env.putFalse(var);
                newEnv = solve(substitute(clauses, literal), env); //else false
            }

            return newEnv;
        }
        else {
            Environment newEnv = new Environment();
            env = env.putTrue(var);
            // Set literal to TRUE, substitute for it in all the clauses, then solve recursively
            newEnv = solve(substitute(clauses, literal), env);
            Literal negate = literal.getNegation();
            if (newEnv == null) return solve(substitute(clauses, negate), env);
            else return newEnv;
        }
    }
    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        // TODO: implement this.
            ImList<Clause> myFinal = new EmptyImList<>();
            Clause clause = new Clause();
            if (clauses.isEmpty()){
                return myFinal;
            }
            for (Clause c : clauses) {
                if (c != null){
                    clause = c.reduce(l); //returns the clause if literal is true or null if everything is true
                }
                if (clause != null){
                    myFinal = myFinal.add(clause);
                }
            }
            return myFinal;
        }
    }
