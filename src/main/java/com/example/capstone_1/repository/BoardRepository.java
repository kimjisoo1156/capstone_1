package com.example.capstone_1.repository;

public interface BoardRepository {
    String getWriterOfBoard(Long bno);
    String getSecretOfBoard(Long bno);
}
