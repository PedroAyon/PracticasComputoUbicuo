package server;

public class PlayerAlreadyExistsException extends Exception{
    public PlayerAlreadyExistsException(){
        super("Player already exists.");
    }
}
