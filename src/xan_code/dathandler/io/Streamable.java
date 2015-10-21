package xan_code.dathandler.io;

public interface Streamable
{
    /**
     * A marker interface for streamable classes that expect to be extended anonymously, but for
     * which the implicit outer class reference can (and should) be ignored. This allows one to
     * package up units of code and ship them between peers, or even between client and server.
     */
    public interface Closure extends Streamable {}
}