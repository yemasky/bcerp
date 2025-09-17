package core.custom_interface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String name();
	String jdbcType() default "String";
	boolean primary_key() default false;// PRIMARYKEY(); 主键
	boolean auto_increment() default false;//AUTO_INCREMENT();MySql自增
	boolean ignore() default false;//忽略的字段不参与读取与写入 例：系统自动增的键
	boolean update_ignore() default false;//更新的时候忽略这个键 
	boolean select_ignore() default false;//查询的时候忽略这字段 总开关控制在whereCondition select_show也为false时生效，默认false
}
