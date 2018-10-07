package com.acme.process

import com.acme.model.CreditCard
import com.acme.model.CreditCardStatus
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class CreditCardProcessorSpec extends Specification {

    @Subject
    CreditCardProcessor creditCardProcessor = new CreditCardProcessor()

    void cleanup() {
        creditCardProcessor.creditCardMap.clear()
    }

    @Unroll
    void "processLine returns status #cardStatus for credit card number #creditCardNumber"() {
        given: "an ADD operation line of text to process"
        String text = "Add Quincy ${creditCardNumber} \$2000"

        when: "processLine is invoked on that line"
        creditCardProcessor.processLine(text)

        then: "the credit card map contains the expected results"
        Map<String, CreditCard> resultMap = creditCardProcessor.creditCardMap
        resultMap.size() == 1
        resultMap["Quincy"].cardStatus == cardStatus

        where:
        creditCardNumber  || cardStatus
        4111111111111111L || CreditCardStatus.VALID
        1234567890123456L || CreditCardStatus.INVALID
    }

    void "charges that would raise the balance over the limit are ignored as if they were declined"() {
        given: "an ADD operation line of text to process"
        String addOperationText = "Add  Tom  4111111111111111 \$1000"

        and: "charge operations that would bring balance above the limit"
        String chargeOperation1Text = "Charge Tom \$500"
        String chargeOperation2Text = "Charge Tom \$800"

        when: "processLine is invoked on those lines"
        creditCardProcessor.processLine(addOperationText)
        creditCardProcessor.processLine(chargeOperation1Text)
        creditCardProcessor.processLine(chargeOperation2Text)

        then: "the credit card map contains the expected results"
        Map<String, CreditCard> resultMap = creditCardProcessor.creditCardMap
        resultMap.size() == 1
        resultMap["Tom"].balance == 500L
        resultMap["Tom"].cardStatus == CreditCardStatus.VALID
    }

    void "charges against Luhn 10 invalid cards are ignored"() {
        given: "an ADD operation line of text to process on an invalid card number"
        String addOperationText = "Add  Tom  1234567890123456 \$1000"

        and: "a charge operation on that card"
        String chargeOperationText = "Charge Tom \$500"

        when: "processLine is invoked on those lines"
        creditCardProcessor.processLine(addOperationText)
        creditCardProcessor.processLine(chargeOperationText)

        then: "the credit card map contains the expected results"
        Map<String, CreditCard> resultMap = creditCardProcessor.creditCardMap
        resultMap["Tom"].balance == 0L
    }

    void "credits against Luhn 10 invalid cards are ignored"() {
        given: "an ADD operation line of text to process on an invalid card number"
        String addOperationText = "Add  Tom  1234567890123456 \$1000"

        and: "a credit operation on that card"
        String creditOperationText = "Credit Tom \$500"

        when: "processLine is invoked on those lines"
        creditCardProcessor.processLine(addOperationText)
        creditCardProcessor.processLine(creditOperationText)

        then: "the credit card map contains the expected results"
        Map<String, CreditCard> resultMap = creditCardProcessor.creditCardMap
        resultMap["Tom"].balance == 0L
    }

    void "a mix of charges and credits against Luhn 10 valid cards yields the expected results"() {
        given: "add operation lines of text"
        String validAddOperation1Text = "ADD  Tom  4111111111111111  \$1000"
        String validAddOperation2Text = "Add Lisa        5454545454545454      \$3000"
        String invalidAddOperationText = "add     Quincy 1234567890123456 \$2000"

        and: "charge operation lines of text"
        String charge1 = "CHARGE Tom  \$500"
        String charge2 = "Charge    Tom    \$800"
        String charge3 = "charge Lisa    \$7"

        and: "credit operation lines of text"
        String credit1 = "CREDIT Lisa      \$100"
        String credit2 = "credit Quincy       \$200"

        when: "processLine is invoked on those lines"
        creditCardProcessor.processLine(validAddOperation1Text)
        creditCardProcessor.processLine(validAddOperation2Text)
        creditCardProcessor.processLine(invalidAddOperationText)
        creditCardProcessor.processLine(charge1)
        creditCardProcessor.processLine(charge2)
        creditCardProcessor.processLine(charge3)
        creditCardProcessor.processLine(credit1)
        creditCardProcessor.processLine(credit2)

        then: "the credit card map contains the expected results"
        Map<String, CreditCard> resultMap = creditCardProcessor.creditCardMap
        resultMap.size() == 3
        resultMap["Lisa"].balance == -93
        resultMap["Quincy"].balance == 0
        resultMap["Tom"].balance == 500
    }
}
