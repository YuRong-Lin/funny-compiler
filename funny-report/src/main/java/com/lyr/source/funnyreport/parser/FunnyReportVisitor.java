// Generated from /Users/lyr/workspace/funny-compiler/funny-report/src/main/java/com/lyr/source/funnyreport/parser/g4/FunnyReport.g4 by ANTLR 4.9
package com.lyr.source.funnyreport.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FunnyReportParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FunnyReportVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#bracedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracedExpression(FunnyReportParser.BracedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(FunnyReportParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(FunnyReportParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(FunnyReportParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(FunnyReportParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(FunnyReportParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#integerLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(FunnyReportParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyReportParser#floatLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLiteral(FunnyReportParser.FloatLiteralContext ctx);
}