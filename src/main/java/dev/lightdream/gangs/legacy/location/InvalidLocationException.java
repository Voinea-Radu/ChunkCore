package dev.lightdream.gangs.legacy.location;

public class InvalidLocationException extends RuntimeException {
    InvalidLocationException(String var1) {
        super(var1);
    }

    public InvalidLocationException() {
        super("Invalid location specified");
    }
}
