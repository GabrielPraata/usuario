package com.Prata.usuario.infrastructure.exception;

public class ResourceNotFoundExeption extends RuntimeException {
    public ResourceNotFoundExeption(String message) {
        super(message);

    }
    public ResourceNotFoundExeption(String mensagem, Throwable throwable){
        super(mensagem, throwable);
    }
}
