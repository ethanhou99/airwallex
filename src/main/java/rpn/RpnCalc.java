package rpn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author jun.Y
 * @date 2020/1/6 3:37 下午
 **/
public class RpnCalc {
    private Stack<BigDecimal> allNum;
    private Stack<BigDecimal> numberBackup;
    private Stack<String> operatorBackUp;
    private int scale;

    public RpnCalc(int scale) {
        this.scale = scale;

        if (allNum == null) {
            allNum = new Stack<>();
        }

        if (numberBackup == null) {
            numberBackup = new Stack<>();
        }

        if (operatorBackUp == null) {
            operatorBackUp = new Stack<>();
        }
    }

    public String doCalculation(String inputStr) {
        String[] arrayList = inputStr.split(" ");
        Queue<String> strQueue = buildQueue(arrayList);
        return calculate(strQueue);
    }

    private String calculate(Queue<String> msgQueue) {
        //String currentResult = null;
        String errorMsg = null;
        int pos = 1;
        if (msgQueue.isEmpty() || msgQueue.size() == 0) {
            return null;
        }

        while (msgQueue.size() != 0) {
            String val = msgQueue.poll();
            if (isNumberCheck(val)) {
                allNum.push(new BigDecimal(val));
                pos += 2;
                continue;
            }

            operatorBackUp.push(val);
            switch (val) {
                case "clear":
                    doClear();
                    break;
                case "sqrt":
                    sqrt();
                    break;
                case "undo":
                    undo();
                    break;
                default:
                    if (allNum.size() == 1 || allNum.size() == 0) {
                        errorMsg = "operator "+val+"(position:"+pos+"):insufficient parameters";
                        System.out.println(errorMsg);
                        break;
                    }
                    doNormalOperate(val);
            }
            pos += 2;

        }
        return allNum.toString();
    }

    private Queue<String> buildQueue(String[] arrayList) {
        Queue<String> queue = new LinkedList<>();
        for (String arr : arrayList) {
            queue.offer(arr);
        }
        return queue;
    }

    private boolean isNumberCheck(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = str.length(); --i >= 0; ) {
            char val = str.charAt(i);
            if (!Character.isDigit(val) || val == '.') {
                return false;
            }
        }
        return true;
    }

    private String sqrt() {
        BigDecimal num = allNum.pop();
        numberBackup.push(num);
        //numsBackUp.push("sqrt");
        allNum.push(new BigDecimal(doSqrt(num)));
        return allNum.peek().toString();
    }

    private String doSqrt(BigDecimal in) {
        BigDecimal sqrt = new BigDecimal(1);
        sqrt.setScale(scale + 3, RoundingMode.FLOOR);
        BigDecimal store = new BigDecimal(in.toString());
        boolean first = true;
        do {
            if (!first) {
                store = new BigDecimal(sqrt.toString());
            } else
            {
                first = false;
            }
            store.setScale(scale + 3, RoundingMode.FLOOR);
            sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(
                    BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
        } while (!store.equals(sqrt));
        return sqrt.setScale(scale, RoundingMode.FLOOR).stripTrailingZeros().toPlainString();
    }

    private String doNormalOperate(String val) {
        BigDecimal num2 = allNum.pop();
        BigDecimal num1 = allNum.pop();
        BigDecimal result = normalOperate(val.charAt(0), num1, num2);
        allNum.push(result);
        numberBackup.push(num2);
        numberBackup.push(num1);
        return allNum.peek().toString();

    }

    private BigDecimal normalOperate(char op, BigDecimal num1, BigDecimal num2) {
        switch (op) {
            case '+':
                return num1.add(num2);
            case '-':
                return num1.subtract(num2);
            case '*':
                return num1.multiply(num2);
            case '/':
                return num1.divide(num2);
            default:
                return null;
        }
    }

    private void doClear() {
        allNum.clear();
        numberBackup.clear();
        operatorBackUp.clear();
    }

    private void undo() {
        if (allNum.size() == 0) {
            return;
        }
        allNum.pop();
        allNum.push(numberBackup.pop());

        /*if (operatorBackUp.pop().equals("sqrt")) {
            allNum.push(numberBackup.pop());
        } else {
            allNum.push(numberBackup.pop());
        }*/
    }

}
