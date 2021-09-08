package AUTOMATON;

public class Automaton7 {
	public static boolean scan(String s) {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);

			switch (state) {
				case 0:
					if (ch == 97) // a
						state = 1;
					else if (ch == 98) // b
						state = 2;
					else
						state = -1;
					break;

				case 1: // stato finale
					if (ch == 97 || ch == 98) // a || b
						state = 1;
					else
						state = -1;
					break;

				case 2:
					if (ch == 97) // a
						state = 1;
					else if (ch == 98) // b
						state = 3;
					else
						state = -1;
					break;

				case 3:
					if (ch == 97) // a
						state = 1;
					else
						state = -1;
					break;

			}
		}
		return state == 1;
	}

	public static void main(String[] args) {
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
	}
}