package com.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryDTO {

    private Long id;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "Available stock is required")
    @Min(value = 0, message = "stock cannot be negative")
    private Integer quantity;

    private String warehouseLocation;

    public InventoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }
}