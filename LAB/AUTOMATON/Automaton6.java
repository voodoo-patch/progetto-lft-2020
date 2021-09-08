package AUTOMATON;

//Riconoscitore di stringhe binarie divisibili per 3
public class Automaton6 {
	public static boolean scan(String s) {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);

			switch (state) {
				case 0:
					if (ch == 48)
						state = 0;
					else if (ch == 49)
						state = 1;
					else
						state = -1;
					break;

				case 1:
					if (ch == 49)
						state = 0;
					else if (ch == 48)
						state = 2;
					else
						state = -1;
					break;

				case 2:
					if (ch == 49)
						state = 2;
					else if (ch == 48)
						state = 1;
					else
						state = -1;
					break;

			}
		}
		return state == 0;
	}

	public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}