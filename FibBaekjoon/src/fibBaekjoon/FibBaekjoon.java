/**
 * Created on Jan 6, 2018
 * @author msjo -- hufs.ac.kr, Dept of CES
 * Copy Right -- Free for Educational Purpose
 */
package fibBaekjoon;

import java.util.Scanner;

public class FibBaekjoon {
	static int zeroCount = 0; // '0'이 호출 된 횟수
	static int oneCount = 0; // '1'이 호출 된 횟수
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt(); // 테스트 케이스의 개수
		
		for(int i = 0; i < T; i++) {
			int n = sc.nextInt();
			fibonacci(n); // 함수 호출을 통한 결과 도출
			System.out.println(zeroCount + " " + oneCount); // 결과 출력
			zeroCount = 0; // '0'이 호출 된 횟수 초기화
			oneCount = 0; // '1'이 호출 된 횟수 초기화
		}
		
		sc.close();
	}
	
	static int fibonacci(int n) {
	    if(n == 0) {
	        zeroCount++;
	        return 0;
	    }
	    else if(n == 1) {
	        oneCount++;
	        return 1;
	    }
	    else {
	        return fibonacci(n-1) + fibonacci(n-2);
	    }
	}
}
