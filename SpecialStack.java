class Stack {
    private static final int MAX_COUNT = 10;
    private int[] data = new int[MAX_COUNT];
    private int top;

    public boolean isEmpty() {
        if (top <= 0) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (top >= MAX_COUNT) {
            return true;
        }
        return false;
    }

    public void push(int value) {
        if (isFull()) {
            throw new StackIndexOutOFBoundException("Stack is full");
        }
        data[top] = value;
        top++;
    }

    public int pop() {
        if (isEmpty()) {
            throw new StackIndexOutOFBoundException("Stack is empty");
        }
        top--;
        return data[top];

    }
}

class StackIndexOutOFBoundException extends RuntimeException {
    public StackIndexOutOFBoundException(String msg) {
        super(msg);
    }
}

public class SpecialStack extends Stack {

    private Stack minStack = new Stack();

    public int getMin() {
        int value = minStack.pop();
        minStack.push(value);
        return value;
    }

    public void push(int value) {
        if (isEmpty()) {
            super.push(value);
            minStack.push(value);
        } else {
            int min = minStack.pop();
            minStack.push(min);
            super.push(value);
            if (min < value ) {
                minStack.push(min);
            } else {
                minStack.push(value);
            }
        }
    }

    public int pop() {
        int value =  super.pop();
        minStack.pop();
        return  value;
    }
}
