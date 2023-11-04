package com.balarawool.simpleapp.domain;

import static com.balarawool.simpleapp.domain.ThreadUtil.logAndWait;

public class CustomerUtil {

    public static Customer getCurrentCustomer() {
        logAndWait("getCurrentCustomer");
        return new Customer();
    }

    public static Savings getSavingsData(Customer customer) {
        logAndWait("getSavingsData");
        return new Savings(customer);
    }

    public static Loans getLoansData(Customer customer) {
        logAndWait("getLoansData");
        return new Loans(customer);
    }

    public static Offer calculateOffer(CustomerDetails customerDetails) {
        logAndWait("calculateOffer");
        return new Offer(getRandomInterestRate(), customerDetails);
    }

    private static double getRandomInterestRate() {
        int t = (int) (1 + Math.random() * 500);
        return t / 100d;
    }

    public record Customer() {}
    public record Savings(Customer customer) {}
    public record Loans(Customer customer) {}
    public record CustomerDetails(Customer customer, Savings savings, Loans loans) {}
    public record Offer(double interestRate, CustomerDetails customerDetails) {}
}
