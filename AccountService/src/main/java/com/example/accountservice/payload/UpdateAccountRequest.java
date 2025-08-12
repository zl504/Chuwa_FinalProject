package com.example.accountservice.payload;

import jakarta.validation.constraints.Size;

public class UpdateAccountRequest {
    @Size(max = 255)
    private String shippingAddress;

    @Size(max = 255)
    private String billingAddress;

    @Size(max = 100)
    private String paymentMethod;

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
