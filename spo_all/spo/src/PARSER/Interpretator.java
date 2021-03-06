package PARSER;

import LEXER.Lexeme;
import LEXER.Token;
import Collections.List;

import java.util.*;

public class Interpretator {
    HashMap <String, Integer> tableOfVar;
    ArrayList<Token> polis;
    private Stack<String> buffer = new Stack<>();
    private HashMap<String, List> tableOfCollections;
    List<String> list;

    int a, b, c;
    boolean flag = false;

    public Interpretator(Parser parser){
        tableOfVar = parser.getPolis().getTableOfVar();
        tableOfCollections = parser.getTableOfCollections();
        polis = parser.getPolis().getPolis();

        Start();
    }

    private void Start(){
        Token tok;
        for(int position = 0; position <= polis.size() - 1; position ++ ){
            tok = polis.get(position);
            //System.out.println( " my Pos = " + position + " - " +  tok.getValue());

            if(tok.getLexeme() == Lexeme.VAR){
                buffer.push(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.DIGIT){
                buffer.push(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.LIST){
                buffer.push(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.OP){
                operation(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.FUNCTIONS){
                functions(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.LOG_OP){
                buffer.push(String.valueOf(logicOperation(tok.getValue())));
            } else if (tok.getLexeme() == Lexeme.ASSIGN_OP){
                assignOp();
            } else if (tok.getLexeme() == Lexeme.NF){
                a = valueOrVariable(tableOfVar) -1;
                flag = buffer.pop().equals("true");
                position = flag ? position : a;
            } else if (tok.getLexeme() == Lexeme.F){
                a = valueOrVariable(tableOfVar) - 1;
                    position  = a;
            }else if (tok.getLexeme() == Lexeme.PRINT){
                System.out.println("\nVariables : " +  tableOfVar + "\n");
                System.out.println("Lists: " + tableOfCollections);
            } else if (tok.getLexeme() == Lexeme.EOF){
                //System.out.println("\n\nEOF!!! ");
            }

            //System.out.println(tableOfVar.toString());
        }
    }

    private void operation(String op) {
        setAB();
        switch (op) {
            case "+":
                c = a + b;
                break;
            case "-":
                c = a - b;
                break;
            case "/":
                c = a / b;
                break;
            case "*":
                c = a * b;
                break;
        }

        buffer.push(String.valueOf(c));
    }

    private boolean logicOperation(String logOp){
        boolean flag = false;
        setAB();
        switch (logOp) {
            case "<":
                flag = a < b;
                break;
            case ">":
                flag = a > b;
                break;
            case "==":
                flag = a == b;
                break;
            case "!=":
                flag = a != b;
                break;
            case "<=":
                flag = a <= b;
                break;
            case ">=":
                flag = a >= b;
                break;
        }

        return flag;
    }

    private boolean functions(String funktions){
        if(!funktions.equals("size")){
            a = valueOrVariable(tableOfVar);//System.out.print("a = " + a);
        }

        String s = "";
        list = tableOfCollections.get(buffer.pop());

        switch (funktions) {
            case "add":
                list.add(String.valueOf(a));
                break;
            case "get":
                s = list.get(a);
                break;
            case "remove":
                s = list.remove(a);
                break;
            case "size":
                s = String.valueOf(list.getSize());
                break;
        }

        if(!s.equals("")){
            buffer.push(String.valueOf(s));
        }

        return flag;
    }

    private void assignOp(){
        a = valueOrVariable(tableOfVar);
        tableOfVar.put(buffer.pop(), a);
    }

    private int valueOrVariable(Map<String, Integer> table) throws EmptyStackException {
        if(isDigit(buffer.peek())){
            return Integer.valueOf(buffer.pop());
        } else if(!isDigit(buffer.peek())){
            return table.get(buffer.pop());
        } else{
            System.err.println();
            System.exit(10);
        }

        return -1;
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setAB(){
        b = valueOrVariable(tableOfVar);
        a = valueOrVariable(tableOfVar);
    }
}