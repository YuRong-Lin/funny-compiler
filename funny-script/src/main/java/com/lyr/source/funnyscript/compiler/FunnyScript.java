package com.lyr.source.funnyscript.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令行工具。可以执行一个脚本，或者以REPL模式运行。
 *
 * @Author LinYuRong
 * @Date 2020/12/29 14:33
 * @Version 1.0
 */
public class FunnyScript {

    public static void main(String[] args) {
        // 脚本
        String script = null;
        Map params;

        // 解析参数
        try {
            params = parseParams(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        boolean help = params.containsKey("help") ? (Boolean) params.get("help") : false;
        if (help) {
            showHelp();
            return;
        }

        // 测试脚本路径
        String path = null;
        if (params.containsKey("test_path")) {
            path = FunnyScript.class.getResource("../").getPath() + File.separator + params.get("test_path");
        }

        // 从源代码读取脚本
        String scriptFile = params.containsKey("scriptFile") ? (String) params.get("scriptFile") : null;
        if (scriptFile != null) {
            try {
                if (path != null) {
                    scriptFile = path + File.separator + scriptFile;
                }
                script = readTextFile(scriptFile);
            } catch (IOException e) {
                System.out.println("unable to read from : " + scriptFile);
                return;
            }
        }

        //是否生成汇编代码
        boolean genAsm = params.containsKey("genAsm") ? (Boolean) params.get("genAsm") : false;

        //是否生成字节码
        boolean genByteCode = params.containsKey("genByteCode") ? (Boolean) params.get("genByteCode") : false;

        //打印编译过程中的信息
        boolean verbose = params.containsKey("verbose") ? (Boolean) params.get("verbose") : false;

        //打印AST
        boolean ast_dump = params.containsKey("ast_dump") ? (Boolean) params.get("ast_dump") : false;

        //进入REPL
        if (script == null) {
            REPL(verbose, ast_dump);
        }

//        //生成汇编代码
//        else if (genAsm) {
//            //输出文件
//            String outputFile = params.containsKey("outputFile") ? (String)params.get("outputFile") : null;
//            generateAsm(script, outputFile);
//        }
//
//        //生成Java字节码
//        else if (genByteCode) {
//            //输出文件
//            //String outputFile = params.containsKey("outputFile") ? (String)params.get("outputFile") : null;
//            byte[] bc = generateByteCode(script);
//            runJavaClass("DefaultPlayClass", bc);
//        }

        //执行脚本
        else {
            FunnyScriptCompiler compiler = new FunnyScriptCompiler();
            AnnotatedTree at = compiler.compile(script, verbose, ast_dump);

            if (!at.hasCompilationError()) {
                compiler.execute(at);
            }
        }
    }

    /**
     * 解析参数
     *
     * @param args
     * @return
     */
    private static Map parseParams(String args[]) throws Exception {
        Map<String, Object> params = new HashMap<>();

        for (int i = 0; i < args.length; i++) {

            // 输出汇编代码
            if (args[i].equals("-S")) {
                params.put("genAsm", true);
            }

            // 生成字节码
            else if (args[i].equals("-bc")) {
                params.put("genByteCode", true);
            }

            // 显示作用域和符号
            else if (args[i].equals("-h") || args[i].equals("--help")) {
                params.put("help", true);
            }

            // 显示作用域和符号
            else if (args[i].equals("-v")) {
                params.put("verbose", true);
            }

            // 显示AST
            else if (args[i].equals("-ast-dump")) {
                params.put("ast_dump", true);
            }

            // 输出文件
            else if (args[i].equals("-o")) {
                if (i + 1 < args.length) {
                    params.put("outputFile", args[++i]);
                } else {
                    throw new Exception("Expecting a filename after -o");
                }
            }

            // 本地测试脚本的相对路径
            else if (args[i].equals("-path")) {
                params.put("test_path", args[++i]);
            }

            //不认识的参数
            else if (args[i].startsWith("-")) {
                throw new Exception("Unknown parameter : " + args[i]);
            }

            //脚本文件
            else {
                params.put("scriptFile", args[i]);
            }
        }

        return params;
    }

    /**
     * 打印帮助信息
     */
    private static void showHelp() {
        System.out.println("usage: java _dir_.FunnyScript [-h | --help | -o outputfile | -S | -bc | -v | -ast-dump] [scriptfile]");

        System.out.println("\t-h or --help : print this help information");
        System.out.println("\t-v verbose mode : dump AST and symbols");
        System.out.println("\t-ast-dump : dump AST in lisp style");
        System.out.println("\t-o outputfile : file pathname used to save generated code, eg. assembly code");
        System.out.println("\t-S : compile to assembly code");
        System.out.println("\t-bc : compile to java byte code");
        System.out.println("\tscriptfile : file contains playscript code");

        System.out.println("\nexamples:");
        System.out.println("\tjava compiler.FunnyScript");
        System.out.println("\t>>interactive REPL mode");
        System.out.println();

        System.out.println("\tjava compiler.FunnyScript -v");
        System.out.println("\t>>enter REPL with verbose mode, dump ast and symbols");
        System.out.println();

        System.out.println("\tjava compiler.FunnyScript scratch.funny");
        System.out.println("\t>>compile and execute scratch.funny");
        System.out.println();

        System.out.println("\tjava compiler.FunnyScript -v scratch.funny");
        System.out.println("\t>>compile and execute scratch.funny in verbose mode, dump ast and symbols");
        System.out.println();

        System.out.println("\tjava compiler.FunnyScript -bc scratch.funny");
        System.out.println("\t>>compile to bytecode, save as DefaultPlayClass.class and run it");
        System.out.println();
    }

    /**
     * 读文本文件
     *
     * @param pathName
     * @return
     * @throws IOException
     */
    private static String readTextFile(String pathName) throws IOException {
        StringBuffer buffer = new StringBuffer();
        try (FileReader reader = new FileReader(pathName);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append('\n');
            }
        }
        return buffer.toString();
    }

    /**
     * REPL
     */
    private static void REPL(boolean verbose, boolean ast_dump) {
        System.out.println("Enjoy FunnyScript!");

        FunnyScriptCompiler compiler = new FunnyScriptCompiler();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String script = "";

        String scriptLet = "";
        System.out.print("\n>");   //提示符

        while (true) {
            try {
                String line = reader.readLine().trim();
                if (line.equals("exit();")) {
                    System.out.println("good bye!");
                    break;
                }
                scriptLet += line + "\n";
                if (line.endsWith(";")) {

                    // 解析整个脚本文件
                    AnnotatedTree at = compiler.compile(script + scriptLet, verbose, ast_dump);

                    //重新执行整个脚本
                    if (!at.hasCompilationError()) {
                        Object result = compiler.execute(at);
                        System.out.println(result);
                        script = script + scriptLet;
                    }

                    System.out.print("\n>");   //提示符

                    scriptLet = "";
                }

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                System.out.print("\n>");   //提示符
                scriptLet = "";
            }
        }
    }
}
