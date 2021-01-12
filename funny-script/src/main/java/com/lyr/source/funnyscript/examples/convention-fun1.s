	.section	__TEXT,__text,regular,pure_instructions

## 过程:fun1
	.globl _fun1
_fun1:

	# 序曲
	pushq	%rbp
	movq	%rsp, %rbp

	# 设置栈顶
	subq	$16, %rsp

	# 过程体
	movl	$10, -4(%rbp)
	movl	%edi, %eax
	addl	%esi, %eax
	addl	%edx, %eax
	addl	%ecx, %eax
	addl	%r8d, %eax
	addl	%r9d, %eax
	addl	16(%rbp), %eax
	addl	24(%rbp), %eax
	addl	-4(%rbp), %eax

	# 返回值
	# 返回值在之前的计算中,已经存入%eax

	# 恢复栈顶
	addq	$16, %rsp

	# 尾声
	popq	%rbp
	retq

