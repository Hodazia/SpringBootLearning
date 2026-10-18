package com.example.firstspringprjct.Services;

import com.example.firstspringprjct.DTO.PaymentRequest;
import com.example.firstspringprjct.DTO.PaymentResponse;
import com.example.firstspringprjct.Entity.PaymentEntity;
import com.example.firstspringprjct.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public PaymentResponse getPaymentDetailsById(PaymentRequest requestobj)
    {
        PaymentEntity paymentModel = paymentRepository.getPaymentById(requestobj);
        PaymentResponse paymentResponse = mapModelToResponse(paymentModel);
        return paymentResponse;
    }

    private PaymentResponse mapModelToResponse(PaymentEntity model)
    {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(model.getId());
        response.setAmount(model.getPaymentAmount());
        response.setCurrency(model.getPaymentCurrency());
        return response;
    }
}
