package com.example.firstspringprjct.Utils.FileExceptions;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message,Throwable cause)
    {
        super(message, cause);
    }

    public FileStorageException(String message)
    {
        super(message);
    }
}
