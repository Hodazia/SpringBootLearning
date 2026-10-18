package com.example.firstspringprjct.Utils.FileExceptions;

public class InvalidFileException extends  RuntimeException {

    public InvalidFileException(String message)
    {
        super(message);
    }
}
