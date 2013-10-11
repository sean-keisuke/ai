package source;

public interface Action<C extends State>
{
    /** returns a new state based on the given action */
    public C updateState(C s);
}


