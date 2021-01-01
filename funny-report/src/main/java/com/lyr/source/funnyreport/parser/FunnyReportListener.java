// Generated from /Users/lyr/workspace/funny-compiler/funny-report/src/main/java/com/lyr/source/funnyreport/parser/g4/FunnyReport.g4 by ANTLR 4.9
package com.lyr.source.funnyreport.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FunnyReportParser}.
 */
public interface FunnyReportListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#bracedExpression}.
	 * @param ctx the parse tree
	 */
	void enterBracedExpression(FunnyReportParser.BracedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#bracedExpression}.
	 * @param ctx the parse tree
	 */
	void exitBracedExpression(FunnyReportParser.BracedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(FunnyReportParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(FunnyReportParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(FunnyReportParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(FunnyReportParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(FunnyReportParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(FunnyReportParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(FunnyReportParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(FunnyReportParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(FunnyReportParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(FunnyReportParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(FunnyReportParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(FunnyReportParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyReportParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(FunnyReportParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyReportParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(FunnyReportParser.FloatLiteralContext ctx);
}