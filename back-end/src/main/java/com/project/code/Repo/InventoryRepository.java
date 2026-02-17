package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Derived query method (no @Query needed)
    Inventory findByProduct_IdAndStore_Id(Long productId, Long storeId);

    List<Inventory> findByStore_Id(Long storeId);

    void deleteByProduct_Id(Long productId);
}