package com.balarawool.simpleapp;

import com.balarawool.simpleapp.domain.CustomerUtil;
import com.balarawool.simpleapp.domain.CustomerUtil.CustomerDetails;
import com.balarawool.simpleapp.domain.CustomerUtil.Offer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@RestController
public class OfferController {

    @GetMapping("/offer")
    public Offer getOffer() {
        var customer = CustomerUtil.getCurrentCustomer();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var task1 = scope.fork(() -> CustomerUtil.getSavingsData(customer));
            var task2 = scope.fork(() -> CustomerUtil.getLoansData(customer));

            scope.join().throwIfFailed();

            var savings = task1.get();
            var loans = task2.get();
            var details = new CustomerUtil.CustomerDetails(customer, savings, loans);

            var offer = CustomerUtil.calculateOffer(details);
            return offer;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/offer2")
    public Offer getOffer2() {
        var customer = CustomerUtil.getCurrentCustomer();
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var task1 = scope.fork(() -> CustomerUtil.getSavingsData(customer));
            var task2 = scope.fork(() -> CustomerUtil.getLoansData(customer));

            scope.join().throwIfFailed();

            var savings = task1.get();
            var loans = task2.get();
            var details = new CustomerUtil.CustomerDetails(customer, savings, loans);

            var offer = CustomerUtil.calculateOffer(details);
            return offer;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
