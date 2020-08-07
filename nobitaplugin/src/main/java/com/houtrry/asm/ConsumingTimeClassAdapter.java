package com.houtrry.asm;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author: houtrry
 * @date: 2020/6/29 20:08
 * @version: $
 * @description:
 */
public class ConsumingTimeClassAdapter extends ClassVisitor {

    private boolean isAnnotationClass = false;
    private String className = "";

    public ConsumingTimeClassAdapter(int api) {
        super(api);
    }

    public ConsumingTimeClassAdapter(int api, ClassVisitor cv) {
        super(api, cv);
    }

    /**
     * 该方法是当扫描类时第一个调用的方法, 主要用于类声明使用
     *
     * @param version    类版本
     * @param access     修饰符
     * @param name       类名
     * @param signature  泛型信息
     * @param superName  继承的父类
     * @param interfaces 实现的接口
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
    }

    /**
     * 该方法是当扫描器扫描到类注解声明时进行调用
     *
     * @param descriptor 注解类型
     * @param visible    注解是否可以在 JVM 中可见
     * @return
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        isAnnotationClass = descriptor.contains(Constants.NOBITA_ANNOTATION);
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * 该方法是当扫描器扫描到类中字段时进行调用
     *
     * @param access     修饰符
     * @param name       字段名
     * @param descriptor 字段类型
     * @param signature  泛型描述
     * @param value      默认值
     * @return
     */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    private static final String INIT_NAME = "<init>";
    /**
     * 该方法是当扫描器扫描到类的方法时进行调用
     *
     * @param access     修饰符
     * @param name       方法名
     * @param desc       方法签名
     * @param signature  泛型信息
     * @param exceptions 抛出的异常
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        String message = "===access: "+access+", name: "+name+", desc: "+desc+", signature: "+signature+", exceptions: "+exceptions;
        System.out.println(message);
        if (INIT_NAME.equals(name)) {
            //如果是构造方法, 就不插入代码
            return methodVisitor;
        }
        methodVisitor = new ConsumingTimeMethodVisitor(Opcodes.ASM7, methodVisitor, access, name, desc, isAnnotationClass, className);
        return methodVisitor;
    }

    /**
     * 该方法是当扫描器完成类扫描时才会调用,
     * 如果想在类中追加某些方法, 可以在这里处理
     */
    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
