package com.lyr.source.lab.repl;

import com.lyr.source.lab.SimpleScript;
import com.lyr.source.lab.parser.ASTNode;
import com.lyr.source.lab.parser.SimpleParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @Author LinYuRong
 * @Date 2020/12/18 18:04
 * @Version 1.0
 */
public class SimpleREPL {

    public static void main(String[] args) {
        boolean verbose = false;
        if (args.length > 0 && args[0].equals("-v")) {
            verbose = true;
            System.out.println("verbose mode");
        }
        System.out.println("Simple script language!");

        SimpleParser parser = new SimpleParser();
        SimpleScript script = new SimpleScript();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String scriptText = "";
        System.out.print("\n>");   //提示符

        while (true) {
            try {
                String line = reader.readLine().trim();
                if (line.equals("exit();")) {
                    System.out.println("good bye!");
                    break;
                }
                scriptText += line + "\n";
                if (line.endsWith(";")) {
                    ASTNode tree = parser.parse(scriptText);
                    if (verbose) {
                        parser.dumpAST(tree, "");
                    }

                    script.evaluate(tree, "", verbose);

                    System.out.print("\n>");   //提示符

                    scriptText = "";
                }

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                System.out.print("\n>");   //提示符
                scriptText = "";
            }
        }
    }
}
