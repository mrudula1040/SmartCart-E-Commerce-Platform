package com.project.service;

import com.project.dto.InventoryDTO;
import com.project.entity.Inventory;
import com.project.entity.Product;
import com.project.repository.InventoryRepository;
import com.project.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository) {

        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {

        Product product = productRepository.findById(
                inventoryDTO.getProductId()
        ).orElseThrow(() ->
                new RuntimeException("Product not found")
        );

        // Keep Product stock and Inventory quantity synchronized
        product.setStock(inventoryDTO.getQuantity());
        productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setWarehouseLocation(
                inventoryDTO.getWarehouseLocation()
        );

        Inventory savedInventory = inventoryRepository.save(inventory);

        return convertToDTO(savedInventory);
    }

    public List<InventoryDTO> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<InventoryDTO> getInventoryById(Long id) {

        return inventoryRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Optional<InventoryDTO> getInventoryByProductId(
            Long productId) {

        return inventoryRepository.findByProductId(productId)
                .map(this::convertToDTO);
    }

    @Transactional
    public Optional<InventoryDTO> updateInventory(
            Long id,
            InventoryDTO inventoryDTO) {

        return inventoryRepository.findById(id)
                .map(existingInventory -> {

                    Product existingProduct =
                            existingInventory.getProduct();

                    // Keep the existing product linked to this inventory record.
                    // Edit its quantity rather than changing the linked product.
                    if (!existingProduct.getId().equals(
                            inventoryDTO.getProductId())) {
                        throw new IllegalArgumentException(
                                "Please keep the same product when editing inventory."
                        );
                    }

                    // Update the product's stock as well
                    existingProduct.setStock(
                            inventoryDTO.getQuantity()
                    );
                    productRepository.save(existingProduct);

                    // Update inventory details
                    existingInventory.setQuantity(
                            inventoryDTO.getQuantity()
                    );

                    existingInventory.setWarehouseLocation(
                            inventoryDTO.getWarehouseLocation()
                    );

                    Inventory updatedInventory =
                            inventoryRepository.save(existingInventory);

                    return convertToDTO(updatedInventory);
                });
    }

    public boolean deleteInventory(Long id) {

        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private InventoryDTO convertToDTO(Inventory inventory) {

        InventoryDTO inventoryDTO = new InventoryDTO();

        inventoryDTO.setId(inventory.getId());
        inventoryDTO.setProductId(
                inventory.getProduct().getId()
        );
        inventoryDTO.setQuantity(
                inventory.getQuantity()
        );
        inventoryDTO.setWarehouseLocation(
                inventory.getWarehouseLocation()
        );

        return inventoryDTO;
    }
}