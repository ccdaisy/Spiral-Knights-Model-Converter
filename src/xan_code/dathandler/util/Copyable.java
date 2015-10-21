package xan_code.dathandler.util;

public interface Copyable
{
    /**
     * Creates a copy of this object, (re)populating the supplied destination object if possible.
     *
     * @return either a reference to the destination object, if it could be repopulated, or a new
     * object containing the copied state.
     */
    public Object copy (Object dest);

    /**
     * Creates a copy of this object, (re)populating the supplied destination object if possible.
     *
     * @param outer the outer object reference to use for inner object creation, if any.
     * @return either a reference to the destination object, if it could be repopulated, or a new
     * object containing the copied state.
     */
    public Object copy (Object dest, Object outer);
}