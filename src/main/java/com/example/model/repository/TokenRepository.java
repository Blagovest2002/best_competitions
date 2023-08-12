package com.example.model.repository;

import com.example.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
   List<Token> findTokenByUserId(int userId);
   Token findTokenByToken(String token);
}
