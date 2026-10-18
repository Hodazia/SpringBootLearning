package com.example.firstspringprjct.Utils.FileExceptions;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(Long id)
    {
        super("File not found with id: " + id);
    }
}
