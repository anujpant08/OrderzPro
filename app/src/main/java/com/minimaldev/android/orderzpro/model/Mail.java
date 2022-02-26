package com.minimaldev.android.orderzpro.model;

import androidx.annotation.NonNull;

public class Mail {
    String description;
    String productSize;
    String deliveryAddress;
    String price;
    String sourceName;
    String expectedDeliveryDate;
    String DeliveredDate;
    String orderedOnDate;
    String paymentMode;
    int quantity;
    boolean isDelivered = false;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getDeliveredDate() {
        return DeliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        DeliveredDate = deliveredDate;
    }

    public String getOrderedOnDate() {
        return orderedOnDate;
    }

    public void setOrderedOnDate(String orderedOnDate) {
        this.orderedOnDate = orderedOnDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mail{" +
                "description='" + description + '\'' +
                ", productSize='" + productSize + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", price='" + price + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", expectedDeliveryDate='" + expectedDeliveryDate + '\'' +
                ", DeliveredDate='" + DeliveredDate + '\'' +
                ", orderedOnDate='" + orderedOnDate + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", quantity=" + quantity +
                ", isDelivered=" + isDelivered +
                '}';
    }
}
