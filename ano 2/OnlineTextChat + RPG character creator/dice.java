package TextChat;
public class dice {

    public static String [] roll (String[] args) {
        int dice = Integer.parseInt(args[1]);
        int sides = Integer.parseInt(args[2]);
        int mod = Integer.parseInt(args[3]);
        String [] finale = new String[dice];

        for (int i = 0; i < dice; i++) {
            Integer roll = (int) (Math.random() * sides) + 1;
            int total = roll + mod;
            String resultado = total + " " + "(" + roll.toString() + " " + "+" + " " + String.valueOf(mod) + ")";
            finale[i] = resultado;
        }
        return finale;

    }
}


