package com.base.util.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

import com.BwleErp.config.Config;

public class TestSecurity {
	public static void main(String[] args) {
		// 1.生成证书
        /*File file = new File(Config.CAPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            KeyPairUtil.initAndStoreKeyPair(fileOutputStream);
        } catch (FileNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

		// 2.生成数字签名
        DataSecurity dataSecurity = new DataSecurity();
        String sign = dataSecurity.sign("大家好");
        System.out.println("sign:" + sign);*/

		// 3.验证数字签名
		DataSecurity dataSecurity_verify = new DataSecurity();
		boolean result = dataSecurity_verify.verifySign("大家好",
				"MCwCFAvsDcs4YWbct/pvFkYSqOXzCATcAhR7VG12z/PAznVexjsGRnSDXm2RKw==");
		System.out.println("result：" + result);

	}
}
