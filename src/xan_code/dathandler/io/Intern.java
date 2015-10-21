package xan_code.dathandler.io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Flags a {@link String} field in a streamable object as being a member of the global string pool
 * accessed using {@link String#intern}.  Each unique value will be streamed only once, with
 * further instances replaced by a compact reference.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Intern
{
}
