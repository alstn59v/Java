package sum123Baekjoon;

import java.util.Scanner;

public class Sum123Baekjoon {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt(); // the number of test-case

		for(int i = 0; i < T; i++) {
			int n = sc.nextInt(); // input n
			System.out.println(sum123(n)); // print result
		}

		sc.close();
	}

	static int sum123(int n) {
		int n1 = 1; // n = 1 -> (1)
		int n2 = 2; // n = 2 -> (1, 1), (2)
		int n3 = 4; // n = 3 -> (1, 1, 1), (1, 2), (2, 1), (3)
		int nk = 0; // n is over 3, result

		if(n == 1) {
			nk = n1;
		}
		else if(n == 2) {
			nk = n2;
		}
		else if(n == 3) {
			nk = n3;
		}
		else {
			for(int i = 3; i < n; i++) {
				nk = n3 + n2 + n1;
				n1 = n2;
				n2 = n3;
				n3 = nk;
			}
		}

		return nk;
	}
}
