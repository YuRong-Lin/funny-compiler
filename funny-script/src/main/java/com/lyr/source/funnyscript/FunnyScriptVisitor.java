// Generated from E:/git-space-github/funny-compiler/funny-script/src/main/java/com/lyr/source/funnyscript\FunnyScript.g4 by ANTLR 4.9

package com.lyr.source.funnyscript;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FunnyScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FunnyScriptVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(FunnyScriptParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(FunnyScriptParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyDeclaration(FunnyScriptParser.ClassBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#memberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaration(FunnyScriptParser.MemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(FunnyScriptParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeTypeOrVoid(FunnyScriptParser.TypeTypeOrVoidContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedNameList(FunnyScriptParser.QualifiedNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(FunnyScriptParser.FormalParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(FunnyScriptParser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#formalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameter(FunnyScriptParser.FormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLastFormalParameter(FunnyScriptParser.LastFormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#variableModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableModifier(FunnyScriptParser.VariableModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(FunnyScriptParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(FunnyScriptParser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaration(FunnyScriptParser.ConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#variableDeclarators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(FunnyScriptParser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaratorId(FunnyScriptParser.VariableDeclaratorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(FunnyScriptParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#arrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInitializer(FunnyScriptParser.ArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceType(FunnyScriptParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#typeArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgument(FunnyScriptParser.TypeArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(FunnyScriptParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#integerLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(FunnyScriptParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#floatLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLiteral(FunnyScriptParser.FloatLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(FunnyScriptParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(FunnyScriptParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#blockStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatements(FunnyScriptParser.BlockStatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(FunnyScriptParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(FunnyScriptParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBlockStatementGroup(FunnyScriptParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#switchLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchLabel(FunnyScriptParser.SwitchLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(FunnyScriptParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(FunnyScriptParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#enhancedForControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnhancedForControl(FunnyScriptParser.EnhancedForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#parExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpression(FunnyScriptParser.ParExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(FunnyScriptParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(FunnyScriptParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(FunnyScriptParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(FunnyScriptParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(FunnyScriptParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#typeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeType(FunnyScriptParser.TypeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#functionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionType(FunnyScriptParser.FunctionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(FunnyScriptParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(FunnyScriptParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#superSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperSuffix(FunnyScriptParser.SuperSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunnyScriptParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(FunnyScriptParser.ArgumentsContext ctx);
}