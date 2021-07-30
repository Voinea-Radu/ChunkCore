package dev.lightdream.gangs.legacy.location;

public class InvalidLocationWorldException extends InvalidLocationException {
    public InvalidLocationWorldException() {
        super("Invalid world specified");
    }
}
