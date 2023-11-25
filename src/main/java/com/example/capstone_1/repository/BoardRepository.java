package com.example.capstone_1.repository;

public interface BoardRepository {
    String getWriterOfBoard(Long bno);
    Long getSecretOfBoard(Long bno);
}
