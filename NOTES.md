
# Implementation Notes

## Assumptions

- Every member is expected to contribute the same fixed amount each week.
- Members are identified by their names. Names are trimmed and compared without regard to letter case.
- Partial contributions are allowed, but a member cannot pay more than the amount still owed.
- A week can close only when all registered members have paid their full contribution.
- The entire weekly pot is paid to one member in joining order.
- The first member registered receives the first payout.
- The application uses `BigDecimal` to avoid floating-point inaccuracies in monetary calculations.
- Group data is stored in memory and is not retained after the application exits.

## Validation

The service validates the weekly contribution, member names, duplicate registrations, unknown members, and payment amounts. It reports outstanding balances when a week cannot close.

## Known limitations and possible improvements

- Member names are used as identifiers; separate member IDs could be introduced.
- The application does not persist group data to a database or file.
- The current command-line interface supports one group per application run.
- Monetary amounts are entered as decimal values; currency display and currency-specific validation could be improved.
- Future enhancements could include persistent storage, transaction receipts, and more detailed reporting.
