package xan_code.dathandler.io;

class ClassMapping
{
    public short code;
    public Class<?> sclass;
    public Streamer streamer;

    public ClassMapping (short code, Class<?> sclass, Streamer streamer)
    {
        this.code = code;
        this.sclass = sclass;
        this.streamer = streamer;
    }

    @Override
    public String toString ()
    {
        return "[code=" + code + ", class=" + sclass.getName() + "]";
    }
}