
# Susu Group Tracker

A Java command-line application for managing a rotating savings group. Members contribute a fixed amount each week, and the full pot is paid to the next member in joining order once everyone has paid in full.

## Features

- Create a group with a fixed weekly contribution.
- Add members with unique names.
- Record full or partial contributions.
- Reject invalid payments, overpayments, duplicate members, and unknown members.
- Prevent a week from closing while contributions are outstanding.
- Rotate payouts in joining order.
- Display current-week payments, outstanding balances, and payout history.
- Use `BigDecimal` for monetary calculations.
- Run automated tests with JUnit 5.

## Requirements

- Java 21 or compatible JDK
- Apache Maven

## Run the application

Clone the repository and open its directory:

```bash
git clone https://github.com/Nqobizitha04Mbuyisa/susu-group-tracker.git
cd susu-group-tracker
```

Run the automated tests:

```bash
mvn clean test
```

Start the application:

```bash
mvn exec:java "-Dexec.mainClass=com.susu.App"
```

## How to use it

1. Enter the fixed weekly contribution when prompted.
2. Use the menu to add group members.
3. Record each member's contributions. Partial payments are allowed.
4. Close the week once every member has paid in full.
5. View the report to check balances and payout history.
6. Choose `0` to exit.

The first member added receives the first payout. Each successful weekly payout advances to the next member in joining order.

## Project structure

```text
src/
  main/java/com/susu/
    App.java
    SusuService.java
  test/java/com/susu/
    AppTest.java
pom.xml
README.md
NOTES.md
```

## Testing

Run `mvn clean test` to execute the automated test suite. Tests cover partial payments, invalid payments, duplicate and unknown members, week closure, payout rotation, and member-registration restrictions.

## Assumptions and limitations

See [NOTES.md](NOTES.md) for implementation assumptions and known limitations.
