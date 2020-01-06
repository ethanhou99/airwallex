package rpn;

import java.util.Scanner;

/**
 * @author jun.Y
 * @date 2020/1/6 3:33 下午
 **/
public class RpnMain {
    public static void main(String[] args) {
        RpnCalc rpnCalculator = new RpnCalc(15);
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input：");
        while (true) {
            String intputMsg = sc.nextLine();
            if (intputMsg.equals("exit")) {
                break;
            }
            System.out.println("stack:" + rpnCalculator.doCalculation(intputMsg));
        }

    }
}

