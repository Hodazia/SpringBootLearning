package com.example.firstspringprjct.Repository;

import com.example.firstspringprjct.DTO.PaymentRequest;
import com.example.firstspringprjct.Entity.PaymentEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository {

    public PaymentEntity getPaymentById(PaymentRequest request)
    {
        PaymentEntity paymentModel = executeQuery(request);
        return paymentModel;
    }

    private PaymentEntity executeQuery(PaymentRequest obj)
    {
        PaymentEntity payment = new PaymentEntity();
        payment.setId(obj.getPaymentId());
        payment.setPaymentAmount(100.0);
        payment.setUserEmail("zia23hoda@gmail.com");
        payment.setPaymentCurrency("INR");
        return payment;
    }
}
