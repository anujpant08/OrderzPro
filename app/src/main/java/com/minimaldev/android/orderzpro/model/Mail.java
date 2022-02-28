package com.minimaldev.android.orderzpro.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "mail_table")
public class Mail {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mail_id")
    public Integer mailId;

    String description;

    @ColumnInfo(name = "product_size")
    String productSize;

    @ColumnInfo(name = "delivery_address")
    String deliveryAddress;

    String price;

    @ColumnInfo(name = "source_name")
    String sourceName;

    @ColumnInfo(name = "expected_delivery_date")
    String expectedDeliveryDate;

    @ColumnInfo(name = "delivered_date")
    String DeliveredDate;

    @ColumnInfo(name = "ordered_on_date")
    String orderedOnDate;

    @ColumnInfo(name = "payment_mode")
    String paymentMode;

    int quantity;

    @ColumnInfo(name = "is_delivered")
    boolean isDelivered = false;

    public Integer getMailID() {
        return mailId;
    }

    public void setMailID(Integer mailId) {
        this.mailId = mailId;
    }

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
