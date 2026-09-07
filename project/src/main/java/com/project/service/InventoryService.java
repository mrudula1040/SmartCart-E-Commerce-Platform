package com.project.service;

import com.project.entity.Inventory;
import com.project.repository.InventoryRepository;
import com.sun.jdi.LongValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);

    }
    public List<Inventory> getAllInventory(){
        return inventoryRepository.findAll();
    }
    public Optional<Inventory> getInventoryById(Long id){
        return inventoryRepository.findById(id);
    }
    public Inventory updateStock(Long id, Integer quantity){
        Inventory existingInventory = inventoryRepository.findById(id).orElseThrow(()->new RuntimeException("Inventory not found"));
        existingInventory.setQuantity(quantity);
        return inventoryRepository.save(existingInventory);
    }
    public void deleteInventory(Long id){
        inventoryRepository.deleteById(id);
    }
}
