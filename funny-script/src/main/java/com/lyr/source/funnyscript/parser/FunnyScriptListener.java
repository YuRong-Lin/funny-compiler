// Generated from E:/git-space-github/funny-compiler/funny-script/src/main/java/com/lyr/source/funnyscript\FunnyScript.g4 by ANTLR 4.9

package com.lyr.source.funnyscript.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FunnyScriptParser}.
 */
public interface FunnyScriptListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(FunnyScriptParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(FunnyScriptParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(FunnyScriptParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(FunnyScriptParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(FunnyScriptParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(FunnyScriptParser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(FunnyScriptParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(FunnyScriptParser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(FunnyScriptParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(FunnyScriptParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(FunnyScriptParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypeTypeOrVoid(FunnyScriptParser.TypeTypeOrVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypeTypeOrVoid(FunnyScriptParser.TypeTypeOrVoidContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedNameList(FunnyScriptParser.QualifiedNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedNameList(FunnyScriptParser.QualifiedNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(FunnyScriptParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(FunnyScriptParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(FunnyScriptParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(FunnyScriptParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(FunnyScriptParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(FunnyScriptParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void enterLastFormalParameter(FunnyScriptParser.LastFormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void exitLastFormalParameter(FunnyScriptParser.LastFormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifier(FunnyScriptParser.VariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifier(FunnyScriptParser.VariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(FunnyScriptParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(FunnyScriptParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(FunnyScriptParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(FunnyScriptParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(FunnyScriptParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(FunnyScriptParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(FunnyScriptParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(FunnyScriptParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(FunnyScriptParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(FunnyScriptParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(FunnyScriptParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(FunnyScriptParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(FunnyScriptParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(FunnyScriptParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(FunnyScriptParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(FunnyScriptParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(FunnyScriptParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(FunnyScriptParser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(FunnyScriptParser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(FunnyScriptParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(FunnyScriptParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(FunnyScriptParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(FunnyScriptParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(FunnyScriptParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(FunnyScriptParser.FloatLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(FunnyScriptParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(FunnyScriptParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(FunnyScriptParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(FunnyScriptParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatements(FunnyScriptParser.BlockStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#blockStatements}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatements(FunnyScriptParser.BlockStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(FunnyScriptParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(FunnyScriptParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(FunnyScriptParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(FunnyScriptParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(FunnyScriptParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(FunnyScriptParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(FunnyScriptParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(FunnyScriptParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(FunnyScriptParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(FunnyScriptParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(FunnyScriptParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(FunnyScriptParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(FunnyScriptParser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(FunnyScriptParser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(FunnyScriptParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(FunnyScriptParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(FunnyScriptParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(FunnyScriptParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(FunnyScriptParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(FunnyScriptParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(FunnyScriptParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(FunnyScriptParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(FunnyScriptParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(FunnyScriptParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(FunnyScriptParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(FunnyScriptParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#typeType}.
	 * @param ctx the parse tree
	 */
	void enterTypeType(FunnyScriptParser.TypeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#typeType}.
	 * @param ctx the parse tree
	 */
	void exitTypeType(FunnyScriptParser.TypeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#functionType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionType(FunnyScriptParser.FunctionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#functionType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionType(FunnyScriptParser.FunctionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(FunnyScriptParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(FunnyScriptParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(FunnyScriptParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(FunnyScriptParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void enterSuperSuffix(FunnyScriptParser.SuperSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void exitSuperSuffix(FunnyScriptParser.SuperSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunnyScriptParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(FunnyScriptParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunnyScriptParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(FunnyScriptParser.ArgumentsContext ctx);
}