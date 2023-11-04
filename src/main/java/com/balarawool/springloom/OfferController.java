package com.balarawool.springloom;

import com.balarawool.springloom.domain.CustomerUtil;
import com.balarawool.springloom.domain.CustomerUtil.CustomerDetails;
import com.balarawool.springloom.domain.CustomerUtil.Offer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@RestController
public class OfferController {

    @GetMapping("/offer")
    public Offer getOffer() {
        var future1 = CompletableFuture.supplyAsync(CustomerUtil::getCurrentCustomer);
        var future2 = future1.thenApplyAsync(CustomerUtil::getSavingsData);
        var future3 = future1.thenApplyAsync(CustomerUtil::getLoansData);

        var customer = future1
                .exceptionally(th -> {
                    throw new RuntimeException(th);
                })
                .join();
        var future = future2
                .thenCombine(future3, ((savings, loans) -> new CustomerDetails(customer, savings, loans)))
                .thenApplyAsync(CustomerUtil::calculateOffer)
                .exceptionally(th -> {
                    throw new RuntimeException(th);
                });

        var offer = future.join();
        return offer;
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
            var details = new CustomerDetails(customer, savings, loans);

            var offer = CustomerUtil.calculateOffer(details);
            return offer;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
