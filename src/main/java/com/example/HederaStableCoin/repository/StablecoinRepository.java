package com.example.HederaStableCoin.repository;

import com.example.HederaStableCoin.model.entity.StablecoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StablecoinRepository extends JpaRepository<StablecoinEntity, String> {
}