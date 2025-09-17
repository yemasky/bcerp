package core.other.test;

public class test {
	public static void main(String[] args) {

		/*HashMap<String, String> fieldhashMap = new HashMap<>();
		fieldhashMap.put("aa", "1");
		Set<String> keySet = fieldhashMap.keySet();
		System.out.println(ObjectUtils.nullSafeToString(keySet));
		fieldhashMap.put("bb", "1");
		fieldhashMap.put("cc", "1");
		String dd = ObjectUtils.nullSafeToString(keySet);
		System.out.println(dd.substring(1, dd.length() - 1));
		ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
		dd = ObjectUtils.nullSafeToString(listOfKeys);
		System.out.println(dd.substring(1, dd.length() - 1));

		MemberFrom memberFrom = new MemberFrom();
		Field[] field = memberFrom.getClass().getDeclaredFields();
		for (Field f : field) {
			System.out.println(f.getName());
		}
		Method[] md = memberFrom.getClass().getDeclaredMethods();
		for (Method m : md) {
			// System.out.println(m.getDefaultValue());
			try {
				//
				String methodName = m.getName();
				if (methodName.startsWith("get")) {
					Object tempObj = m.invoke(memberFrom);
					System.out.println(methodName + "==>" + tempObj + "-->" + m.getDefaultValue() + "-->"
							+ ObjectUtils.isEmpty(tempObj));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String table = "MemberFormThis";
		String str = "([A-Z]+?)";
		// str = "id=an(.*?)usg";
		Pattern pattern = Pattern.compile(str);
		Matcher sss = pattern.matcher(table);
		int i = 0;
		while (sss.find()) {
			if (i > 0) {
				// System.out.println(sss.group());
				table = table.replace(sss.group(), "_" + sss.group().toLowerCase());
			}
			i++;
		}*/
		// System.out.println(table.toLowerCase());
	}
}
