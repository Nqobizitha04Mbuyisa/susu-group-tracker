package com.susu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SusuService {

    private final BigDecimal weeklyContribution;
    private final List<String> members = new ArrayList<>();
    private final Map<String, BigDecimal> payments = new LinkedHashMap<>();
    private final List<String> payoutHistory = new ArrayList<>();

    private int currentWeek = 1;
    private int nextRecipientIndex = 0;
    private boolean weekStarted = false;

    public SusuService(BigDecimal weeklyContribution) {
        Objects.requireNonNull(weeklyContribution, "Contribution is required");

        if (weeklyContribution.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Weekly contribution must be positive.");
        }

        this.weeklyContribution = weeklyContribution;
    }

    public void addMember(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name is required.");
        }

        String cleanName = name.trim();

        boolean exists = members.stream()
                .anyMatch(member -> member.equalsIgnoreCase(cleanName));

        if (exists) {
            throw new IllegalArgumentException("Member already exists.");
        }

        if (weekStarted || currentWeek > 1) {
            throw new IllegalStateException(
                    "Members cannot be added after a week has started.");
        }

        members.add(cleanName);
        payments.put(cleanName, BigDecimal.ZERO);
    }

    public void recordContribution(String memberName, BigDecimal amount) {
        String member = findMember(memberName);

        Objects.requireNonNull(amount, "Payment amount is required");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Payment must be greater than zero.");
        }

        BigDecimal alreadyPaid = payments.get(member);
        BigDecimal remaining = weeklyContribution.subtract(alreadyPaid);

        if (amount.compareTo(remaining) > 0) {
            throw new IllegalArgumentException(
                    "Payment exceeds the amount owed: " + remaining);
        }

        payments.put(member, alreadyPaid.add(amount));
        weekStarted = true;
    }

    public String closeWeek() {
        if (members.isEmpty()) {
            throw new IllegalStateException("Add members before closing a week.");
        }

        List<String> outstanding = new ArrayList<>();

        for (String member : members) {
            BigDecimal owed = weeklyContribution.subtract(payments.get(member));

            if (owed.compareTo(BigDecimal.ZERO) > 0) {
                outstanding.add(member + " owes " + owed.toPlainString());
            }
        }

        if (!outstanding.isEmpty()) {
            return "Week " + currentWeek + " cannot close. "
                    + String.join("; ", outstanding);
        }

        BigDecimal pot = weeklyContribution.multiply(
                BigDecimal.valueOf(members.size()));

        String recipient = members.get(nextRecipientIndex);

        payoutHistory.add("Week " + currentWeek + ": "
                + recipient + " received " + pot.toPlainString());

        currentWeek++;
        nextRecipientIndex = (nextRecipientIndex + 1) % members.size();
        payments.replaceAll((member, amount) -> BigDecimal.ZERO);
        weekStarted = false;

        return "Week closed successfully. " + recipient
                + " received " + pot.toPlainString();
    }

    public String getReport() {
        StringBuilder report = new StringBuilder();
        report.append("Current week: ").append(currentWeek).append("\n");
        report.append("Weekly contribution: ")
                .append(weeklyContribution.toPlainString()).append("\n");

        report.append("\nMember payments:\n");

        for (String member : members) {
            BigDecimal paid = payments.get(member);
            BigDecimal owed = weeklyContribution.subtract(paid);

            report.append("- ").append(member)
                    .append(": paid ").append(paid.toPlainString());

            if (owed.compareTo(BigDecimal.ZERO) == 0) {
                report.append(" (paid in full)");
            } else {
                report.append(", owes ").append(owed.toPlainString());
            }

            report.append("\n");
        }

        report.append("\nPayout history:\n");

        if (payoutHistory.isEmpty()) {
            report.append("No payouts yet.\n");
        } else {
            payoutHistory.forEach(entry -> report.append("- ")
                    .append(entry).append("\n"));
        }

        return report.toString();
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public List<String> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<String> getPayoutHistory() {
        return Collections.unmodifiableList(payoutHistory);
    }

    private String findMember(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Member name is required.");
        }

        return members.stream()
                .filter(member -> member.equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown member: " + name));
    }
}

