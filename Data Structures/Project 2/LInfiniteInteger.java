//WILLIAM KUREK
//PROJECT 2
//CS 445 TUES/THURS 230-345
public class LInfiniteInteger implements InfiniteIntegerInterface {

    private Node firstNode;
    private Node lastNode;
    private int numberOfDigits;
    private boolean isNegative;

    /**
     * Constructor: Constructs this infinite integer from a string representing
     * an integer.
     *
     * @param s a string represents an integer
     */
    public LInfiniteInteger(String s) {
        if (s.matches("^[0]+$")) {
            firstNode = new Node(Integer.parseInt(s));
            lastNode = firstNode;
            numberOfDigits++;
            return;
        }
        if (s.contains("-")) {
            this.isNegative = true;
            s = s.replace("-", "");
        } else {
            this.isNegative = false;
        }
        int index = 0;
        int zero = Integer.parseInt(String.valueOf(s.charAt(index)));
        if (zero == 0) {
            index++;
            while (true) {
                zero = Integer.parseInt(String.valueOf(s.charAt(index)));
                if (zero == 0) {
                    index++;
                } else {
                    break;
                }
            }
        }
        int strLength = s.length();

        for (int i = index; i < strLength; ++i) {
            int anInteger = Integer.parseInt(String.valueOf(s.charAt(i)));

            if (firstNode == null) {
                firstNode = new Node(anInteger);
                lastNode = firstNode;
                numberOfDigits++;
            } else {
                Node newNode = new Node(lastNode, anInteger, null);
                lastNode.next = newNode;
                lastNode = newNode;
                numberOfDigits++;
            }
        }
    }

    /**
     * Constructor: Constructs this infinite integer from an integer.
     *
     * @param anInteger an integer
     */
    public LInfiniteInteger(int anInteger) {
        if (anInteger < 0) {
            isNegative = true;
        }
        if (anInteger >= 0) {
            isNegative = false;
        }
        String s = Integer.toString(anInteger);
        if (Integer.parseInt(s) == 0) {
            firstNode = new Node(Integer.parseInt(s));
            lastNode = firstNode;
            numberOfDigits++;
            return;
        }
        if (Integer.parseInt(s) >= 0) {
            isNegative = false;
        }
        if (Integer.parseInt(s) < 0) {
            this.isNegative = true;
            s = s.replace("-", "");
        }
        int index = 0;
        int zero = Integer.parseInt(String.valueOf(s.charAt(index)));

        if (zero == 0) {
            index++;
            while (true) {
                zero = Integer.parseInt(String.valueOf(s.charAt(index)));
                if (zero == 0) {
                    index++;
                } else {
                    break;
                }
            }

        }
        int strLength = s.length();
        for (int i = index; i < strLength; ++i) {
            int theInteger = Integer.parseInt(String.valueOf(s.charAt(i)));
            if (firstNode == null) {
                firstNode = new Node(theInteger);
                lastNode = firstNode;
                numberOfDigits++;
            } else {
                Node newNode = new Node(lastNode, theInteger, null);
                lastNode.next = newNode;
                lastNode = newNode;
                numberOfDigits++;
            }
        }

    }

    /**
     * Gets the number of digits of this infinite integer.
     *
     * @return an integer representing the number of digits of this infinite
     * integer.
     */
    public int getNumberOfDigits() {
        return this.numberOfDigits;
    }

    /**
     * Checks whether this infinite integer is a negative number.
     *
     * @return true if this infinite integer is a negative number. Otherwise,
     * return false.
     */
    public boolean isNegative() {
        return this.isNegative;
    }

    /**
     * Calculates the result of this infinite integer plus anInfiniteInteger
     *
     * @param anInfiniteInteger the infinite integer to be added to this
     * infinite integer.
     * @return a NEW infinite integer representing the result of this infinite
     * integer plus anInfiniteInteger
     */
    public InfiniteIntegerInterface plus(final InfiniteIntegerInterface anInfiniteInteger) {
        LInfiniteInteger operand1 = new LInfiniteInteger(this.toString());
        LInfiniteInteger operand2 = new LInfiniteInteger(anInfiniteInteger.toString());
        LInfiniteInteger result = null;
        Node current1 = operand1.lastNode;
        Node current2 = operand2.lastNode;
        if (operand1.isNegative() == false && operand2.isNegative() == false) {
            if (current1 == null && current2 == null) {
                return null;
            }
            int carry = 0;
            int count = 0;
            result = new LInfiniteInteger(0);
            Node head = result.firstNode;
            while (current1 != null || current2 != null) {
                if (current1 != null) {
                    carry += current1.data;
                    current1 = current1.previous;
                }
                if (current2 != null) {
                    carry += current2.data;
                    current2 = current2.previous;
                }
                int d3 = carry % 10;
                carry /= 10;
                if (count == 0) {
                    head.data = d3;
                    result.firstNode = head;
                    count++;
                } else {
                    Node newNode = new Node(head, d3, null);
                    head.next = newNode;
                    head = newNode;
                    numberOfDigits++;
                }
            }
            if (carry == 1) {
                Node newNode = new Node(head, 1, null);
                head.next = newNode;
                head = newNode;
                numberOfDigits++;
            }
            result.lastNode = head;
            LInfiniteInteger reversed = new LInfiniteInteger(0);
            reversed.firstNode.data = head.data;
            head = head.previous;
            Node reversedHead = reversed.firstNode;
            while (head != null) {
                Node newNode = new Node(reversedHead, head.data, null);
                reversedHead.next = newNode;
                reversedHead = newNode;
                head = head.previous;
                reversed.numberOfDigits++;

            }
            return reversed;
        }
        if (operand1.isNegative() == false && operand2.isNegative() == true) {
            if (operand1.numberOfDigits < operand2.numberOfDigits) {
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                }
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = true;
                }
            } else if (operand1.numberOfDigits > operand2.numberOfDigits) {
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    current1 = operand1.lastNode;
                    current2 = operand2.lastNode;
                    operand1.isNegative = true;
                    operand2.isNegative = false;
                }
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    current1 = operand1.lastNode;
                    current2 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                }
                if (operand1.isNegative == true && operand2.isNegative == true) {
                    current1 = operand1.lastNode;
                    current2 = operand2.lastNode;
                    operand1.isNegative = true;
                    operand2.isNegative = true;
                }
            } else if (operand1.numberOfDigits == operand2.numberOfDigits) {
                if (operand1.firstNode.data < operand2.firstNode.data) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    if (operand1.isNegative == true && operand2.isNegative == false) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                    } else if (operand1.isNegative == false && operand2.isNegative == true) {
                        operand1.isNegative = false;
                        operand2.isNegative = true;
                    }
                } else {
                    if (operand1.isNegative == false && operand2.isNegative == true) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                        int compare = operand1.compareTo(operand2);
                        if (compare == 1) {
                            current1 = operand1.lastNode;
                            current2 = operand2.lastNode;
                            operand1.isNegative = false;
                            operand2.isNegative = false;
                        }
                        if (compare == -1) {
                            current2 = operand1.lastNode;
                            current1 = operand2.lastNode;
                            operand1.isNegative = false;
                            operand2.isNegative = true;
                        }
                    }
                    if (operand1.isNegative == true && operand2.isNegative == false) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                        int compare = operand1.compareTo(operand2);
                        if (compare == 1) {
                            current1 = operand1.lastNode;
                            current2 = operand2.lastNode;
                            operand1.isNegative = true;
                            operand2.isNegative = false;
                        }
                        if (compare == -1) {
                            current2 = operand1.lastNode;
                            current1 = operand2.lastNode;
                            operand1.isNegative = true;
                            operand1.isNegative = false;
                        }
                    }
                    if (operand1.isNegative == true && operand2.isNegative == true) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                        int compare = operand1.compareTo(operand2);
                        if (compare == 1) {
                            current1 = operand1.lastNode;
                            current2 = operand2.lastNode;
                            operand1.isNegative = true;
                            operand2.isNegative = true;
                        }
                        if (compare == -1) {
                            current2 = operand1.lastNode;
                            current1 = operand2.lastNode;
                            operand1.isNegative = false;
                            operand1.isNegative = false;
                        }
                    }
                }
            } else {
                current1 = operand1.lastNode;
                current2 = operand2.lastNode;
            }
            if (operand1.isNegative == false && operand2.isNegative == false) {
                result = new LInfiniteInteger(0);
                int carry = 0;
                int count = 0;
                Node resultHead = result.firstNode;
                while (current1 != null || current2 != null) {
                    int newDigit;
                    int digitTwo = 0;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        digitTwo = current2.data;
                    }
                    if (current1.data < digitTwo) {
                        newDigit = (current1.data + 10) - digitTwo;
                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        }
                    } else {
                        newDigit = current1.data - digitTwo;
                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                        }
                    }
                    current1 = current1.previous;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        current2 = current2.previous;
                    }
                }
                result.lastNode = resultHead;
                LInfiniteInteger reversed = new LInfiniteInteger(0);
                reversed.firstNode.data = resultHead.data;
                resultHead = resultHead.previous;
                Node reversedHead = reversed.firstNode;
                while (resultHead != null) {
                    Node newNode = new Node(reversedHead, resultHead.data, null);
                    reversedHead.next = newNode;
                    reversedHead = newNode;
                    resultHead = resultHead.previous;
                    reversed.numberOfDigits++;
                }
                LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
                noZero.isNegative = false;
                if (noZero.firstNode.data == 0) {
                    noZero.isNegative = false;
                }

                return noZero;
            }
            if (operand1.isNegative == false && operand2.isNegative == true) {
                result = new LInfiniteInteger(0);
                int carry = 0;
                int count = 0;

                Node resultHead = result.firstNode;
                while (current1 != null || current2 != null) {

                    int newDigit;
                    int digitTwo = 0;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        digitTwo = current2.data;
                    }
                    if (current1.data < digitTwo) {

                        newDigit = (current1.data + 10) - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;

                        }
                    } else {
                        newDigit = current1.data - digitTwo;
                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                        }
                    }

                    current1 = current1.previous;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        current2 = current2.previous;
                    }
                }
                result.lastNode = resultHead;
                LInfiniteInteger reversed = new LInfiniteInteger(0);
                reversed.firstNode.data = resultHead.data;
                resultHead = resultHead.previous;
                Node reversedHead = reversed.firstNode;
                while (resultHead != null) {
                    Node newNode = new Node(reversedHead, resultHead.data, null);
                    reversedHead.next = newNode;
                    reversedHead = newNode;
                    resultHead = resultHead.previous;
                    reversed.numberOfDigits++;
                }
                LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
                noZero.isNegative = true;
                if (noZero.firstNode.data == 0) {
                    noZero.isNegative = false;
                }
                return noZero;
            }
            if (operand1.isNegative == true && operand2.isNegative == false) {
                result = new LInfiniteInteger(0);
                int carry = 0;
                int count = 0;
                Node resultHead = result.firstNode;
                while (current1 != null || current2 != null) {

                    int newDigit;
                    int digitTwo = 0;

                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        digitTwo = current2.data;
                    }
                    if (current1.data < digitTwo) {
                        newDigit = (current1.data + 10) - digitTwo;
                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        }
                    } else {

                        newDigit = current1.data - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                        }
                    }
                    current1 = current1.previous;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        current2 = current2.previous;
                    }
                }
                result.lastNode = resultHead;
                LInfiniteInteger reversed = new LInfiniteInteger(0);
                reversed.firstNode.data = resultHead.data;
                resultHead = resultHead.previous;
                Node reversedHead = reversed.firstNode;
                while (resultHead != null) {
                    Node newNode = new Node(reversedHead, resultHead.data, null);
                    reversedHead.next = newNode;
                    reversedHead = newNode;
                    resultHead = resultHead.previous;
                    reversed.numberOfDigits++;
                }
                LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
                noZero.isNegative = true;
                if (noZero.firstNode.data == 0) {
                    noZero.isNegative = false;
                }
                return noZero;
            }
        }
        if (operand1.isNegative() == true && operand2.isNegative() == false) {
            if (operand1.numberOfDigits < operand2.numberOfDigits) {
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                }
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = true;
                }
            } else if (operand1.numberOfDigits > operand2.numberOfDigits) {
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    current1 = operand1.lastNode;
                    current2 = operand2.lastNode;
                    operand1.isNegative = true;
                    operand2.isNegative = false;
                }
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    current1 = operand1.lastNode;
                    current2 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                }
                if (operand1.isNegative == true && operand2.isNegative == true) {
                    current1 = operand1.lastNode;
                    current2 = operand2.lastNode;
                    operand1.isNegative = true;
                    operand2.isNegative = true;
                }
            } else if (operand1.numberOfDigits == operand2.numberOfDigits) {
                if (operand1.firstNode.data < operand2.firstNode.data) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    if (operand1.isNegative == true && operand2.isNegative == false) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                    } else if (operand1.isNegative == false && operand2.isNegative == true) {
                        operand1.isNegative = false;
                        operand2.isNegative = true;
                    }
                } else {
                    if (operand1.isNegative == false && operand2.isNegative == true) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                        int compare = operand1.compareTo(operand2);
                        if (compare == 1) {
                            current1 = operand1.lastNode;
                            current2 = operand2.lastNode;
                            operand1.isNegative = false;
                            operand2.isNegative = false;
                        }
                        if (compare == -1) {
                            current2 = operand1.lastNode;
                            current1 = operand2.lastNode;
                            operand1.isNegative = false;
                            operand2.isNegative = true;
                        }
                    }
                    if (operand1.isNegative == true && operand2.isNegative == false) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                        int compare = operand1.compareTo(operand2);
                        if (compare == 1) {
                            current1 = operand1.lastNode;
                            current2 = operand2.lastNode;
                            operand1.isNegative = true;
                            operand2.isNegative = false;
                        }
                        if (compare == -1) {
                            current2 = operand1.lastNode;
                            current1 = operand2.lastNode;
                            operand1.isNegative = true;
                            operand1.isNegative = false;
                        }
                    }
                    if (operand1.isNegative == true && operand2.isNegative == true) {
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                        int compare = operand1.compareTo(operand2);
                        if (compare == 1) {
                            current1 = operand1.lastNode;
                            current2 = operand2.lastNode;
                            operand1.isNegative = true;
                            operand2.isNegative = true;
                        }
                        if (compare == -1) {
                            current2 = operand1.lastNode;
                            current1 = operand2.lastNode;
                            operand1.isNegative = false;
                            operand1.isNegative = false;
                        }
                    }
                }
            } else {
                current1 = operand1.lastNode;
                current2 = operand2.lastNode;
            }
            if (operand1.isNegative == false && operand2.isNegative == false) {
                result = new LInfiniteInteger(0);
                int carry = 0;
                int count = 0;
                Node resultHead = result.firstNode;
                while (current1 != null || current2 != null) {

                    int newDigit;
                    int digitTwo = 0;

                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        digitTwo = current2.data;
                    }
                    if (current1.data < digitTwo) {

                        newDigit = (current1.data + 10) - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;

                        }
                    } else {
                        newDigit = current1.data - digitTwo;
                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                        }
                    }
                    current1 = current1.previous;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        current2 = current2.previous;
                    }
                }
                result.lastNode = resultHead;
                LInfiniteInteger reversed = new LInfiniteInteger(0);
                reversed.firstNode.data = resultHead.data;
                resultHead = resultHead.previous;
                Node reversedHead = reversed.firstNode;
                while (resultHead != null) {
                    Node newNode = new Node(reversedHead, resultHead.data, null);
                    reversedHead.next = newNode;
                    reversedHead = newNode;
                    resultHead = resultHead.previous;
                    reversed.numberOfDigits++;
                }

                LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
                noZero.isNegative = false;
                if (noZero.firstNode.data == 0) {
                    noZero.isNegative = false;
                }
                return noZero;
            }
            if (operand1.isNegative == false && operand2.isNegative == true) {
                result = new LInfiniteInteger(0);

                int count = 0;
                Node resultHead = result.firstNode;
                while (current1 != null || current2 != null) {
                    int newDigit;
                    int digitTwo = 0;

                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        digitTwo = current2.data;
                    }
                    if (current1.data < digitTwo) {

                        newDigit = (current1.data + 10) - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        }
                    } else {

                        newDigit = current1.data - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                        }
                    }
                    current1 = current1.previous;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        current2 = current2.previous;
                    }
                }

                result.lastNode = resultHead;
                LInfiniteInteger reversed = new LInfiniteInteger(0);
                reversed.firstNode.data = resultHead.data;
                resultHead = resultHead.previous;
                Node reversedHead = reversed.firstNode;

                while (resultHead != null) {
                    Node newNode = new Node(reversedHead, resultHead.data, null);
                    reversedHead.next = newNode;
                    reversedHead = newNode;
                    resultHead = resultHead.previous;
                    reversed.numberOfDigits++;
                }

                LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
                noZero.isNegative = true;
                if (noZero.firstNode.data == 0) {
                    noZero.isNegative = false;
                }

                return noZero;

            }
            if (operand1.isNegative == true && operand2.isNegative == false) {
                result = new LInfiniteInteger(0);

                int count = 0;

                Node resultHead = result.firstNode;
                while (current1 != null || current2 != null) {

                    int newDigit;
                    int digitTwo = 0;

                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        digitTwo = current2.data;
                    }
                    if (current1.data < digitTwo) {

                        newDigit = (current1.data + 10) - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                            if (current1.previous.data == 0) {
                                current1.previous.data = 10;
                                current1.previous.previous.data = current1.previous.previous.data - 1;
                            }
                            current1.previous.data = current1.previous.data - 1;
                        }
                    } else {

                        newDigit = current1.data - digitTwo;

                        if (count == 0) {
                            resultHead.data = newDigit;
                            result.firstNode = resultHead;
                            count++;
                        } else {
                            Node newNode = new Node(resultHead, newDigit, null);
                            resultHead.next = newNode;
                            resultHead = newNode;
                            result.numberOfDigits++;
                        }
                    }
                    current1 = current1.previous;
                    if (current2 == null) {
                        digitTwo = 0;
                    } else {
                        current2 = current2.previous;
                    }
                }

                result.lastNode = resultHead;
                LInfiniteInteger reversed = new LInfiniteInteger(0);
                reversed.firstNode.data = resultHead.data;
                resultHead = resultHead.previous;
                Node reversedHead = reversed.firstNode;

                while (resultHead != null) {
                    Node newNode = new Node(reversedHead, resultHead.data, null);
                    reversedHead.next = newNode;
                    reversedHead = newNode;
                    resultHead = resultHead.previous;
                    reversed.numberOfDigits++;
                }

                LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
                noZero.isNegative = true;
                if (noZero.firstNode.data == 0) {
                    noZero.isNegative = false;
                }

                return noZero;

            }

        }
        if (operand1.isNegative() == true && operand2.isNegative() == true) {
            if (current1 == null && current2 == null) {
                return null;
            }

            int carry = 0;
            int count = 0;

            result = new LInfiniteInteger(0);
            Node head = result.firstNode;

            while (current1 != null || current2 != null) {
                if (current1 != null) {
                    carry += current1.data;
                    current1 = current1.previous;
                }

                if (current2 != null) {
                    carry += current2.data;
                    current2 = current2.previous;
                }

                int d3 = carry % 10;
                carry /= 10;

                if (count == 0) {
                    head.data = d3;
                    result.firstNode = head;
                    count++;
                } else {
                    Node newNode = new Node(head, d3, null);
                    head.next = newNode;
                    head = newNode;
                    numberOfDigits++;
                }
            }
            if (carry == 1) {
                Node newNode = new Node(head, 1, null);
                head.next = newNode;
                head = newNode;
                numberOfDigits++;
            }

            result.lastNode = head;
            LInfiniteInteger reversed = new LInfiniteInteger(0);
            reversed.firstNode.data = head.data;
            head = head.previous;
            Node reversedHead = reversed.firstNode;

            while (head != null) {
                Node newNode = new Node(reversedHead, head.data, null);
                reversedHead.next = newNode;
                reversedHead = newNode;
                head = head.previous;
                reversed.numberOfDigits++;

            }
            reversed.isNegative = true;
            return reversed;
        }
        return anInfiniteInteger;
    }

    /**
     * Calculates the result of this infinite integer subtracted by
     * anInfiniteInteger
     *
     * @param anInfiniteInteger the infinite integer to subtract.
     * @return a NEW infinite integer representing the result of this infinite
     * integer subtracted by anInfiniteInteger
     */
    public InfiniteIntegerInterface minus(final InfiniteIntegerInterface anInfiniteInteger) {
        LInfiniteInteger operand1 = new LInfiniteInteger(this.toString());
        LInfiniteInteger operand2 = new LInfiniteInteger(anInfiniteInteger.toString());

        Node current1 = null;
        Node current2 = null;

        LInfiniteInteger result = null;
        if (operand2.firstNode.data == 0) {
            if (operand2.isNegative == false) {
                return operand1;
            }
            if (operand2.isNegative == true) {
                return operand1;
            }

        }
        if (operand1.firstNode.data == 0) {
            if (operand2.isNegative == false) {
                operand2.isNegative = true;
                return operand1.plus(operand2);

            }
            if (operand2.isNegative == true) {
                operand2.isNegative = false;
                return operand1.plus(operand2);
            }

        }
        if (operand1.numberOfDigits < operand2.numberOfDigits) {
            if (operand1.isNegative == true && operand2.isNegative == false) {
                operand2.isNegative = true;
                return operand1.plus(operand2);

            } else if (operand1.isNegative == false && operand2.isNegative == true) {
                operand2.isNegative = false;
                return operand1.plus(operand2);
            } else if (operand1.isNegative == true && operand2.isNegative == true) {
                current2 = operand1.lastNode;
                current1 = operand2.lastNode;
                operand1.isNegative = false;
                operand2.isNegative = false;
            } else if (operand1.isNegative == false && operand2.isNegative == false) {
                current2 = operand1.lastNode;
                current1 = operand2.lastNode;
                operand1.isNegative = false;
                operand2.isNegative = true;
            }
        } else if (operand1.numberOfDigits > operand2.numberOfDigits) {
            if (operand1.isNegative == true && operand2.isNegative == false) {
                operand2.isNegative = true;
                return operand1.plus(operand2);
            } else if (operand1.isNegative == false && operand2.isNegative == true) {
                operand2.isNegative = false;
                return operand1.plus(operand2);
            } else if (operand1.isNegative == true && operand2.isNegative == true) {
                current1 = operand1.lastNode;
                current2 = operand2.lastNode;
                operand1.isNegative = false;
                operand2.isNegative = true;
            } else if (operand1.isNegative == false && operand2.isNegative == false) {
                current1 = operand1.lastNode;
                current2 = operand2.lastNode;
                operand1.isNegative = false;
                operand2.isNegative = false;
            }

        } else if (operand1.numberOfDigits == operand2.numberOfDigits) {
            if (operand1.firstNode.data < operand2.firstNode.data) {
                //current2 = operand1.lastNode;
                //current1 = operand2.lastNode;
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    operand2.isNegative = true;
                    return operand2.plus(operand1);
                } else if (operand1.isNegative == false && operand2.isNegative == true) {
                    operand2.isNegative = false;
                    return operand1.plus(operand2);
                } else if (operand1.isNegative == true && operand2.isNegative == true) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                } else if (operand1.isNegative == false && operand2.isNegative == false) {
                    current2 = operand1.lastNode;
                    current1 = operand2.lastNode;
                    operand1.isNegative = false;
                    operand2.isNegative = true;
                }

            } else {
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    operand2.isNegative = false;
                    return operand1.plus(operand2);
                } else if (operand1.isNegative == true && operand2.isNegative == false) {
                    operand2.isNegative = true;
                    return operand1.plus(operand2);
                } else if (operand1.isNegative == true && operand2.isNegative == true) {
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                    int compare = operand1.compareTo(operand2);
                    if (compare == 1) {
                        current1 = operand1.lastNode;
                        current2 = operand2.lastNode;
                        operand1.isNegative = false;
                        operand2.isNegative = true;
                    } else if (compare == -1) {
                        current2 = operand1.lastNode;
                        current1 = operand2.lastNode;
                        operand1.isNegative = false;
                        operand1.isNegative = false;
                    }
                } else if (operand1.isNegative == false && operand2.isNegative == false) {
                    operand1.isNegative = false;
                    operand2.isNegative = false;
                    int compare = operand1.compareTo(operand2);
                    if (compare == 1) {
                        current1 = operand1.lastNode;
                        current2 = operand2.lastNode;
                        operand1.isNegative = false;
                        operand2.isNegative = false;
                    } else if (compare == -1) {
                        current2 = operand1.lastNode;
                        current1 = operand2.lastNode;
                        operand1.isNegative = false;
                        operand1.isNegative = true;
                    }

                }

            }
        } else {
            current1 = operand1.lastNode;
            current2 = operand2.lastNode;
        }

        if (operand1.isNegative == false && operand2.isNegative == false) {

            result = new LInfiniteInteger(0);
            int count = 0;

            Node resultHead = result.firstNode;
            while (current1 != null || current2 != null) {

                int newDigit;
                int digitTwo = 0;

                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    digitTwo = current2.data;
                }
                if (current1.data < digitTwo) {

                    newDigit = (current1.data + 10) - digitTwo;
                    if (count == 0) {
                        resultHead.data = newDigit;

                        result.firstNode = resultHead;
                        count++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    }
                } else {

                    newDigit = current1.data - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                    }
                }
                current1 = current1.previous;
                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    current2 = current2.previous;
                }
            }

            result.lastNode = resultHead;
            LInfiniteInteger reversed = new LInfiniteInteger(0);
            reversed.firstNode.data = resultHead.data;
            resultHead = resultHead.previous;
            Node reversedHead = reversed.firstNode;

            while (resultHead != null) {
                Node newNode = new Node(reversedHead, resultHead.data, null);
                reversedHead.next = newNode;
                reversedHead = newNode;
                resultHead = resultHead.previous;
                reversed.numberOfDigits++;
            }

            LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
            noZero.isNegative = false;
            if (noZero.firstNode.data == 0) {
                noZero.isNegative = false;
            }

            return noZero;

        }

        if (operand1.isNegative == false && operand2.isNegative == true) {
            result = new LInfiniteInteger(0);
            int count = 0;

            Node resultHead = result.firstNode;
            while (current1 != null || current2 != null) {

                int newDigit;
                int digitTwo = 0;

                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    digitTwo = current2.data;
                }
                if (current1.data < digitTwo) {

                    newDigit = (current1.data + 10) - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;

                    }
                } else {

                    newDigit = current1.data - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                    }
                }
                current1 = current1.previous;
                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    current2 = current2.previous;
                }
            }

            result.lastNode = resultHead;
            LInfiniteInteger reversed = new LInfiniteInteger(0);
            reversed.firstNode.data = resultHead.data;
            resultHead = resultHead.previous;
            Node reversedHead = reversed.firstNode;

            while (resultHead != null) {
                Node newNode = new Node(reversedHead, resultHead.data, null);
                reversedHead.next = newNode;
                reversedHead = newNode;
                resultHead = resultHead.previous;
                reversed.numberOfDigits++;
            }

            LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
            noZero.isNegative = true;
            if (noZero.firstNode.data == 0) {
                noZero.isNegative = false;
            }

            return noZero;

        }
        if (operand1.isNegative == true && operand2.isNegative == false) {
            result = new LInfiniteInteger(0);
            int count = 0;

            Node resultHead = result.firstNode;
            while (current1 != null || current2 != null) {

                int newDigit;
                int digitTwo = 0;

                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    digitTwo = current2.data;
                }
                if (current1.data < digitTwo) {

                    newDigit = (current1.data + 10) - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    }
                } else {

                    newDigit = current1.data - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                    }
                }
                current1 = current1.previous;
                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    current2 = current2.previous;
                }
            }

            result.lastNode = resultHead;
            LInfiniteInteger reversed = new LInfiniteInteger(0);
            reversed.firstNode.data = resultHead.data;
            resultHead = resultHead.previous;
            Node reversedHead = reversed.firstNode;

            while (resultHead != null) {
                Node newNode = new Node(reversedHead, resultHead.data, null);
                reversedHead.next = newNode;
                reversedHead = newNode;
                resultHead = resultHead.previous;
                reversed.numberOfDigits++;
            }

            LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
            noZero.isNegative = true;
            if (noZero.firstNode.data == 0) {
                noZero.isNegative = false;
            }

            return noZero;

        }
        if (operand1.isNegative == true && operand2.isNegative == true) {
            result = new LInfiniteInteger(0);
            int count = 0;

            Node resultHead = result.firstNode;
            while (current1 != null || current2 != null) {

                int newDigit;
                int digitTwo = 0;

                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    digitTwo = current2.data;
                }
                if (current1.data < digitTwo) {

                    newDigit = (current1.data + 10) - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                        if (current1.previous.data == 0) {
                            current1.previous.data = 10;
                            current1.previous.previous.data = current1.previous.previous.data - 1;
                        }
                        current1.previous.data = current1.previous.data - 1;
                    }
                } else {

                    newDigit = current1.data - digitTwo;

                    if (count == 0) {
                        resultHead.data = newDigit;
                        result.firstNode = resultHead;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead, newDigit, null);
                        resultHead.next = newNode;
                        resultHead = newNode;
                        result.numberOfDigits++;
                    }
                }
                current1 = current1.previous;
                if (current2 == null) {
                    digitTwo = 0;
                } else {
                    current2 = current2.previous;
                }
            }

            result.lastNode = resultHead;
            LInfiniteInteger reversed = new LInfiniteInteger(0);
            reversed.firstNode.data = resultHead.data;
            resultHead = resultHead.previous;
            Node reversedHead = reversed.firstNode;

            while (resultHead != null) {
                Node newNode = new Node(reversedHead, resultHead.data, null);
                reversedHead.next = newNode;
                reversedHead = newNode;
                resultHead = resultHead.previous;
                reversed.numberOfDigits++;
            }

            LInfiniteInteger noZero = new LInfiniteInteger(reversed.toString());
            noZero.isNegative = true;
            if (noZero.firstNode.data == 0) {
                noZero.isNegative = false;
            }

            return noZero;

        }
        return anInfiniteInteger;
    }

    /**
     * Generates a string representing this infinite integer. If this infinite
     * integer is a negative number a minus symbol should be in the front of
     * numbers. For example, "-12345678901234567890". But if the infinite
     * integer is a positive number, no symbol should be in the front of the
     * numbers (e.g., "12345678901234567890").
     *
     * @return a string representing this infinite integer number.
     */
    public String toString() {
        String result = "";
        Node current = this.firstNode;
        if (current.data == 0 && current.next == null) {
            return "0";
        }

        if (isNegative() == true) {
            result += "-";
        }
        while (current != null) {
            result += current.data;
            current = current.next;
        }
        return result;
    }

    /**
     * Compares this infinite integer with anInfiniteInteger
     *
     * @return either -1, 0, or 1 as follows: If this infinite integer is less
     * than anInfiniteInteger, return -1. If this infinite integer is equal to
     * anInfiniteInteger, return 0. If this infinite integer is greater than
     * anInfiniteInteger, return 1.
     */
    public int compareTo(final InfiniteIntegerInterface anInfiniteInteger) {
        LInfiniteInteger operand1 = new LInfiniteInteger(this.toString());
        LInfiniteInteger operand2 = new LInfiniteInteger(anInfiniteInteger.toString());
        if (operand1.isNegative == true && operand2.isNegative == false) {
            return -1;
        }

        if (operand1.isNegative == false && operand2.isNegative == true) {
            return 1;
        }

        if (operand1.numberOfDigits == operand2.numberOfDigits) {
            Node head1 = operand1.firstNode;
            Node head2 = operand2.firstNode;

            while (head1.next != null && head2.next != null) {
                if (head1.data == head2.data) {
                    head1 = head1.next;
                    head2 = head2.next;
                } else {
                    break;
                }
            }

            if (operand1.isNegative == true && operand2.isNegative == true) {
                if (head1.data > head2.data) {
                    return -1;
                }
                if (head1.data < head2.data) {
                    return 1;
                }
            }

            if (operand1.isNegative == false && operand2.isNegative == false) {
                if (head1.data > head2.data) {
                    return 1;
                }
                if (head1.data < head2.data) {
                    return -1;
                }

            }
        }

        if (operand1.numberOfDigits > operand2.numberOfDigits) {
            return 1;
        }
        if (operand1.numberOfDigits < operand2.numberOfDigits) {
            return -1;
        }

        return 0;
    }

    /**
     * Calculates the result of this infinite integer multiplied by
     * anInfiniteInteger
     *
     * @param anInfiniteInteger the multiplier.
     * @return a NEW infinite integer representing the result of this infinite
     * integer multiplied by anInfiniteInteger.
     */
    public InfiniteIntegerInterface multiply(final InfiniteIntegerInterface anInfiniteInteger) {
        LInfiniteInteger operand1 = new LInfiniteInteger(this.toString());
        LInfiniteInteger operand2 = new LInfiniteInteger(anInfiniteInteger.toString());
        LInfiniteInteger result = null;
        Node current1 = operand1.lastNode;
        Node current2 = operand2.lastNode;
        Node temp = null;

        if (operand1.numberOfDigits < operand2.numberOfDigits) {
            current2 = operand1.lastNode;
            current1 = operand2.lastNode;
            temp = operand2.lastNode;
        } else {
            temp = operand1.lastNode;
        }

        if (operand1.firstNode.data == 0 || operand2.firstNode.data == 0) {
            LInfiniteInteger zero = new LInfiniteInteger(0);
            return zero;
        }
        if (operand1.numberOfDigits == 1) {
            if (operand1.firstNode.data == 1) {
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    return operand2;
                }
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    operand2.isNegative = true;
                    return operand2;
                }
                if (operand1.isNegative == false && operand2.isNegative == false) {
                    return operand2;
                }
                if (operand1.isNegative == true && operand2.isNegative == true) {
                    operand2.isNegative = false;
                    return operand2;
                }
            }
        }
        if (operand2.numberOfDigits == 1) {
            if (operand2.firstNode.data == 1) {
                if (operand1.isNegative == false && operand2.isNegative == true) {
                    operand1.isNegative = true;
                    return operand1;
                }
                if (operand1.isNegative == true && operand2.isNegative == false) {
                    return operand1;
                }
                if (operand1.isNegative == false && operand2.isNegative == false) {
                    return operand1;
                }
                if (operand1.isNegative == true && operand2.isNegative == true) {
                    operand1.isNegative = false;
                    return operand1;
                }
            }
        }

        if (operand1.numberOfDigits == 1 && operand2.numberOfDigits == 1) {
            if (operand1.isNegative == false && operand2.isNegative == true) {
                LInfiniteInteger trueResult = new LInfiniteInteger(0);
                trueResult.firstNode.data = current1.data * current2.data;
                trueResult.isNegative = true;
                return trueResult;
            }
            if (operand1.isNegative == true && operand2.isNegative == false) {
                LInfiniteInteger trueResult = new LInfiniteInteger(0);
                trueResult.firstNode.data = current1.data * current2.data;
                trueResult.isNegative = true;
                return trueResult;
            }
            if (operand1.isNegative == false && operand2.isNegative == false) {
                LInfiniteInteger trueResult = new LInfiniteInteger(0);
                trueResult.firstNode.data = current1.data * current2.data;
                trueResult.isNegative = false;
                return trueResult;
            }
            if (operand1.isNegative == true && operand2.isNegative == true) {
                operand1.isNegative = false;
                LInfiniteInteger trueResult = new LInfiniteInteger(0);
                trueResult.firstNode.data = current1.data * current2.data;
                trueResult.isNegative = false;
                return trueResult;
            }
        }

        if (this.isNegative() == true && anInfiniteInteger.isNegative() == true) {

            int zeroes = 0;
            LInfiniteInteger trueResult = new LInfiniteInteger(0);
            LInfiniteInteger reversed = null;
            while (current2 != null) {
                current1 = temp;
                int product = 0;
                int carry = 0;
                int count = 0;

                result = new LInfiniteInteger(0);
                Node resultHead1 = result.firstNode;

                if (zeroes > 0) {
                    int i = 0;
                    while (i < zeroes) {
                        if (count == 0) {
                            resultHead1.data = 0;
                            result.firstNode = resultHead1;
                            count++;
                            i++;
                        } else {
                            Node newNode = new Node(resultHead1, 0, null);
                            resultHead1.next = newNode;
                            resultHead1 = newNode;
                            result.numberOfDigits++;
                            i++;
                        }
                    }
                }

                while (current1 != null) {

                    int x = (current1.data * current2.data) + carry;
                    if (current1.previous == null) {
                        if (x <= 9) {
                            if (count == 0) {
                                resultHead1.data = x;
                                result.firstNode = resultHead1;
                                count++;
                                break;
                            } else {
                                Node newNode = new Node(resultHead1, x, null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;
                            }
                        } else {
                            if (count == 0) {
                                resultHead1.data = (x % 10);
                                result.firstNode = resultHead1;
                                count++;
                                resultHead1 = resultHead1.next;
                                Node newNode = new Node(resultHead1, ((x / 10) % 10), null);
                                //resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;

                            } else {
                                Node newNode = new Node(resultHead1, (x % 10), null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                Node newNode2 = new Node(resultHead1, ((x / 10) % 10), null);
                                resultHead1.next = newNode2;
                                resultHead1 = newNode2;
                                result.numberOfDigits++;
                                break;
                            }
                        }
                    }
                    if (x > 9) {
                        product = (x % 10);
                        carry = (x / 10) % 10;
                    } else {
                        product = x;
                        carry = 0;
                    }
                    if (count == 0) {
                        resultHead1.data = product;
                        result.firstNode = resultHead1;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead1, product, null);
                        resultHead1.next = newNode;
                        resultHead1 = newNode;
                        result.numberOfDigits++;
                    }
                    current1 = current1.previous;
                }

                if (zeroes > 0) {
                    result.lastNode = resultHead1;
                    LInfiniteInteger reversed2 = new LInfiniteInteger(0);
                    reversed2.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed2.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed2.numberOfDigits++;
                    }
                    reversed = (LInfiniteInteger) reversed.plus(reversed2);
                } else {
                    result.lastNode = resultHead1;
                    reversed = new LInfiniteInteger(0);
                    reversed.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed.numberOfDigits++;
                    }
                }

                current2 = current2.previous;
                zeroes++;

            }
            trueResult = new LInfiniteInteger(reversed.toString());
            trueResult.isNegative = false;
            if (trueResult.firstNode.data == 0) {
                trueResult.isNegative = false;
            }

            return trueResult;

        }
        if (this.isNegative() == false && anInfiniteInteger.isNegative() == false) {

            int zeroes = 0;
            LInfiniteInteger trueResult = new LInfiniteInteger(0);
            LInfiniteInteger reversed = null;
            while (current2 != null) {
                current1 = temp;
                int product = 0;
                int carry = 0;
                int count = 0;

                result = new LInfiniteInteger(0);
                Node resultHead1 = result.firstNode;

                if (zeroes > 0) {
                    int i = 0;
                    while (i < zeroes) {
                        if (count == 0) {
                            resultHead1.data = 0;
                            result.firstNode = resultHead1;
                            count++;
                            i++;
                        } else {
                            Node newNode = new Node(resultHead1, 0, null);
                            resultHead1.next = newNode;
                            resultHead1 = newNode;
                            result.numberOfDigits++;
                            i++;
                        }
                    }
                }

                while (current1 != null) {

                    int x = (current1.data * current2.data) + carry;
                    if (current1.previous == null) {
                        if (x <= 9) {
                            if (count == 0) {
                                resultHead1.data = x;
                                result.firstNode = resultHead1;
                                count++;
                                break;
                            } else {
                                Node newNode = new Node(resultHead1, x, null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;
                            }
                        } else {
                            if (count == 0) {
                                resultHead1.data = (x % 10);
                                result.firstNode = resultHead1;
                                count++;
                                resultHead1 = resultHead1.next;
                                Node newNode = new Node(resultHead1, ((x / 10) % 10), null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;

                            } else {
                                Node newNode = new Node(resultHead1, (x % 10), null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                Node newNode2 = new Node(resultHead1, ((x / 10) % 10), null);
                                resultHead1.next = newNode2;
                                resultHead1 = newNode2;
                                result.numberOfDigits++;
                                break;
                            }
                        }
                    }
                    if (x > 9) {
                        product = (x % 10);
                        carry = (x / 10) % 10;
                    } else {
                        product = x;
                        carry = 0;
                    }
                    if (count == 0) {
                        resultHead1.data = product;
                        result.firstNode = resultHead1;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead1, product, null);
                        resultHead1.next = newNode;
                        resultHead1 = newNode;
                        result.numberOfDigits++;
                    }
                    current1 = current1.previous;
                }

                if (zeroes > 0) {
                    result.lastNode = resultHead1;
                    LInfiniteInteger reversed2 = new LInfiniteInteger(0);
                    reversed2.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed2.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed2.numberOfDigits++;
                    }
                    reversed = (LInfiniteInteger) reversed.plus(reversed2);
                } else {
                    result.lastNode = resultHead1;
                    reversed = new LInfiniteInteger(0);
                    reversed.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed.numberOfDigits++;
                    }
                }

                current2 = current2.previous;
                zeroes++;

            }
            trueResult = new LInfiniteInteger(reversed.toString());
            trueResult.isNegative = false;
            if (trueResult.firstNode.data == 0) {
                trueResult.isNegative = false;
            }
            return trueResult;

        }
        if (this.isNegative() == true && anInfiniteInteger.isNegative() == false) {

            int zeroes = 0;
            LInfiniteInteger trueResult = new LInfiniteInteger(0);
            LInfiniteInteger reversed = null;
            while (current2 != null) {
                current1 = temp;
                int product = 0;
                int carry = 0;
                int count = 0;

                result = new LInfiniteInteger(0);
                Node resultHead1 = result.firstNode;

                if (zeroes > 0) {
                    int i = 0;
                    while (i < zeroes) {
                        if (count == 0) {
                            resultHead1.data = 0;
                            result.firstNode = resultHead1;
                            count++;
                            i++;
                        } else {
                            Node newNode = new Node(resultHead1, 0, null);
                            resultHead1.next = newNode;
                            resultHead1 = newNode;
                            result.numberOfDigits++;
                            i++;
                        }
                    }
                }

                while (current1 != null) {

                    int x = (current1.data * current2.data) + carry;
                    if (current1.previous == null) {
                        if (x <= 9) {
                            if (count == 0) {
                                resultHead1.data = x;
                                result.firstNode = resultHead1;
                                count++;
                                break;
                            } else {
                                Node newNode = new Node(resultHead1, x, null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;
                            }
                        } else {
                            if (count == 0) {
                                resultHead1.data = (x % 10);
                                result.firstNode = resultHead1;
                                count++;
                                resultHead1 = resultHead1.next;
                                Node newNode = new Node(resultHead1, ((x / 10) % 10), null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;

                            } else {
                                Node newNode = new Node(resultHead1, (x % 10), null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                Node newNode2 = new Node(resultHead1, ((x / 10) % 10), null);
                                resultHead1.next = newNode2;
                                resultHead1 = newNode2;
                                result.numberOfDigits++;
                                break;
                            }
                        }
                    }
                    if (x > 9) {
                        product = (x % 10);
                        carry = (x / 10) % 10;
                    } else {
                        product = x;
                        carry = 0;
                    }
                    if (count == 0) {
                        resultHead1.data = product;
                        result.firstNode = resultHead1;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead1, product, null);
                        resultHead1.next = newNode;
                        resultHead1 = newNode;
                        result.numberOfDigits++;
                    }
                    current1 = current1.previous;
                }

                if (zeroes > 0) {
                    result.lastNode = resultHead1;
                    LInfiniteInteger reversed2 = new LInfiniteInteger(0);
                    reversed2.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed2.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed2.numberOfDigits++;
                    }
                    reversed = (LInfiniteInteger) reversed.plus(reversed2);
                } else {
                    result.lastNode = resultHead1;
                    reversed = new LInfiniteInteger(0);
                    reversed.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed.numberOfDigits++;
                    }
                }

                current2 = current2.previous;
                zeroes++;

            }
            trueResult = new LInfiniteInteger(reversed.toString());
            trueResult.isNegative = true;
            if (trueResult.firstNode.data == 0) {
                trueResult.isNegative = false;
            }
            return trueResult;
        }

        if (this.isNegative() == false && anInfiniteInteger.isNegative() == true) {

            int zeroes = 0;
            LInfiniteInteger trueResult = new LInfiniteInteger(0);
            LInfiniteInteger reversed = null;
            while (current2 != null) {
                current1 = temp;
                int product = 0;
                int carry = 0;
                int count = 0;

                result = new LInfiniteInteger(0);
                Node resultHead1 = result.firstNode;

                if (zeroes > 0) {
                    int i = 0;
                    while (i < zeroes) {
                        if (count == 0) {
                            resultHead1.data = 0;
                            result.firstNode = resultHead1;
                            count++;
                            i++;
                        } else {
                            Node newNode = new Node(resultHead1, 0, null);
                            resultHead1.next = newNode;
                            resultHead1 = newNode;
                            result.numberOfDigits++;
                            i++;
                        }
                    }
                }

                while (current1 != null) {

                    int x = (current1.data * current2.data) + carry;
                    if (current1.previous == null) {
                        if (x <= 9) {
                            if (count == 0) {
                                resultHead1.data = x;
                                result.firstNode = resultHead1;
                                count++;
                                break;
                            } else {
                                Node newNode = new Node(resultHead1, x, null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;
                            }
                        } else {
                            if (count == 0) {
                                resultHead1.data = (x % 10);
                                result.firstNode = resultHead1;
                                count++;
                                resultHead1 = resultHead1.next;
                                Node newNode = new Node(resultHead1, ((x / 10) % 10), null);
                                //resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                break;

                            } else {
                                Node newNode = new Node(resultHead1, (x % 10), null);
                                resultHead1.next = newNode;
                                resultHead1 = newNode;
                                result.numberOfDigits++;
                                Node newNode2 = new Node(resultHead1, ((x / 10) % 10), null);
                                resultHead1.next = newNode2;
                                resultHead1 = newNode2;
                                result.numberOfDigits++;
                                break;
                            }
                        }
                    }
                    if (x > 9) {
                        product = (x % 10);
                        carry = (x / 10) % 10;
                    } else {
                        product = x;
                        carry = 0;
                    }
                    if (count == 0) {
                        resultHead1.data = product;
                        result.firstNode = resultHead1;
                        count++;
                    } else {
                        Node newNode = new Node(resultHead1, product, null);
                        resultHead1.next = newNode;
                        resultHead1 = newNode;
                        result.numberOfDigits++;
                    }
                    current1 = current1.previous;
                }

                if (zeroes > 0) {
                    result.lastNode = resultHead1;
                    LInfiniteInteger reversed2 = new LInfiniteInteger(0);
                    reversed2.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed2.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed2.numberOfDigits++;
                    }
                    reversed = (LInfiniteInteger) reversed.plus(reversed2);
                } else {
                    result.lastNode = resultHead1;
                    reversed = new LInfiniteInteger(0);
                    reversed.firstNode.data = resultHead1.data;
                    resultHead1 = resultHead1.previous;
                    Node reversedHead = reversed.firstNode;

                    while (resultHead1 != null) {
                        Node newNode = new Node(reversedHead, resultHead1.data, null);
                        reversedHead.next = newNode;
                        reversedHead = newNode;
                        resultHead1 = resultHead1.previous;
                        reversed.numberOfDigits++;
                    }
                }

                current2 = current2.previous;
                zeroes++;

            }
            trueResult = new LInfiniteInteger(reversed.toString());
            trueResult.isNegative = true;
            if (trueResult.firstNode.data == 0) {
                trueResult.isNegative = false;
            }

            return trueResult;

        }

        return anInfiniteInteger;
    }

    private class Node {

        private int data;
        private Node next;
        private Node previous;

        private Node(Node previousNode, int aData, Node nextNode) {
            previous = previousNode;
            data = aData;
            next = nextNode;
        }

        private Node(int aData) {
            this(null, aData, null);
        }
    }
}
