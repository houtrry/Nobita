package com.houtrry.asm;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author: houtrry
 * @date: 2020/6/29 20:18
 * @version: $
 * @description: MethodVisitor 是一个抽象类，当 ASM 的 ClassReader 读取到 Method 时就转入 MethodVisitor 接口处理。
 * AdviceAdapter 是 MethodVisitor 的子类，使用 AdviceAdapter 可以更方便的修改方法的字节码
 */
public class ConsumingTimeMethodVisitor extends AdviceAdapter {

    private int startTimeId = -1;
    private boolean isAnnotationClass = false;
    private boolean isAnnotationMethod = false;

    private String className = "";
    private String methodName = "";

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api    the ASM API version implemented by this visitor. Must be one
     *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param mv     the method visitor to which this adapter delegates calls.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name   the method's name.
     * @param desc   the method's descriptor (see {@link Type Type}).
     */
    protected ConsumingTimeMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, boolean isAnnotationClass, String className) {
        super(api, mv, access, name, desc);
        this.isAnnotationClass = isAnnotationClass;
        this.className = className;
        this.methodName = name;
    }

    /**
     * 表示 ASM 开始扫描这个方法
     */
    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, desc: "+desc);
        if (Constants.NOBITA_ANNOTATION.equals(desc)) {
            isAnnotationMethod = true;
        } else {
            System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, desc->1: "+desc);
            System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, desc->2: "+Constants.NOBITA_ANNOTATION);
        }
        System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, isAnnotationMethod: "+isAnnotationMethod);

        return super.visitAnnotation(desc, visible);
    }

    /**
     * 进入这个方法
     */
//    @Override
//    protected void onMethodEnter() {
//        super.onMethodEnter();
//        final boolean isInjected = isAnnotationClass || isAnnotationMethod;
//        startTimeId = newLocal(Type.LONG_TYPE);
//        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//        mv.visitIntInsn(LSTORE, startTimeId);
//    }

    /**
     * 即将从这个方法出去
     * @param opcode
     */
//    @Override
//    protected void onMethodExit(int opcode) {
//        super.onMethodExit(opcode);
//        final boolean isInjected = isAnnotationClass || isAnnotationMethod;
//        if (opcode == RETURN) {
//            visitInsn(ACONST_NULL);
//        } else if (opcode == ARETURN || opcode == ATHROW) {
//            dup();
//        } else {
//            if (opcode == LRETURN || opcode == DRETURN) {
//                dup2();
//            } else {
//                dup();
//            }
//            box(Type.getReturnType(this.methodDesc));
//        }
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitLdcInsn("costTime is ");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//        mv.visitVarInsn(LLOAD, startTimeId);
//        mv.visitInsn(LSUB);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//    }

    /**
     * 表示方法扫码完毕
     */
    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }


    private int timeLocalIndex = 0;

    @Override
    protected void onMethodEnter() {
        System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>onMethodEnter, isAnnotationMethod: "+isAnnotationMethod+", isAnnotationClass: "+isAnnotationClass);
        if (isAnnotationClass || isAnnotationMethod) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            timeLocalIndex = newLocal(Type.LONG_TYPE); //这个是LocalVariablesSorter 提供的功能，可以尽量复用以前的局部变量
            mv.visitVarInsn(LSTORE, timeLocalIndex);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isAnnotationClass || isAnnotationMethod) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, timeLocalIndex);
            mv.visitInsn(LSUB);//此处的值在栈顶
            mv.visitVarInsn(LSTORE, timeLocalIndex);//因为后面要用到这个值所以先将其保存到本地变量表中


//        int stringBuilderIndex = newLocal(Type.getType("java/lang/StringBuilder"));
            int stringBuilderIndex = newLocal(Type.getType(StringBuilder.class));
            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ASTORE, stringBuilderIndex);//需要将栈顶的 stringbuilder 保存起来否则后面找不到了
            mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
            mv.visitLdcInsn(className + "." + methodName + " cost time:");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitInsn(Opcodes.POP);//将 append 方法的返回值从栈里 pop 出去
            mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
            mv.visitVarInsn(Opcodes.LLOAD, timeLocalIndex);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitInsn(Opcodes.POP);//将 append 方法的返回值从栈里 pop 出去
            mv.visitLdcInsn("Nobita");
            mv.visitVarInsn(Opcodes.ALOAD, stringBuilderIndex);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);//注意： Log.d 方法是有返回值的，需要 pop 出去
            mv.visitInsn(Opcodes.POP);//插入字节码后要保证栈的清洁，不影响原来的逻辑，否则就会产生异常，也会对其他框架处理字节码造成影响

//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitLdcInsn("costTime is ");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
//        mv.visitVarInsn(LLOAD, timeLocalIndex);
//        mv.visitInsn(LSUB);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//        mv.visitInsn(Opcodes.POP);
//        mv.visitInsn(IRETURN);
//        mv.visitMaxs(6, 5);
//        mv.visitEnd();
        }
    }

}
