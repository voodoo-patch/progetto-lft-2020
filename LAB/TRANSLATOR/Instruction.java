package TRANSLATOR;

public class Instruction {
    public enum OpCode {
        ldc, imul, ineg, idiv, iadd,
        isub, istore, ior, iand, iload,
        if_icmpeq, if_icmple, if_icmplt, if_icmpne, if_icmpge,
        if_icmpgt, ifne, GOto, invokestatic, label
    }

    OpCode opCode;
    int operand;

    public Instruction(OpCode opCode) {
        this.opCode = opCode;
    }

    public Instruction(OpCode opCode, int operand) {
        this.opCode = opCode;
        this.operand = operand;
    }

    public static int GetNeutral(OpCode operation) {
        if (operation == OpCode.iadd)
            return 0;
        else if (operation == OpCode.imul)
            return 1;
        else
            return -1;
    }

    public static OpCode getOppositeRelop(OpCode operation) {
        switch (operation) {
            case if_icmpeq:
                return OpCode.if_icmpne;
            case if_icmpne:
                return OpCode.if_icmpeq;
            case if_icmpgt:
                return OpCode.if_icmple;
            case if_icmpge:
                return OpCode.if_icmplt;
            case if_icmplt:
                return OpCode.if_icmpge;
            case if_icmple:
                return OpCode.if_icmpgt;
            default:
                return null;
        }
    }

    public String toJasmin() {
        String temp = "";
        switch (this.opCode) {
            case ldc:
                temp = " ldc " + operand + "\n";
                break;
            case invokestatic:
                if (operand == 1)
                    temp = " invokestatic " + "Output/print(I)V" + "\n";
                else
                    temp = " invokestatic " + "Output/read()I" + "\n";
                break;
            case iadd:
                temp = " iadd " + "\n";
                break;
            case imul:
                temp = " imul " + "\n";
                break;
            case idiv:
                temp = " idiv " + "\n";
                break;
            case isub:
                temp = " isub " + "\n";
                break;
            case ineg:
                temp = " ineg " + "\n";
                break;
            case istore:
                temp = " istore " + operand + "\n";
                break;
            case ior:
                temp = " ior " + "\n";
                break;
            case iand:
                temp = " iand " + "\n";
                break;
            case iload:
                temp = " iload " + operand + "\n";
                break;
            case if_icmpeq:
                temp = " if_icmpeq L" + operand + "\n";
                break;
            case if_icmple:
                temp = " if_icmple L" + operand + "\n";
                break;
            case if_icmplt:
                temp = " if_icmplt L" + operand + "\n";
                break;
            case if_icmpne:
                temp = " if_icmpne L" + operand + "\n";
                break;
            case if_icmpge:
                temp = " if_icmpge L" + operand + "\n";
                break;
            case if_icmpgt:
                temp = " if_icmpgt L" + operand + "\n";
                break;
            case ifne:
                temp = " ifne L" + operand + "\n";
                break;
            case GOto:
                temp = " goto L" + operand + "\n";
                break;
            case label:
                temp = "L" + operand + ":\n";
                break;
        }
        return temp;
    }
}