
package com.susu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private SusuService service;

    private BigDecimal money(String amount) {
        return new BigDecimal(amount);
    }

    @BeforeEach
    void setUp() {
        service = new SusuService(money("100.00"));
        service.addMember("Alice");
        service.addMember("Bob");
    }

    @Test
    void shouldAcceptPartialContribution() {
        service.recordContribution("Alice", money("40.00"));

        assertTrue(service.getReport().contains("paid 40.00"));
        assertTrue(service.getReport().contains("owes 60.00"));
    }

    @Test
    void shouldRejectOverpayment() {
        assertThrows(IllegalArgumentException.class,
                () -> service.recordContribution("Alice", money("101.00")));
    }

    @Test
    void shouldRejectDuplicateMemberIgnoringCase() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addMember("alice"));
    }

    @Test
    void shouldRejectUnknownMember() {
        assertThrows(IllegalArgumentException.class,
                () -> service.recordContribution("Charlie", money("50.00")));
    }

    @Test
    void shouldNotCloseWeekWhenMembersStillOweMoney() {
        service.recordContribution("Alice", money("100.00"));

        String result = service.closeWeek();

        assertTrue(result.contains("cannot close"));
        assertEquals(1, service.getCurrentWeek());
        assertTrue(service.getPayoutHistory().isEmpty());
        assertTrue(result.contains("Bob owes 100.00"));
    }

    @Test
    void shouldCloseWeekWhenEveryoneHasPaidInFull() {
        service.recordContribution("Alice", money("100.00"));
        service.recordContribution("Bob", money("100.00"));

        String result = service.closeWeek();

        assertTrue(result.contains("Alice received 200.00"));
        assertEquals(2, service.getCurrentWeek());
        assertEquals(1, service.getPayoutHistory().size());
    }

    @Test
    void shouldRotatePayoutRecipientEachWeek() {
        service.recordContribution("Alice", money("100.00"));
        service.recordContribution("Bob", money("100.00"));
        service.closeWeek();

        service.recordContribution("Alice", money("100.00"));
        service.recordContribution("Bob", money("100.00"));
        service.closeWeek();

        assertTrue(service.getPayoutHistory().get(0)
                .contains("Alice received 200.00"));
        assertTrue(service.getPayoutHistory().get(1)
                .contains("Bob received 200.00"));
        assertEquals(3, service.getCurrentWeek());
    }

    @Test
    void shouldRejectAddingMembersAfterContributionsStart() {
        service.recordContribution("Alice", money("25.00"));

        assertThrows(IllegalStateException.class,
                () -> service.addMember("Charlie"));
    }

    @Test
    void shouldRejectZeroContribution() {
        assertThrows(IllegalArgumentException.class,
                () -> service.recordContribution("Alice", money("0.00")));
    }
    @Test
    void shouldRejectAddingMembersAfterFirstWeekCloses() {
    service.recordContribution("Alice", money("100.00"));
    service.recordContribution("Bob", money("100.00"));
    service.closeWeek();

    assertThrows(IllegalStateException.class,
            () -> service.addMember("Charlie"));
}
}
