package com.example.android.apis;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActivityIsReviewed {
    public double sequence() default 999;
}
