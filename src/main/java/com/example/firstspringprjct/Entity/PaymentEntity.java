package com.example.firstspringprjct.Entity;

public class PaymentEntity {
    /*
    *
    * */
    private Long id;
    private double paymentAmount;
    private String paymentCurrency;
    private String userEmail;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public double getPaymentAmount()
    {
        return paymentAmount;
    }

    public void setPaymentAmount(double amount)
    {
        this.paymentAmount = amount;
    }

    public String getPaymentCurrency()
    {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String currency)
    {
        this.paymentCurrency = currency;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String mail)
    {
        this.userEmail = mail;
    }


}
