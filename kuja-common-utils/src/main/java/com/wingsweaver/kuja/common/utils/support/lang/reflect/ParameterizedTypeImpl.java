package com.wingsweaver.kuja.common.utils.support.lang.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * {@link ParameterizedType} 的实现类。
 *
 * @author wingsweaver
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type ownerType;

    private final Type rawType;

    private final Type[] typeArguments;

    private final String rawTypeName;

    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type[] typeArguments) {
        this.ownerType = ownerType;
        this.rawType = rawType;
        this.typeArguments = typeArguments;
        if (rawType instanceof Class) {
            this.rawTypeName = ((Class<?>) rawType).getName();
        } else {
            this.rawTypeName = rawType.getTypeName();
        }
    }

    public ParameterizedTypeImpl(Type rawType, Type[] typeArguments) {
        this(null, rawType, typeArguments);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return typeArguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return this.ownerType;
    }

    @Override
    public String getTypeName() {
        StringBuilder typeName = new StringBuilder(this.rawTypeName);

        if (typeArguments.length > 0) {
            typeName.append("<");
            for (int i = 0; i < typeArguments.length; i++) {
                if (i > 0) {
                    typeName.append(", ");
                }
                if (typeArguments[i] instanceof Class) {
                    typeName.append(((Class<?>) typeArguments[i]).getName());
                } else {
                    typeName.append(typeArguments[i].getTypeName());
                }
            }
            typeName.append(">");
        }

        return typeName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType that = (ParameterizedType) o;
        return Objects.equals(ownerType, that.getOwnerType())
                && Objects.equals(rawType, that.getRawType())
                && Arrays.equals(typeArguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ownerType, rawType);
        result = 31 * result + Arrays.hashCode(typeArguments);
        return result;
    }
}