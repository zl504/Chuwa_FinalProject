package com.example.accountservice.payload;

public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;

    public UserResponse() {}

    public UserResponse(Long id, String email, String username,
                        String shippingAddress, String billingAddress, String paymentMethod) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
