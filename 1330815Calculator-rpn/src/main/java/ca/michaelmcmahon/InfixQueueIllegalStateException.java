package ca.michaelmcmahon;

/**
 * 
 * The exception used in the Expression parser.
 * 
 * @author Michael McMahon
 * @version 1.0
 * 
 */
public class InfixQueueIllegalStateException extends Exception
{
    public InfixQueueIllegalStateException(String message) 
    {
        super(message);
    }
}
