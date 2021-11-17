import java.nio.channels.ClosedChannelException;

class BigNumber {
    public static final BigNumber ZERO = new BigNumber("0");
    public static final BigNumber ONE = new BigNumber("1");
    public static final BigNumber TWO = new BigNumber("2");
    public static final BigNumber TEN = new BigNumber("10");

    private int[] digits = new int[20];
    private boolean isNegative;

    public BigNumber(String number) {
        if (number.charAt(0) == '-') {
            isNegative = true;
            number = number.substring(1);
        } else if (number.charAt(0) == '+') {
            number = number.substring(1);
        }

        var startIndex = Math.max(number.length() - 20, 0);
        var characters = number.substring(startIndex).toCharArray();

        for (int i = characters.length - 1; i >= 0; i--) {
            digits[(characters.length - 1) - i] = characters[i] - 48;
        }
    }

    public BigNumber increase() {
        return plus(ONE);
    }

    public BigNumber decrease() {
        return minus(ONE);
    }

    public BigNumber shiftRight() {
        return shiftRight(1);
    }

    public BigNumber shiftLeft() {
        return shiftLeft(1);
    }

    public BigNumber shiftRight(int amount) {
        var result = clone();

        for (int i = amount; i < result.digits.length; i++)
            result.digits[i - amount] = result.digits[i];

        for (int i = result.digits.length - amount; i < result.digits.length - 1; i++)
            result.digits[i] = 0;

        return result;
    }

    public BigNumber shiftLeft(int amount) {
        return new BigNumber(this + "0".repeat(amount));
    }

    public BigNumber plus(BigNumber other) {
        var result = ZERO;
        var carry = false;

        for (int i = 0; i < digits.length; i++) {
            var sum = digits[i] + other.digits[i] + (carry ? 1 : 0);

            result.digits[i] = sum % 10;
            carry = sum > 9;
        }

        return result;
    }

    public BigNumber minus(BigNumber other) {
        if (this.isNegative && !other.isNegative) {
            var cloned = this.clone();
            cloned.isNegative = false;

            var result = cloned.plus(other);
            result.isNegative = true;
            return result;
        }

        if (!this.isNegative && other.isNegative) {
            var cloned = other.clone();
            cloned.isNegative = false;

            return this.plus(cloned);
        }

        if (this.isNegative) {
            var leftCloned = this.clone();
            leftCloned.isNegative = false;

            var rightCloned = other.clone();
            rightCloned.isNegative = false;

            return rightCloned.minus(leftCloned);
        }

        var comparison = this.toStringWithZeros().compareTo(other.toStringWithZeros());

        if (comparison == 0) return ZERO;

        if (comparison < 0) {
            var result = other.minus(this);
            result.isNegative = !result.isNegative;
            return result;
        }

        var result = ZERO;
        for (int i = 0; i < digits.length; i++) {
            var minus = digits[i] - other.digits[i];

            if (minus < 0) {
                minus += 10;
                digits[i + 1]--;
            }

            result.digits[i] = minus;
        }

        return result;
    }

//    public BigNumber times(BigNumber other) {
//        // TODO: your code ...
//    }
//
//    public BigNumber toThePowerOf(BigNumber other) {
//        // TODO: your code ...
//    }

    public BigNumber clone() {
        return new BigNumber(toString());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(toStringWithZeros());
        result = new StringBuilder(result.toString().replace("0", " "));
        result.append("X");
        result = new StringBuilder(result.toString().trim());
        result = new StringBuilder(result.toString().replace(" ", "0"));

        var absolute = result.length() == 1 ? "0" : result.substring(0, result.length() - 1);
        return (isNegative ? "-" : "") + absolute;
    }

    private String toStringWithZeros() {
        StringBuilder result = new StringBuilder();
        for (var digit : digits)
            result.append(digit);

        result.reverse();
        return result.toString();
    }
}
