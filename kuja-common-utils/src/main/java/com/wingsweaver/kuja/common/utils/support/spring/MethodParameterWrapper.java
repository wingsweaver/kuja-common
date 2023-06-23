package com.wingsweaver.kuja.common.utils.support.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * {@link MethodParameter} 的封装类。
 *
 * @author wingsweaver
 */
public class MethodParameterWrapper extends MethodParameter {
    private final MethodParameter methodParameter;

    public MethodParameterWrapper(MethodParameter methodParameter) {
        super(methodParameter);
        this.methodParameter = methodParameter;
    }

    private Method method;

    @Override
    public Method getMethod() {
        if (this.method == null) {
            return this.methodParameter.getMethod();
        } else {
            return this.method;
        }
    }

    @SuppressWarnings("unused")
    public void setMethod(Method method) {
        this.method = method;
    }

    private Constructor<?> constructor;

    @Override
    public Constructor<?> getConstructor() {
        if (this.constructor == null) {
            return this.methodParameter.getConstructor();
        } else {
            return this.constructor;
        }
    }

    @SuppressWarnings("unused")
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    private Class<?> declaringClass;

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?> getDeclaringClass() {
        if (this.declaringClass == null) {
            return this.methodParameter.getDeclaringClass();
        } else {
            return this.declaringClass;
        }
    }

    @SuppressWarnings("unused")
    public void setDeclaringClass(Class<?> declaringClass) {
        this.declaringClass = declaringClass;
    }

    private Member member;

    @SuppressWarnings("NullableProblems")
    @Override
    public Member getMember() {
        if (this.member == null) {
            return this.methodParameter.getMember();
        } else {
            return this.member;
        }
    }

    @SuppressWarnings("unused")
    public void setMember(Member member) {
        this.member = member;
    }

    private AnnotatedElement annotatedElement;

    @SuppressWarnings("NullableProblems")
    @Override
    public AnnotatedElement getAnnotatedElement() {
        if (this.annotatedElement == null) {
            return this.methodParameter.getAnnotatedElement();
        } else {
            return this.annotatedElement;
        }
    }

    @SuppressWarnings("unused")
    public void setAnnotatedElement(AnnotatedElement annotatedElement) {
        this.annotatedElement = annotatedElement;
    }

    private Executable executable;

    @SuppressWarnings("NullableProblems")
    @Override
    public Executable getExecutable() {
        if (this.executable == null) {
            return this.methodParameter.getExecutable();
        } else {
            return this.executable;
        }
    }

    @SuppressWarnings("unused")
    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    private Parameter parameter;

    @SuppressWarnings("NullableProblems")
    @Override
    public Parameter getParameter() {
        if (this.parameter == null) {
            return this.methodParameter.getParameter();
        } else {
            return this.parameter;
        }
    }

    @SuppressWarnings("unused")
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    private Integer parameterIndex;

    @Override
    public int getParameterIndex() {
        if (this.parameterIndex == null) {
            return this.methodParameter.getParameterIndex();
        } else {
            return this.parameterIndex;
        }
    }

    @SuppressWarnings("unused")
    public void setParameterIndex(Integer parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void increaseNestingLevel() {
        this.methodParameter.increaseNestingLevel();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void decreaseNestingLevel() {
        this.methodParameter.decreaseNestingLevel();
    }

    @Override
    public int getNestingLevel() {
        return this.methodParameter.getNestingLevel();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public MethodParameter withTypeIndex(int typeIndex) {
        return this.methodParameter.withTypeIndex(typeIndex);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setTypeIndexForCurrentLevel(int typeIndex) {
        this.methodParameter.setTypeIndexForCurrentLevel(typeIndex);
    }

    @Override
    public Integer getTypeIndexForCurrentLevel() {
        return this.methodParameter.getTypeIndexForCurrentLevel();
    }

    @Override
    public Integer getTypeIndexForLevel(int nestingLevel) {
        return this.methodParameter.getTypeIndexForLevel(nestingLevel);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public MethodParameter nested() {
        return this.methodParameter.nested();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public MethodParameter nested(Integer typeIndex) {
        return this.methodParameter.nested(typeIndex);
    }

    @Override
    public boolean isOptional() {
        return this.methodParameter.isOptional();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public MethodParameter nestedIfOptional() {
        return this.methodParameter.nestedIfOptional();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public MethodParameter withContainingClass(Class<?> containingClass) {
        return this.methodParameter.withContainingClass(containingClass);
    }

    private Class<?> containingClass;

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?> getContainingClass() {
        if (this.containingClass == null) {
            return this.methodParameter.getContainingClass();
        } else {
            return this.containingClass;
        }
    }

    @SuppressWarnings({"squid:S2177", "unused", "PMD.MissingOverride"})
    public void setContainingClass(Class<?> containingClass) {
        this.containingClass = containingClass;
    }

    private Class<?> parameterType;

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?> getParameterType() {
        if (this.parameterType == null) {
            return this.methodParameter.getParameterType();
        } else {
            return this.parameterType;
        }
    }

    @SuppressWarnings({"squid:S2177", "PMD.MissingOverride"})
    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    private Type genericParameterType;

    @SuppressWarnings("NullableProblems")
    @Override
    public Type getGenericParameterType() {
        if (this.genericParameterType == null) {
            return this.methodParameter.getGenericParameterType();
        } else {
            return this.genericParameterType;
        }
    }

    public void setGenericParameterType(Type genericParameterType) {
        this.genericParameterType = genericParameterType;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?> getNestedParameterType() {
        return this.methodParameter.getNestedParameterType();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Type getNestedGenericParameterType() {
        return this.methodParameter.getNestedGenericParameterType();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Annotation[] getMethodAnnotations() {
        return this.methodParameter.getMethodAnnotations();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
        return this.methodParameter.getMethodAnnotation(annotationType);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
        return this.methodParameter.hasMethodAnnotation(annotationType);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Annotation[] getParameterAnnotations() {
        return this.methodParameter.getParameterAnnotations();
    }

    @Override
    public boolean hasParameterAnnotations() {
        return this.methodParameter.hasParameterAnnotations();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
        return this.methodParameter.getParameterAnnotation(annotationType);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
        return this.methodParameter.hasParameterAnnotation(annotationType);
    }

    @Override
    public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
    }

    private String parameterName;

    @Override
    public String getParameterName() {
        if (StringUtils.isEmpty(this.parameterName)) {
            return this.methodParameter.getParameterName();
        } else {
            return this.parameterName;
        }
    }

    @SuppressWarnings("unused")
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @SuppressWarnings({"com.haulmont.jpb.EqualsDoesntCheckParameterClass", "EqualsWhichDoesntCheckParameterClass"})
    @Override
    public boolean equals(Object other) {
        return this.methodParameter.equals(other);
    }

    @Override
    public int hashCode() {
        return this.methodParameter.hashCode();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return this.methodParameter.toString();
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod", "NullableProblems", "squid:S2975", "squid:S1182"})
    @Override
    public MethodParameter clone() {
        return new MethodParameterWrapper(this);
    }

    @SuppressWarnings("unused")
    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }
}
