package com.Example.controller.Test;
import core.util.Encrypt;
import com.base.util.Utility;
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 System.out.println("Hello, World!");
		 
		 try {
			String aa = Encrypt.strAESEncrypt("1" + "---" + Utility.instance().getTodayDate(), "07811dc607811dc607811dc607811dc6");
			 System.out.println(aa);
			 String bb = "gK68gipAtUASTZQtpw3jVxOeShItPUJj";
			 System.out.println(Encrypt.strAESDecrypt(bb, "07811dc607811dc607811dc607811dc6"));
			 System.out.println(Encrypt.strAESDecrypt(aa, "07811dc607811dc607811dc607811dc6"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
