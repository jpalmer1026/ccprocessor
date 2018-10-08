#Basic Credit Card Processing


This program adds new credit card accounts, processes charges and credits
against them, and displays summary information about the accounts.

## Requirements

- The program must accept input from two sources: a filename passed in
command line arguments and STDIN. For example, on Linux or OSX both
'./myprogram input.txt' and './myprogram < input.txt' should work.
- The program must accept the following three input commands passed with space delimited
arguments:
  - "Add" will create a new credit card for a given name, card number, and limit
    - Card numbers should be validated using Luhn 10
    - New cards start with a $0 balance
  - "Charge" will increase the balance of the card associated with the provided
name by the amount specified
    - Charges that would raise the balance over the limit are ignored as if they
were declined
    - Charges against Luhn 10 invalid cards are ignored
  - "Credit" will decrease the balance of the card associated with the provided
name by the amount specified
    - Credits that would drop the balance below $0 will create a negative balance
    - Credits against Luhn 10 invalid cards are ignored
- When all input has been read and processed, a summary should be generated and
written to STDOUT in the format shown in the example below.
  - The summary should include the name of each person followed by a colon and
balance.
  - The names should be displayed alphabetically.
  - Display "error" instead of the balance if the credit card number does not pass
Luhn 10.

## Input Assumptions

- All input will be valid -- there's no need to check for illegal characters
or malformed commands.
- All input will be space delimited.
- Credit card numbers may vary in length up to 19 characters.
- Credit card numbers will always be numeric.
- Amounts will always be prefixed with "$" and will be in whole dollars (no
decimals).

## Input and Output:

Given the following input:

```
Add Tom 4111111111111111 $1000
Add Lisa 5454545454545454 $3000
Add Quincy 1234567890123456 $2000
Charge Tom $500
Charge Tom $800
Charge Lisa $7
Credit Lisa $100
Credit Quincy $200
```

The program must produce the following output:

```
Lisa: $-93
Quincy: error
Tom: $500
```

### Design overview

The program splits the work of reading the file, processing the file, and displaying the results of the processing into 3 separate files, adhering to the Single Responsibility Principle

I chose Kotlin, mainly since I'm still fairly new to the language, and I wanted to obtain more practice with it. The fact that it embraces immutability made it all the more appealing to me.

The program uses the Gradle build system, which can be used to compile the application, install the required dependencies, and run the code and tests.

