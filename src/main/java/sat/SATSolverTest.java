package sat;

/*
import static org.junit.Assert.*;

import org.junit.Test;
*/




import immutable.ImList;
import sat.env.*;
import sat.formula.*;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class SATSolverTest{
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();



	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    
	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String Path =args[0];
        System.out.println("\nFile location: "+Path+"\n");

        Formula f=new Formula();
        long parsingstart= System.nanoTime();
        try{
            // trying to read the file line by line.
            File cnf=new File(Path);
            FileReader cnf_r=new FileReader(cnf);
            BufferedReader reader=new BufferedReader(cnf_r);
            Pattern pattern=Pattern.compile("-?\\d+(\\.\\d+)?");// this is to check if the given entry is numeric or not
            String line;

            while ((line= reader.readLine())!=null) {
                char zero = '0';
                String[] line_split = line.split(" ");
                if (line==null){
                   break;
                }
                if (line.length() == 0) {
                    continue;
                }// if its an empty line it should increment flag
                else if (!pattern.matcher(line_split[0]).matches()) {// to check if the line contains any non integer values
                    continue;
                }
                else if (line.charAt(line.length() - 1) == zero) {
                    Clause c=new Clause();
                    int i = 0;

                    for (i = 0; i < line_split.length; i++) {
                        if (Integer.parseInt(line_split[i]) > 0) {
                            Literal positive = PosLiteral.make(line_split[i]);
                            c=c.add(positive);
                        } else if (Integer.parseInt(line_split[i]) < 0) {
                            Literal negative = NegLiteral.make(line_split[i].substring(1));
                            c=c.add(negative);
                        }
                    }
                    f=f.addClause(c);
                }
            }
            cnf_r.close();
        }
        catch(Exception e) {
            System.out.println("Error encountered while reading from .cnf file!");
            e.getStackTrace();
        }
        long parsingend=System.nanoTime();
        long parsingtime=parsingend-parsingstart;
        System.out.println("\nTime taken to Parse: "+ parsingtime/Math.pow(10,9) + "s");
        SATSolver solver=new SATSolver();
        long started=System.nanoTime();
        //System.out.println("\nThe logic reaches this point");
        Environment result=solver.solve(f);

        long ended=System.nanoTime();
        long time_taken=ended-started;
        System.out.println("\nTime taken: "+ time_taken/Math.pow(10.0,9.0) + "s");
        if (result==null){
            System.out.println("Unsatisfiable");
            try{
                File somename=new File("BoolAssignment.txt");
                FileWriter writer=new FileWriter(somename);
                writer.write("The equation is unsatisfiable");
                writer.write("\nThe runtime of the parsing process was: "+ parsingtime +" ns");
                writer.write("\nThe runnning time of the solcing process was: "+ time_taken+ "ns");
                writer.close();
            }
            catch (Exception e){
                System.out.println("\nError while writing to BoolAssignment.txt. Equation is unsatisfiable btw");
                e.getStackTrace();
            }


        }
        else{
            System.out.println("Satisfiable");
            try{
                File somename=new File("BoolAssignment.txt");
                FileWriter writer=new FileWriter(somename);

                if (somename.createNewFile()){
                    System.out.println("New file was created");
                }
                else{
                    System.out.println("\nFile already exists\nWriting on to file now... Open file to check!");
                }
                writer.write("\nThe equation is satisfiable\n");
                writer.write("\nThe runtime of the parsing process was: "+ parsingtime+ "ns\n");
                writer.write("\nThe runnning time of the solcing process was: "+ time_taken+ "ns\n");
                writer.write("\nVariable assignments if you really have the time to go through this lmao\n\n");
                writer.write(result.toString());
                writer.close();
            }
            catch(Exception e){
                System.out.println("Error while writing to BoolAssignment.txt. Equation is satisfiable btw");
                e.getStackTrace();
            }
        }

    }
}