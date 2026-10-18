package com.example.firstspringprjct.Controller;


import com.example.firstspringprjct.DTO.PaymentRequest;
import com.example.firstspringprjct.DTO.PaymentResponse;
import com.example.firstspringprjct.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id)
    {
        // map incoming request to payment request DTO
        PaymentRequest requestobj = new PaymentRequest();
        requestobj.setPaymentid(id);

        PaymentResponse payment = paymentService.getPaymentDetailsById(requestobj);
        return ResponseEntity.ok(payment);

    }
}
